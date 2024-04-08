#!/bin/bash
# Compile: javac -cp .:./commons-codec-1.3.jar:./jade.jar *.java
# java -cp .:./commons-codec-1.3.jar:jade.jar  jade.Boot -gui -agents Vendedor

# java -cp .:./commons-codec-1.3.jar:jade.jar jade.Boot -gui -agents "vendedor:Vendedor"
java -cp .:./commons-codec-1.3.jar:jade.jar jade.Boot -gui -agents "vendedor:Vendedor;comprador:Cliente;comprador2:Cliente;comprador3:Cliente;comprador4:Cliente"
