#!/bin/bash
# export GRAAL_HOME=/usr/jdk/graalvm-ce-1.0.0-rc15/

echo GRAAL_HOME $GRAAL_HOME

$GRAAL_HOME/bin/native-image \
  -cp target/main/artifact/fr.umlv.graalvmdemo-1.3.jar:deps/org.objectweb.asm.jar:deps/org.objectweb.asm.commons.jar:deps/org.objectweb.asm.tree.jar:deps/org.objectweb.asm.tree.analysis.jar fr.umlv.graalvmdemo.Main
