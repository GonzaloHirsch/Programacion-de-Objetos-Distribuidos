#!/bin/bash
#sets the classpath and starts the registry on port 1099

CLASSPATH="."
for dep in `ls lib/jars/*.jar`
do
	CLASSPATH="$CLASSPATH:$dep"
done

export CLASSPATH

rmiregistry -J-Djava.rmi.server.logCalls=true  $*