#!/bin/bash
rm *class
javac -cp .:./commons-codec-1.3.jar:./jade.jar *.java
