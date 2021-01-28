# Respuestas

Respuestas a los ejercicios que no son de programación

##Ejercicio 1

**Interfaz Remota** -> Es una interfaz que declara un conjunto de métodos que un cliente puede invocar desde una máquina virtual Java remota. Es el elemento que deben tener cliente y servidor ya que define el contrato del servicio

**Servant** -> Encargado de realmente hacer las operaciones del servicio. Reside en el servidor e implementa la interfaz remota

**Stub** -> Funciona de proxy a un objeto remoto en el cliente. Implementa la interfaz remota. Pasa las invocaciones al objeto por la red.

**Client** -> Aplicación que solicita la ejecucion de la operacion de un servicio

**Server** -> Aplicación encargada de instancias al servant y los recursos necesarios del mismo

**Servidor de Nombres** -> Provee una capa de abstraccion entre los usuarios y los recursos compartidos. Permite registar al servicio

##Ejercicio 2

Falso porque no hace los throws necesarios

## Ejercicio 3

1 - Tira excepción de connection refused

2 - Tira excepción de connection refused, pero al llamar al addVisits, no al tratar de conectarse en si

3 - Una vez que ya se pudo conectar, sigue funcionando porque la instancia ya la tiene creo. Mientras el servicio ande

4 - Una vez que se corta el servicio, no puede seguir invocando y falla.

5 - Sin cambiar la implementacion, se puede apagar uno de los servicios y ver si anda igual. Sino, se puede agregar a la implementacion un metodo para obetener el nombre.

6 - Si usas el bind() tira una excepcion porque ya esta bindeado. Conviene el rebind() si no queres tener que bajar el otro servicio