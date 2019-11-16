import static com.github.forax.pro.Pro.*;
import static com.github.forax.pro.builder.Builders.*;

resolver.
  dependencies(
    // ASM
    "org.objectweb.asm:7.1",
    "org.objectweb.asm.commons:7.1",
    
    "org.objectweb.asm.tree:7.1",
    "org.objectweb.asm.tree.analysis:7.1",
    
    // JMH
    "org.openjdk.jmh=org.openjdk.jmh:jmh-core:1.21",
    "org.apache.commons.math3=org.apache.commons:commons-math3:3.3.2",
    "net.sf.jopt-simple=net.sf.jopt-simple:jopt-simple:4.6",
    "org.openjdk.jmh.generator=org.openjdk.jmh:jmh-generator-annprocess:1.21"
    )

compiler.
  sourceRelease(8)

packager.
  modules("fr.umlv.graalvmdemo@1.4/fr.umlv.graalvmdemo.Main")   

run(resolver, modulefixer, compiler, packager)
    
/exit

