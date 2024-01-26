---
layout: default
title:  "Store Distribuido (ejemplos)"
description: "Introducción al concepto de un store distribuido, incluyendo ejemplos de los mismos y cuál es la arquitectura que dichos sistemas usan para funcionar."
active: true
image:
  path: /meta.png
  height: 640
  width: 1280
---

## Store Distribuido
[Go to Index](resumen.md)

## HDFS

HDFS es el **Hadoop Distributed Fule System**, modelado a partir del Google File System, está optimizado para *high throughput* y trabaja mejor *leyendo/escribiendo archivos muy grandes*. Utiliza tamaños de bloque muy grandes, tiene replicación para *fault tolerance* y está diseñado para correr en hardware "no muy wow".

### Arquitectura

Tiene una arquitectura **master/slave**. Un cluster consiste de un solo **NameNode**, que es un master que maneja los nombres del file system y regula el acceso a los archivos para clientes. Hay varios **DataNodes**, generalmente 1 por cada nodo en el cluster, que maneja el almacenamiento de los nodos en los que corren. 

HDFS expone el nombre del file system y permite que los usuarios guarden cosas. Internamente está separado en bloques en diferentes *DataNodes*. El *NameNode* hace las operaciones y determina los mapeos. Los *DataNodes* son responsables de la read/write requests, y además crean, borran y replican bloques.

### Bloque

Los archivos se particionan en bloques de tamaño definido, y cada uno está replicado en diferentes nodos para generar redundancia. Si el archivo es menor que un bloque, solo ocupa el tamaño real.

### NameNode

Administra el namespace del file system y regula acceso de clientes. Administra y mantiene estructura lógica y metadata de los archivos. Conoce el estado de los *DataNode*, y responde a búsquedas y escrituras en los bloques.

Se ocupa de persistir la información a disco usando 2 componentes. El *FsImage* es un snapshot que se hace de vez en cuando del sistema, y el *Transaction Log* guarda escrituras hasta que se actualice *FsImage*.

### DataNode

Guarda los bloques de información, se reporta al *NameNode* cada cierta cantidad de tiempo para informar su estado y que bloques contiene, y se comunica con otros *DataNode* para replicar información.

### Problemas

Hay algunos problemas que puede tener HDFS:
- El *NameNode* es un Single Point of Failure(SPOF)
- Problemas con acceso random a información de los archivos, no está optimizado para leer 1 byte de los archivos, sinó que el archivo entero, necesita HBase para poder optimizarlo.
- Muchos archivos pequeños degradan performance.

### distcp

Es una herramienta de copiado de archivos/directorios entre 2 clusters de HDFS usando un MapReduce Job. Tiene operaciones de *overwrite*, *update*, *delete*, *p* y *log*.

## Hazelcast

**Hazelcast** es un grid de datos en memoria, distribuido y open source. Se puede usar desde caching y microservicios, hasta mensajería y store noSQL.

### Estructuras

Define varias estructuras de datos, como *IMap*, *IList*, *IQueue* e *ITopic*. Hay que usar estas estructutas para que la información esté distribuida.

### Nodos

Hay diferentes tipos de nodos:
- **Node Member** --> Tipo default de nodo, tiene la información distribuida y hace las operaciones distribuidas.
- **Lite Member** --> No guardan información, sirven para lanzar tareas y acciones.
- **Native Client** --> No es un nodo per se, es una manera de conectarse al server como un client. Se usa para poner datos y ejecutar cosas en el cluster.

### Coordinacion

Tiene una arquitectura **peer-to-peer**, de forma que elimina el SPOF. 

Para elegir al **Coordinador**, se usa al nodo con mayor antigüedad. Este nodo tiene una tabla de particiones que dice que nodo es dueño/backup de cada partición, cada vez que llega/se va un nodo se actualiza. Si este nodo se pierde, se elije al próximo en antigüedad y se recalcula esta tabla.

### Particionado

Se divide en 271 particiones entre todos los nodos, todos pueden acceder a todos los datos.

Para asignar las particiones se usa el algoritmo de *Consistent Hashing*:
```java
NroParticion(objeto) = hash(objeto) % 271 
```

### Replicas

Si hay más de 1 nodo, cada nodo tiene 271/N particiones primarias y 271/N particiones de backup de las de otros nodos.

Las réplicas se actualizan *sincrónicamente* con la partición principal (aunque es configurable para que sea asincrónico, por lo que garantiza consistencia eventual, aunque uno puede llegar a leer un dato desactualizado). Por default hay 1 réplica por cada partición, pero es configurable. Si se cae el nodo owner, la réplica pasa a ser el owner.

### Configuracion

**Member Discovery** --> Se puede encontrar a otros miembros por *Multicast* (no recomendado en prod porque usar UDP puede generar problemas), *TCP/IP*, con *frameworks* (Zookeper, Consul) y con *clouds* (AWS, Azure).

**Seguridad** --> Se puede indicar un nombre y contraseña del grupo para que no esté abierto.

**Colecciones** --> Se pueden configurar las colecciones con sus nombres y algunos parámetros.

### Split Brain

Ocurre cuando dado un problema de conexión, el cluster se divide en 2 partes independientes. 

Para resolver esto se elije a un nodo de más antigüedad entre los 2 líderes de los clusters, se decide que cluster se mergea (el más chico o por alguna función), y por cada miembro que se mergea hay que pausar operaciones, cerrar conexión, mergear y reiniciar.

### Serializacion

Toda clase que se envía a Hazelcast debería ser serializable, para esto se pueden implementar las interfaces correspondientes. Es recomendable usar `DataSerializable` porque es más performante.
