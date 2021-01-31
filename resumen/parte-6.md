# Procesamiento Distribuido
[Go to Index](resumen.md)

## Hazelcast MapReduce

**MapReduce** es un modelo de programación para el procesamiento de grandes volúmenes de datos de manera paralela y distribuida a partir de primitivas simples. Se subdividen los datos para procesarlos distribuidamente (*divide & conquer*) y se intenta trabajar localmente los datos lo más posible (*data locality*).

![MapReduce](mapreduce.jpeg "MapReduce")
Las operaciones se encargan de filtrar y transformar los datos (**map**), para luego agregar esos datos para obtener el valor final (**reduce**).

### Datos

Cada unidad de información que se mueve entre las etapas como entrada y salida cuenta con 2 partes:
- **clave** --> Clasifica o identifica a la información
- **valor** --> Contenido del dato

Cada parte puede ser tan compleja como se quiere, o puede ser simplemente un primitivo.

En cada etapa se aplica una función tal que: `f(k1, v1) = [k2, v2]`

### Etapas

Hay 4 diferentes etapas, *map* y *reduce* se programan, mientras que *sort*/*shuffle* es provisto por el framework.

#### Map

Transforma los datos iniciales en información útil para la operación final.

Map se encarga de:
- **filtrar** --> registros no necesarios
- **proyectar** --> no emite valores que no son necesarios para la operacion final
- **expandir** --> agrega información que puede venir de fuentes externas
- **multiplicar** --> en algunas operaciones necesita emitir más de 1 valor

Recibe un `(key, value)` y emite 0, 1 o más pares `(key, value)`.

#### Sort

Toma los valores emitidos por el mapper y los junta según la clave, para luego enviarlos a cada reducer.

#### Reduce

Se usa un reducer por cada clave emitida, y recibe todos los valores para la clave emitida por el/los mapper/s. Procesa los valores y emite 1 o más valores finales para la clave.

### Etapas Ocultas

Hay etapas ocultas además de las mencionadas antes:
- **preliminar** --> se carga la información y hace disponible para el framework
- **intermedia** --> esto es post-mapper, el **Combiner**, donde se sumarizan los datos dentro del mapper, antes de enviarlos al reducer. Son una forma de optimizar los envios por la red, reduciendolos (OPCIONAL)
- **postprocesado**  --> se puede hacer post-reducción, para darle algún formato a los datos o hacer alguna última transformación (OPCIONAL)

## Hadoop

El procesamiento de información tiene ciertas etapas:
```bash
Acquire --> Process --> Store --> Show
```

El **Hadoop Project** tiene un conjunto de componentes diseñado para esto:
- *Hadoop Distributed File System* (HDFS) --> File system distribuido para guardar archivos de altos volumenes y provee gran rendimiento en transmisión de información.
- *Hadoop MapReduce* --> Framework de procesamiento distribuido. Usar como input/output a HDFS.
- *Hadoop YARN* --> Framework de manejo de recursos y tareas distribuidas.

Para facilitar el uso de Hadoop se crearon muchos proyectos "satélites" que tomaron vida propia o se convirtieron en partes de frameworks.

### Criticas

Tiene 2 grandes críticas:
1. Hay problemas difíciles de modelar con MapReduce o cadena de MapReduce.
2. Al persistir todo a disco puede ser más lento por todas las operaciones de IO comparado con otros.

### Herramientas que utilizan Hadoop

#### Flume

**Apache Flume** es un framework de recolección y streameo de datos que puede conectarse a HDFS para lograr una pipeline de recolección de datos eficiente. Está compuesto por un *source* (de donde sale la información), un *channel* (por donde viaja) y un *sink* (donde se junta todo temporalmente).

#### Pig

**Apache Pig** es una plataforma de alto nivel para crear programas MapReduce con Hadoop. Tiene diferentes comandos que se usan para cargar el contenido de un file system y definir las operaciones.

#### Hive

**Apache Hive** es un Data Warehouse construido sobre Hadoop. Tiene su propio lenguaje de querying (HiveQL), que se convierte en trabajos MapReduce que se envían al cluster. *Hive* corre en el nodo master. HiveQL soporta varios operadores de SQL.

Las queries se corren contra tablas (directorio en el HDFS que contiene archivos/s con el contenido de las tablas). La *metadata* de las tablas se guarda en una base relacional no en el HDFS.

## Streaming

Un **stream** es un flujo de datos que no termina y no se interrumpe:
- **Stream Processing** es el procesamiento que obtiene un valor a partir del stream de datos.
- **Real-Time Processing** es un procesamiento que se hace con tanta rapidez que parece que los valores son procesados en tiempo real.

Si bien estos 2 conceptos no son lo mismo, generalmente *Real-Time Processing* implica el uso de un *Stream*.

Ejemplos: timeline de twitter; comentarios, posteos y likes de youtube/instagram/facebook; sensores de máquinas.

### Evento

Un **evento** es una unidad de información dentro de un stream. Armar la ingestión de información de eventos en un stream ayuda a hacer un sistema más escalable/mantenible. La clave es guardar eventos que modifican el sistema.

Se pueden tener:
- *eventos en crudo* --> tienen baja complejidad pero alto volumen, pueden ser inmutables y funcionan como una fuente de verdad. Cuenta como *escritura*.
- *información agregada* --> se puede recalcular a partir de los eventos en crudo, son como una vista cacheada de los eventos en crudo, y responden consultas de los clientes. Cuenta como *lectura*.

Tener estos 2 esquemas diferentes (lectura/escritura) brinda:
- **Desacoplamiento** --> hay independencia entre los esquemas
- **Performance de lectura y escritura** --> cada esquema es óptimo para la operación que debe realizar
- **Escalabilidad** --> como la escritura es “append only” permite paralelizar y escalar de manera simple
- **Flexibilidad** --> al tener muchas “vistas cacheadas” y poder recalcular, se permite responder más preguntas e implementar nuevas features rápidamente
- **Error recovery** --> en caso de error se recalcula todo de nuevo después de corregirlo

### Operaciones

**Filter** --> Es un filtro que decide que datos continuan por el stream y que datos no.

**Join** --> Toma 2 streams y une los datos según cierto criterio.

**Windowing** --> Particionar el stream en batches discretos, se puede usar si no sirve el dato instantaneo, sinó una métrica cada cierta cantidad de tiempo. Se puede separar por el tiempo en el que fue procesado o el tiempo en el que ocurrió el evento.

## Arquitecturas del Procesamiento

### Arquitectura Lambda

La arquitectura lambda surgió para resolver los problemas/necesidades que no podía resolver Hadoop.

La arquitectura tiene los siguientes componentes:
- **Input Buffer** --> soporta y bufferea los datos de entrada con colas
- **Master Dataset** --> colección inmutable de datos, es la fuente de verdad, y sirve como un histórico, se appendean los datos cuando llegan
- **Batch Layer** --> procesa los datos totales hasta ese momento, es periódico
- **Real-Time Layer** --> procesamiento de streaming que calcula valores totales desde el último reprocesamiento hasta el momento actual
- **Serving Layer** --> se encarga de guardar resultados y devolverselos a los clientes, mergea los valores real-time y batch

La crítica de principal de Lambda es que hay que tener la lógica de procesamiento en 2 codebases diferentes, la batch y la real-time.

### Arquitectura Kappa

La arquitectura kappa surge como una mejora a la lambda, con velocidad suficiente para hacer todo real-time.

### Herramientas

#### Apache Storm

Los procesamientos en Storm se ejecutan en topologías compuestas de *spouts* (fuente de datos, lee y escucha eventos de su fuente), *bolts* (consume de un stream, transforma el mensaje y puede emitir a otro stream) y streams (salida de un spoit/bolt que puede conectar a otro bolt).

Al registrar los streams se puede indicar el tipo de distribución que se va a utilizar en sus diferentes componentes.

#### Apache Spark

Engine distribuido para poder realizar procesamiento paralelo y tolerante a fallas.

#### Apache Kafka

Un **Log** es una secuencia de eventos ordenada, append-only. Estos eventos se agregan al final, tienen un número de secuencia y no se borran (en principio). La idea es tener un sistema de logueo y consumo de logs que sean independientes, de forma que se logra una abstracción y no hay tiempos de espera y acoplamiento.

Kafka es un sistema de mensajería diseñado para ser rápido, escalable y durable. Se suele usar como eslabón inicial en una cadena de procesamiento.

##### Conceptos

**Brokers** --> Nodos del sistema, uno por cada nodo lógico del cluster

**Tópico** --> Conjuntos lógicos de mensajes que se quieren enviar

**Productores** --> Clientes que quieren escribir logs, escriben a una partición del tópico dentro del broker

**Mensajes** --> Dentro de la partición del tópico están ordenados por momento de escritura, pares clave-valor, y en principio se persisten todos a disco (pero se evita por problemas de espacio)

**Consumidores** --> Clientes que leen, se les asigna una partición, la leen toda y se les asigna otra

Por defecto no hay esquemas para los mensajes, pero se pueden usar 2 esquemas para los mensajes:
1. **Embebido** --> Esquema va en mensaje
2. **Centralizado** --> Se crea servidor central que guarda los mensajes

##### Streams

*Kafka Streams* es una librería de procesamiento de información provista por Kafka. Permite definir un pipeline de operaciones a realizar que se envía a cada cluster y cada nodo procesa la información que tiene en sus particiones.