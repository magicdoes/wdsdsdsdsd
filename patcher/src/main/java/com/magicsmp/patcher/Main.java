package com.magicsmp.patcher;

import org.objectweb.asm.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;

public final class Main {
  public static void main(String[] args) throws Exception {
    if (args.length != 3) throw new IllegalArgumentException("baseline guard-classes output");
    Path baseline=Path.of(args[0]), guard=Path.of(args[1]), output=Path.of(args[2]);
    Map<String,byte[]> entries=new LinkedHashMap<>();
    try(JarInputStream in=new JarInputStream(Files.newInputStream(baseline))){ JarEntry e; while((e=in.getNextJarEntry())!=null){ if(!e.isDirectory()) entries.put(e.getName(),in.readAllBytes()); } }
    String main="com/bx/magicSmp/MagicSmp.class";
    entries.put(main, patchMain(entries.get(main)));
    Path cls=guard.resolve("com/bx/magicSmp/patch/SellGuiProtectionListener.class");
    entries.put("com/bx/magicSmp/patch/SellGuiProtectionListener.class", Files.readAllBytes(cls));
    Files.createDirectories(output.getParent());
    try(JarOutputStream out=new JarOutputStream(Files.newOutputStream(output))){ for(var x:entries.entrySet()){ JarEntry e=new JarEntry(x.getKey()); out.putNextEntry(e); out.write(x.getValue()); out.closeEntry(); } }
    System.out.println("Built "+output);
  }
  private static byte[] patchMain(byte[] original){
    ClassReader cr=new ClassReader(original); ClassWriter cw=new ClassWriter(cr,ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
    cr.accept(new ClassVisitor(Opcodes.ASM9,cw){
      @Override public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){
        MethodVisitor mv=super.visitMethod(a,n,d,s,ex);
        if(!n.equals("registerListeners")||!d.equals("()V")) return mv;
        return new MethodVisitor(Opcodes.ASM9,mv){
          @Override public void visitInsn(int op){
            if(op==Opcodes.RETURN){
              super.visitMethodInsn(Opcodes.INVOKESTATIC,"org/bukkit/Bukkit","getPluginManager","()Lorg/bukkit/plugin/PluginManager;",false);
              super.visitTypeInsn(Opcodes.NEW,"com/bx/magicSmp/patch/SellGuiProtectionListener");
              super.visitInsn(Opcodes.DUP);
              super.visitMethodInsn(Opcodes.INVOKESPECIAL,"com/bx/magicSmp/patch/SellGuiProtectionListener","<init>","()V",false);
              super.visitVarInsn(Opcodes.ALOAD,0);
              super.visitMethodInsn(Opcodes.INVOKEINTERFACE,"org/bukkit/plugin/PluginManager","registerEvents","(Lorg/bukkit/event/Listener;Lorg/bukkit/plugin/Plugin;)V",true);
            }
            super.visitInsn(op);
          }
        }; }
    },0); return cw.toByteArray();
  }
}
