package com.liubs.shadowrpc.base.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Liubsyy
 * @date 2023/12/24 1:18 AM
 **/
public class ClassScanWalker {
    public static void scanPackage(String packageName, Consumer<Class<?>> consumer) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        scanPackage(classLoader,packageName,consumer);
    }

    private static void scanPackage(ClassLoader classLoader, String packageName, Consumer<Class<?>> consumer) throws IOException {
        String packagePath = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(packagePath);


        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("jar")) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                scanFromJar(classLoader,jarPath,packagePath,consumer );
            } else {
                scanFromDir(classLoader,new File(resource.getFile()),packageName,consumer);
            }
        }

    }

    private static <T> void scanFromJar(ClassLoader classLoader,String jarPath,String packagePath ,  Consumer<Class<?>> consumer) throws IOException {
        JarFile jar = new JarFile(jarPath);

        // 遍历JAR文件中的条目
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            // 检查条目是否为指定包路径下的类
            if (name.startsWith(packagePath) && name.endsWith(".class")) {
                String className = name.replace('/', '.').substring(0, name.length() - 6);

                Class<?> clazz = null;
                try {
                    clazz = classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(null != clazz) {
                    consumer.accept(clazz);
                }
            }
        }
        jar.close();
    }

    private static void scanFromDir(ClassLoader classLoader,File directory, String packageName, Consumer<Class<?>> consumer) {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanFromDir(classLoader,file, packageName + "." + file.getName(),consumer);
                } else if (file.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(packageName + '.' +
                                file.getName().substring(0, file.getName().length() - 6));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(null != clazz) {
                        consumer.accept(clazz);
                    }
                }
            }
        }
    }
}
