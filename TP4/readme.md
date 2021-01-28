# RMI

## Como Correr

Se necesitan 3 terminales estando dentro del path del proyecto.

En la terminal que corre el **rmi registry** correr:
```
CLASSPATH="./target/classes/"
export CLASSPATH
rmiregistry -J-Djava.rmi.server.logCalls=true 1099
```

En una terminal con el **servicio** en si correr:
```
java -cp ./target/TP4-1.0-SNAPSHOT.jar "server_1.Server"
```

En una terminal con el **cliente** en si correr:
```
java -cp ./target/TP4-1.0-SNAPSHOT.jar "client_1.Client"
```

## Cambios

Si se agregan clases hay que volver a correr el registry y el service
