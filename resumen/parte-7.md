---
layout: default
title:  "NoSQL, Bases de Datos Distribuidas"
description: "Introducción al concepto de NoSQL, bases de datos distribuidas y cual es el teorema CAP que rige los tipos de bases de datos que existen. Ejemplos de cada tipo."
active: true
image:
  path: /meta.png
  height: 640
  width: 1280
---

## NoSQL
[Go to Index](resumen.md)

Los sistemas NoSQL cumplen ciertas características:
- No usan modelo relacional
- No usan SQL
- No refuerzan un esquema
- Corren en clusters de hardware barato
- Escalan horizontalmente
- Usan otras propiedades en vez de transacciones ACID

Resuelven problemas de escalabilidad con las siguientes estrategias:
- Relajan cumplimientos de esquema
- Quitan/relajan transacciones ACID
- Índices acordes a la operación
- Escalan horizontalmente

## Teorema CAP

El teorema CAP se basa en 3 conceptos:
- **Consistency** --> Todos los nodos devuelven el último dato escrito
- **Availability** --> Garantiza que todo request se responde inmediatamente
- **Partition Tolerance** --> El sistema sigue funcionando aunque se particione o un nodo pierda datos

El teorema indica que no se pueden los 3, sinó que se elijen 2. Como son sistemas que escalan horizontalmente, la `P` debe estar, se elije entre las otras dos. En general suelen elegir la `A` y tener *Consistencia Eventual* (promete que ante una escritura, todos los nodos eventualmente tendrán la misma información, y la cantidad de nodos/replicas afecta a la velocidad de esto).

## Key-Value Store

Base de datos simple que usa un vector para guardar pares clave-valor. La *clave* generalmente es un string, y el *valor* puede ser cualquier cosa.

No hacen falta índices, y la respuesta no se puede controlar porque no hay esquema para los valores.

No suelen tener query language más que unas operaciones simples para guardar, borrar y recuperar. Son rápidas para escritura y lectura, además de fáciles de usar.

### Redis

**Redis** es *Remote Dictionary Server*, es una base de datos en memoria que sirve como cache y broker de mensajes. Soporta distintas estructuras de datos además de tipos primitivos básicos. 

No es recomendable como store permanente, y no puede guardar más información de la que entra en memoria.

## Columnar Store

Base de datos optimizada para leer columnas, sirve más que nada para hacer queries analíticas. Reducen accesos de IO ya que no hace falta hacer un full-scan. Son horizontalmente escalables con hardware barato.

Teniendo un ejemplo con 3 filas y 3 columnas:

|A   	|B   	|C   	|
|---	|---	|---	|
|A1   	|B1   	|C1   	|
|A2   	|B2  	|C2   	|
|A3   	|B3  	|C3   	|

En una base por filas se guardan conceptualmente como:

|A1   	|B1   	|C1   	|A2   	|B2   	|C2   	|A3   	|B3   	|C3   	|
|---	|---	|---	|---	|---	|---	|---	|---	|---	|
|   	|   	|   	|   	|   	|   	|   	|   	|   	|

En una base por columnas se guardan como:

|A1   	|A2   	|A3   	|B1   	|B2   	|B3   	|C1   	|C2   	|C3   	|
|---	|---	|---	|---	|---	|---	|---	|---	|---	|
|   	|   	|   	|   	|   	|   	|   	|   	|   	|

Y probablemente cada columna se guarda/escribe en un archivo separado.

### Apache HBase

**HBase** es una base de datos no relacional, columnar, distribuida, escalable, de baja latencia y random access construida sobre *Apache Hadoop*.

Ventajas:
- Read/Write consistentes
- Sharding automático
- Failover y compactación automático
- Integración con HDFS y MapReduce
- Tiene una API REST

Desventajas:
- No tiene SQL
- No tiene transacciones
- Requiere cluster Hadoop y Zookeper
- Puede tener "hot spots" si no se maneja con cuidado

#### Modelo de Datos

Las **tables** son mapas de filas, cada fila tiene una **primary key**, y cada fila tiene un conjunto de **familia de columnas** que es parte del esquema.

Se pueden agregar columnas dinámicamente, las **celdas** son combinaciones de fila-columna, tienen tipo `byte[]` y un timestamp. Los `NULL` no ocupan espacio.

#### Componentes

Las tablas se componen de una o más **regiones**.

Hay **RegionServer**s, que sirve de a una región a la vez, y estas regiones están colocadas con HDFS.

Cada vez que se quiere hacer una escritura, hay 3 elementos que participan:
1. *HLog* --> Es un *write ahead log* que guarda las operaciones de manera secuencial e inmutable
2. *MemStore* --> Árbol en memoria que guarda las últimas inserciones
3. *HFiles* --> Se genera cuando el memstore crece mucho para guardar a disco

#### Read y Delete

Cuando un cliente pide una fila, hace que pase:
1. Buscar en memstore
2. Si no está, se busca en el cache del RegionServer
3. Si no está, se fija en los HFiles por el registro

El borrado escribe un "tombstone" en MemStore para que devuelva "borrado" más rápido.

### Apache Cassandra

Desarrollado por Facebook y basado en papers de Google y Amazon.

Features claves son:
- **Decentralized** --> cada nodo en el cluster tiene el mismo rol (NO SPOF)
- **Linear scalability** --> la capacidad de lectura y escritura crece lineal con la cantidad de nodos
- **Tunable consistency** --> los niveles de consistencia son configurables, hay muchos tipos, que 1, 2 o 3 nodos sean consistentes, o que haya quorum dentro de un conjunto
- **Multi datacenter replication**
- **Commercial support**

Tiene su propio query language.

#### Gossip Protocol

Los nodos periódicamente intercambian información de su estado, corre cada segundo y cada nodo habla con hasta 3 otros nodos. Se puede ver quién tiene la última versión de la información.

#### Modelo de Datos

Es recomendable que el modelo de datos se haga a partir del query, cada consulta tiene su tabla asociada.

#### Primary Keys

Las **Primary Keys** tienen 2 partes (ambas pueden ser compuestas):
- *Partition Key* --> Define quién es el nodo dueño de la partición y quienes serán las réplicas, sirve para ver por igualdad
- *Clustering Column* --> No son necesarias, es un segundo nivel de direccionamiento que sirve para ver por rangos

#### Filtering

Cassandra no tiene soporte de JOIN, LIKE, Subqueries y Aggregations, los campos del WHERE son solo de Primary Keys (a menos que se especifique ALLOW FILTERING). Esto se da por como se indexa la información en el file system de cada nodo (según la estructura de la Primary Key), y genera que sea "1 tabla por query".

## Document Oriented Store

Es un Key-Value store donde el valor es un *documento* que no tiene esquema, y que a diferencia de key-value store, se pueden filtrar los documentos. Sirven para información semi-estructurada.

Estos documentos son unidades independientes y complejas, no hace falta convertir modelos en tablas.

### MongoDB

**MongoDB** es una base de datos open source que provee alta performance, alta disponibilidad y escalamiento automático.

Features claves son:
- **High Performance**
- **Rich Query Language** --> Soporta operaciones CRUD, Query, Data Aggregation, Text Search y Geospatial Queries
- **High Availability** --> Las replica set proveen automatic failover y redundancia
- **Horizontal Scalability**
- **Multiple storage engines**

#### Modelo de Datos

Los **documentos** son pares *key-value* que se presentan como JSON (en realidad es BSON).

Las **colecciones** son conjuntos de *documentos*, y las **bases de datos** son conjuntos de *colecciones*.

## Graph Store

Guarda la información como un conjunto de nodos y aristas. 

Cada **nodo** es una entidad que se define con una clave única, aristas de entrada y salida, y las propiedades (key-value).

Cada **arista** tiene clave única, nodo origen y destino, y propiedades (key-value).

### Elastic

Es un engine altamente escalable y open source para búsquedas full-text y queries de agregación analítica. Permite guardar, buscar y analizar grandes cantidades de información en casi "real-time". Usa *Apache Lucene* como base para full-text.

Es una base distribuida, multitenant, con interfaz REST y que guarda documentos en JSON.
