#!/bin/bash

echo GRAAL_HOME $GRAAL_HOME

$GRAAL_HOME/bin/native-image \
  --no-server \
  -cp target/main/artifact/fr.umlv.graalvmdemo-1.4.jar:deps/org.objectweb.asm.jar:deps/org.objectweb.asm.commons.jar:deps/org.objectweb.asm.tree.jar:deps/org.objectweb.asm.tree.analysis.jar fr.umlv.graalvmdemo.Main
