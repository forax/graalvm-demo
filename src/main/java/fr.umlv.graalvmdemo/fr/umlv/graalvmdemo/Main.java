package fr.umlv.graalvmdemo;

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.CodeSizeEvaluator;

public class Main {
  enum Kind {
    INSTANCE_FIELD, STATIC_FIELD, INSTANCE_METHOD, STATIC_METHOD, CODE_SIZE
    ;
    
    String text() {
      return name().toLowerCase().replace('_', ' ');
    }
  }
  
  static class Statistics {
    private final EnumMap<Kind, Long> valueMap;
    
    public Statistics(EnumMap<Kind, Long> valueMap) {
      this.valueMap = valueMap;
    }
    
    public Statistics merge(Statistics stat) {
      EnumMap<Kind, Long> newMap = new EnumMap<>(Kind.class);
      newMap.putAll(valueMap);
      stat.valueMap.forEach((kind, value) -> {
        newMap.merge(kind, value, Long::sum);
      });
      return new Statistics(newMap);
    }
  }
  
  public static void main(String[] args) throws IOException {
    Path data = Paths.get("data/rt.jar");
    
    HashMap<String, Statistics> statMap = new HashMap<>();
    
    try (JarFile file = new JarFile(data.toFile())) {
      for (JarEntry entry : Collections.list(file.entries())) {
        if (!entry.getName().endsWith(".class")) {
          continue; // skip
        }
        InputStream input = file.getInputStream(entry);
        ClassReader reader = new ClassReader(input);
        reader.accept(new ClassVisitor(ASM7) {
          private EnumMap<Kind, Long> valueMap;
          
          private boolean isStatic(int access) {
            return (access & ACC_STATIC) != 0;
          }
          
          @Override
          public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            valueMap = new EnumMap<>(Kind.class);
          }
          
          @Override
          public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            valueMap.merge(isStatic(access)? Kind.STATIC_FIELD: Kind.INSTANCE_FIELD, 1L, Long::sum);
            return null;
          }
          
          @Override
          public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            valueMap.merge(isStatic(access)? Kind.STATIC_METHOD: Kind.INSTANCE_METHOD, 1L, Long::sum);
            return new CodeSizeEvaluator(ASM7, null) {
              @Override
              public void visitEnd() {
                super.visitEnd();
                valueMap.merge(Kind.CODE_SIZE, (long)getMaxSize(), Long::sum);
              }
            };
          }
          
          @Override
          public void visitEnd() {
            super.visitEnd();
            statMap.put(reader.getClassName(), new Statistics(new EnumMap<>(valueMap)));
          }
        }, 0);
      }
    }
    
    double classNumber = statMap.size();
    Statistics globalStat = statMap.values().stream().reduce(new Statistics(new EnumMap<>(Kind.class)), Statistics::merge);
   
    globalStat.valueMap.forEach((kind, value) -> {
      System.out.printf("average %s %f\n", kind.text(), value / classNumber);
    });
  }
}
