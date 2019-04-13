# graalvm-demo
A demo of all graal projects

## How to build the test application

First you need to get pro, the simplest solution is to use the pro_wrapper,
so grab a version of jdk 11 or 12 and type
```
  /path/to/jdk/bin/java pro_wrapper.java
```

If you want to re-run the build, you can either re-run pro_wrapper or call directly pro like this
```
  ./pro/bin/pro
```

This will compile the source code for Java 8 with the module-info for Java 9, so the generated jar
can be used either in the classpath or in the module-path


## get GraalVM

To test GraalVM, you need to first download the Community Edition from Github 
```
  https://github.com/oracle/graal/releases
```

and them set the GRAAL_HOME env variable to the folder containing GraalVM.
```
  export GRAAL_HOME=/path/to/graalvm
```

you can test that GraalVM distrib is correctly installed by typing
```
  $GRAAL_HOME/bin/java -version
```

As the time of the writing, the GraalVM distrib acts as a JDK version 8 patched to use Graal as JIT compiler instead
of the JITs c1 and c2 that comes with the OpenJDK (or any distributation based on the OpenJDK like the Oracle JDK, IBM JDK, Amazon JDK, etc).

 
## execution modes

So now we can run our application with 4 configurations
- Hotspot with c1/c2 as JIT compilers
- Hostspot with Graal as JIT compiler
- A custom JRE generated using jlink with Hotspot
- A native image generated using Graal AOT with SubstrateVM


# execute Hotspot with c1/c2

```
  $JAVA_HOME/bin/java \
    --module-path target/main/artifact:deps \
    --module fr.umlv.graalvmdemo
```

# execute Hotspot with Graal

```
  $GRAAL_HOME/bin/java \
    -XX:+UseJVMCICompiler \
    -classpath target/main/artifact/fr.umlv.graalvmdemo-1.3.jar:deps/org.objectweb.asm.jar:deps/org.objectweb.asm.commons.jar \
    fr.umlv.graalvmdemo.Main
```

# generate and execute custom JRE using jlink

```
  $JAVA_HOME/bin/jlink \
    --module-path target/main/artifact:deps \
    --add-modules fr.umlv.graalvmdemo \
    --launcher fr.umlv.graalvmdemo=fr.umlv.graalvmdemo \
    --no-header-files \
    --no-man-pages \
    --strip-debug \
    --output jlink-image
```

```
  ./jlink-image/bin/fr.umlv.graalvmdemo
```

# generate and execute a native image using Graal AOT running with SubstrateVM

```
  $GRAAL_HOME/bin/native-image \
    -cp target/main/artifact/fr.umlv.graalvmdemo-1.3.jar:deps/org.objectweb.asm.jar:deps/org.objectweb.asm.commons.jar:deps/org.objectweb.asm.tree.jar:deps/org.objectweb.asm.tree.analysis.jar \
    fr.umlv.graalvmdemo.Main
```

```
  ./fr.umlv.graalvmdemo.main
```
