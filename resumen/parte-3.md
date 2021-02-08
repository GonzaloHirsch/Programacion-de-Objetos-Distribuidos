# RMI
[Go to Index](resumen.md)

## Sistemas Distribuidos

El modelo concurrente se ve limitado por la capacidad del computador. Lo que se quiere incrementar es la capacidad de **procesamiento** y **almacenamiento**.

Un **Sistema Distribuido** es una colección de computadoras (*nodos*) autónomas débilmente acopladas conectas en una red. Cada nodo tiene lo necesario para operar por sí solo, cada proceso tiene su espacio de direccionamiento y los nodos se comunican por una red.

El modelo definido, al ser implementado, vuelve transparentes a mucho problemas, y cada sistema debe elegir cuales implementa o no:
- **ACCESO** --> Esconde la complejidad de invocar servicios remotos y la heterogeneidad de representación.
- **UBICACIÓN** --> Provee un acceso lógico a través de nombres independientes de la ubicación física del servicio.
- **MIGRACIÓN** --> Oculta que los componentes puedan ser migrados de ubicación física.
- **RELOCACIÓN** --> Permite cambiar las interfaces logrando evitar o resolver inconsistencias a la hora de la transición.
- **FALLAS** --> Oculta que una parte del servicio puede fallar y el potencial recover por otra parte del mismo.
- **REPLICACIÓN** --> Oculta que un servicio puede ser resuelto por uno de varios componentes compatibles.
- **PERSISTENCIA** --> El sistema debe poder agregar y quitar nodos y recursos sin modificar el comportamiento.
- **TRANSACCIÓN** --> Oculta la interacción de varios componentes internos para resolver la operación.

## Servicios

**Servicio** --> Parte de un sistema que maneja recursos y presenta su funcionalidad a usuarios restringiendo sus posibilidades a un conjunto de acciones definidas en la API.

**Servidor** --> Proceso en una red de computadoras que *ejecuta las operaciones ofrecidas* por la API del servicio.

**Cliente** --> Proceso en una red de computadoras que *solicita la ejecución de una operación* de un servicio.

Hay varios modelos para el esquema Cliente-Servidor:
- Request-Reply a un único punto
- Request-Reply con colaboración interna dentro del servicio
- Proxy y localidad del cache
- Peer, la coordinación y comunicación se hace simétricamente

Sin usar frameworks, la comunicación entre nodos se hace por **sockets** (IP + puerto).

Idealmente se debería separar en capas para abstraer lo mayor posible cada porción de código. La capa que separa la aplicación de la comunicación es el **Middleware**.

## RPC

RPC es **Remote Procedure Call**, es un protocolo que especifica el llamado de ejecución de funciones remotas sin preocuparse por comunicación cliente-servidor. Establece que hay un *middleware* que se ocupa de establecer la comunicación, traducir el llamado y transformar la respuesta.

## RMI

RMI es **Remote Method Invocation**, es un mecanismo para ejecutar métodos en objetos remotos, y el objetivo es hacer que esta comunicación sea lo más transparente posible.

RMI usa la semántica *At most once*, que significa que se asegura que el mensaje recibido no se vuelva a ejecutar, por lo que se ejecuta 0 o 1 vez.

### Definiciones

**Interfaz Remota** --> Interfaz que declara conjunto de métodos que el cliente puede invocar desde una JVM remota. Cliente y servidor deben tenerlo, define el contrato del servicio. Debe ser `public` y extender de `Remote`. Todo método declarado debe tener un throws de `RemoteException`, y los objetos que son parámetros o devueltos deben implementar `Serializable`.

**Objeto Remoto / Servant** --> Objeto que se encarga de hacer las operaciones. Está en el servidor e implementa la interfaz remota, y su ejecución es en el servidor. Debe implementar al menos 1 interfaz remota, y debe ser exportado con `UnicastRemoteObject` o `Activatable`. Debería ser *thread-safe*. Hay 3 formas de exportarlo:
```java
// FORMA 1 --> extender de UnicastRemoteObject
public class GenericServiceImpl extends UnicastRemoteObject implements GenericService {
    public GenericServiceImpl() throws RemoteException { ...

// FORMA 2 --> export dentro del constructor del servant
public class GenericServiceImpl implements GenericService {
    public GenericServiceImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0); ...

// FORMA 3 --> instanciar el objeto y exportarlo (en el objeto que construya).
final GenericService gs = new GenericServiceImpl();
final Remote remote = UnicastRemoteObject.exportObject(gs, 0);
```

**Referencia de Objeto Remoto** --> Objeto que le permite al cliente hacer llamadas remotas como si fueran locales, reside en el cliente.

**Stub** --> Es proxy a un objeto remoto en el cliente. Implementa los mismos métodos que la interfaz remota. Recibe invocaciones del cliente y las pasa por la red, espera resultados para enviarlos al cliente.

**Skeleton** --> Se crea en el servidor y se encarga de recibir el pedido del cliente, interpretarlo, obtener parámteros y llamar al objeto remoto. Forwardea la respuesta al cliente cuando la recibe. Abstrae al objeto remoto de la comunicación.

**Server** --> Aplicación encargada de instanciar al Servant y los recursos necesarios, además de asegurarse que se exporte y publique en el servidor de nombres.

**Servidor de Nombres / Servidor de Directorio** --> Servicios que proveen una abstracción entre el cliente y los recursos compartidos. Registra al servicio con un mapeo a un nombre asociado. Java provee al *RMI Registry*, que por su forma de trabajar viola 3 de las transparencias:
- *Replicación* --> Mapeo es 1 a 1, por lo que no se puede tener a otro servant atendiendo al mismo servicio.
- *Fallas* --> No puede trabajar en cluster ni ofrecer High Availability, es un Single Point of Failure.
- *Ubicación* --> Solo acepta servants que están en localhost.

**Client-Side Polling** --> El cliente pregunta de vez en cuando si hay una respuesta. Hace sleeps de vez en cuando hasta que haya una respuesta. (RMI Asincrónico)

**Server-Side Pushing** --> Cuando el cliente llama al servidor, le provee una forma de avisarle cuando termina. Esa manera es una función de callback que el servidor llama cuando termina con la respuesta. Este callback se pasa como un objeto remoto implementando una interfaz remota con el método de callback, el cliente le pasa este objeto al servidor. (RMI Asincrónico)

**Distributed Garbage Collector** --> Algoritmo de recolección de memoria que se basa en conteo de referencias, y en la coordinación del garbage collector del cliente y servidor. Es complicado de mantener y consume muchos recursos. (RMI Remote Garbage Collector)

**Leasing** --> Algoritmo de conteo de referencias, cada referencia que va al cliente tiene un período de lease que el cliente debe ocuparse de renovar. Si no lo renueva, el servidor asume que fue liberada. (RMI Remote Garbage Collector)

### Cliente Remoto 

El cliente remoto necesita obtener una instancia del objeto remoto, por lo que hace un lookup para ese objeto:
```java
// Así se obtiene la instancia, y el método debería tener un "throws Exception"
(GenericService)Naming.lookup("//xx.xx.xx.xx:1099/service");
```

### Pasaje de Parametros

Hay 3 posibles casos
1. Si el tipo es **built-in** se pasa por **valor**.
2. Si el tipo es **objeto no remoto** se pasa por **valor**.
3. Si el tipo es **objeto remoto** se pasa por **referencia**.

### Serializacion y Marshalizacion

**Serializar** --> Convertir el estado de un objeto en un stream de bytes para que pueda ser utilizado después. Se puede hacer con las clases `ObjectInputStream` (leer) y `ObjectOutputStream` (escribir).

**Marshalling** --> Codifica información que contiene el objeto y también la información de como construirlo.

**De-Serializar** --> Tomar un stream de bytes y obtener a partir de el un objeto con la misma información que el original.

**Un-Marshalling** --> Similar a de-serializar, pero también permite obtener la definición del objeto.

Para que un objeto sea serializable, debe implementar la interfaz `Serializable`, y todos sus atributos no primitivos deberían implementar `Serializable` también. Esta interfaz no tiene métodos, es simplemente una marca de que puede ser serializable.

Se puede agregar un `serialVersionUID` que indica que versión del objeto se está utilizando. Sirve para versionar las versiones del cliente/servidor para ver que matcheen los números, si no matchea falla.

### Semánticas

Hay diferentes semánticas que se pueden usar:
- **Exactly once** --> Cliente y servidor colaboran para que el mensaje se ejecute exactamente una vez
- **Maybe** --> El cliente envía el mensaje y se despreocupa si llega o no
- **At least once** --> Cliente envía envía mensaje y espera por la respuesta del servidor indicando si se ejecutó. Si pasa tiempo vuelve a intentar. Asegura que se ejecuta pero puede ser que más de una vez
- **At most once** --> Servidor se encarga de que el mensaje no se vuelva a ejecutar. No garantiza que el mensaje le llegue por lo que puede ejecutarse 0 o 1 veces

### Activation

Los objetos remotos deben quedar activos de manera permanente, que genera que se consuman recursos, si se cae se pierde el estado y necesita constante monitoreo.

**Activatable** genera servants que tienen:
- *Lazy Activation* --> Se activa solo cuando recibe un pedido, despúes de deshabilita
- *Persistance* --> Permite guardar el estado del servant, hay que considerar que la persistencia puede fallar y los archivos se pueden corromper.

Los posibles estados son:
- **Activo** --> Está exportado y siendo usado por JVM remota
- **Pasivo** --> No fue instanciado o exportado todavía

Se utiliza el servicio de *RMID*, que se ocupa de tomar los pedidos del cliente y handlear los casos en donde es necesaria la activación. Instancia un *Activator* y cuando recibe un pedido se lo forwardea al Activator.

**Activator** --> Mantiene información para activar objetos remotos y maneja los JVM donde se activan esos objetos.

**Activation Group** --> Creado por el *Activator* en una JVM independiente para controlar el ciclo de vida del Servant que se le asigna.

Hay 2 tipos de servicios que se pueden implementar con *Activation*:
- **Stateless** --> Cada invocación es independiente y cada request tiene lo necesario para realizar la operación.
- **Stateful** --> Mantiene un estado interno, RMI facilita estos servicios.