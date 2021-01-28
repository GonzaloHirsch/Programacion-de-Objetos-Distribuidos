# How to Set Up

1 - Have the hazelcast jar inside a *hazel* directory

2 - Export the classpath, while being in the root directory folder of the project
```
export CLASSPATH=$HOME/Repository/Programacion-de-Objetos-Distribuidos/TP6/hazel/hazelcast-all-3.8.6.jar
```

3 - Run the App
```
java com.hazelcast.console.ConsoleApp
```

# How to configure

1 - Download de XML configuration from campus and put it inside the *hazel* directory:
```
wget https://YOUR_USER:YOUR_PASS@campus.itba.edu.ar/bbcswebdav/pid-262839-dt-content-rid-3267260_1/courses/72.42-28913/hazelcast.xml
```
**NOTE:** Change the username and password for your login information

2 - Change to TCP discovery in the XML, and disable multicast. It should look like:
```
<multicast enabled="false">
    <multicast-group>224.2.2.3</multicast-group>
    <multicast-port>54327</multicast-port>
</multicast>
<tcp-ip enabled="true">
    <interface>192.168.1.192</interface>
    <member-list>
        <member>192.168.1.192</member>
    </member-list>
</tcp-ip>
```

3 - Change IP to local IP address in the TCP-IP configurations and the enabled interfaces:
```
<tcp-ip enabled="true">
    <interface>192.168.1.192</interface>
    <member-list>
        <member>192.168.1.192</member>
    </member-list>
</tcp-ip>
.
.
.
<interfaces enabled="true">
    <interface>192.168.1.192</interface>
</interfaces>
```
Local IP address can be found with:
```
ifconfig | grep 19
```

4 - Export the classpath:
```
export CLASSPATH=$HOME/Repository/Programacion-de-Objetos-Distribuidos/TP6/hazel/hazelcast-all-3.8.6.jar
```

5 - Run Hazelcast:
```
java -Dhazelcast.config=-Dhazelcast.config=/Users/gonzalo/Repository/Programacion-de-Objetos-Distribuidos/TP6/hazel/hazelcast.xml com.hazelcast.console.ConsoleApp
```

**NOTE:** Make sure to be running Hazelcast where the XML file is

# How to create the project

1 - Run the mvn archetype generation, and choose the POD archetype:
```
mvn archetype:generate -DarchetypeCatalog=local

or

mvn archetype:generate
```

The *hazel* folder can be placed inside the project or kept in a sepparate directory

2 - Add the dependency to the Server dependencies:
```
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-all</artifactId>
</dependency>
```

3 - Add the flag to the running options
```
-Dhazelcast.config=/Users/gonzalo/Repository/Programacion-de-Objetos-Distribuidos/TP6/hazel/hazelcast.xml
```