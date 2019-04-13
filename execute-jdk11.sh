#!/bin/bash

echo using jdk $JAVA_HOME

# classpath version
# /usr/jdk/jdk-13/bin/java \
#   --class-path target/main/artifact/fr.umlv.graalvmdemo-1.3.jar:deps/org.objectweb.asm.jar:deps/org.objectweb.asm.commons.jar \
#   fr.umlv.graalvmdemo.Main

# module-path version
$JAVA_HOME/bin/java \
  --module-path target/main/artifact:deps \
  --module fr.umlv.graalvmdemo
 