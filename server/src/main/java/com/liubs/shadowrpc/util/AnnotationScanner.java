package com.liubs.shadowrpc.util;



import com.liubs.shadowrpc.protocol.annotation.ShadowServiceHolder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Liubsyy
 * @date 2023/12/12 21:54
 */
public class AnnotationScanner {


    public static <T extends Annotation>  List<ShadowServiceHolder<T>> scanAnnotations(String packageName, Class<T> annotation) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        return scanAnnotations(classLoader,packageName,annotation);
    }

    private static <T extends Annotation> List<ShadowServiceHolder<T>> scanAnnotations(ClassLoader classLoader, String packageName, Class<T> annotation) throws IOException {
        String packagePath = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(packagePath);

        List<ShadowServiceHolder<T>> allResults = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("jar")) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                scanFromJar(classLoader,jarPath,packagePath,annotation, allResults );
            } else {
                scanFromDir(classLoader,new File(resource.getFile()),packageName,annotation,allResults);
            }
        }

        return allResults;
    }

    private static <T extends Annotation> void scanFromJar(ClassLoader classLoader,String jarPath,String packagePath , Class<? extends Annotation> annoCls,List<ShadowServiceHolder<T>> allResults) throws IOException {
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
                    T shadowServiceAnno = (T)clazz.getAnnotation(annoCls);
                    if (shadowServiceAnno != null) {
                        allResults.add(new ShadowServiceHolder<>(shadowServiceAnno, clazz));
                    }
                }
            }
        }
        jar.close();
    }

    private static <T extends Annotation> void scanFromDir(ClassLoader classLoader,File directory, String packageName,Class<? extends Annotation> annoCls,List<ShadowServiceHolder<T>> allResults) {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanFromDir(classLoader,file, packageName + "." + file.getName(),annoCls,allResults);
                } else if (file.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(packageName + '.' +
                                file.getName().substring(0, file.getName().length() - 6));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(null != clazz) {
                        T shadowServiceAnno = (T)clazz.getAnnotation(annoCls);
                        if (shadowServiceAnno != null) {
                            allResults.add(new ShadowServiceHolder<>(shadowServiceAnno,clazz));
                        }
                    }
                }
            }
        }
    }

}
