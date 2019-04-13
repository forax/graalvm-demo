#!/bin/bash
# export JAVA_HOME=/usr/jdk/jdk-11/

echo JAVA_HOME $JAVA_HOME

rm -fr ./jlink-image
$JAVA_HOME/bin/jlink \
  --module-path target/main/artifact:deps \
  --add-modules fr.umlv.graalvmdemo \
  --launcher fr.umlv.graalvmdemo=fr.umlv.graalvmdemo \
  --no-header-files \
  --no-man-pages \
  --strip-debug \
  --output jlink-image
