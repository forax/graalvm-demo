#!/bin/bash

echo using GRAAL_HOME $GRAAL_HOME

# classpath version
$GRAAL_HOME/bin/java \
  -XX:+UseJVMCICompiler \
  -classpath target/main/artifact/fr.umlv.graalvmdemo-1.4.jar:deps/org.objectweb.asm.jar:deps/org.objectweb.asm.commons.jar \
  fr.umlv.graalvmdemo.Main

 
