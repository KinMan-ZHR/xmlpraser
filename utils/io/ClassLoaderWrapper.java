package com.jiuaoedu.monitor.platform.utils.io;

import java.io.InputStream;
import java.net.URL;

/**
 * A class to wrap access to multiple class loaders making them work as one
 * 封装了5个类加载器,见getClassLoaders方法
 *
 * @author ZhangHaoRan or KinMan Zhang
 */
public class ClassLoaderWrapper {

    ClassLoader defaultClassLoader;
    ClassLoader systemClassLoader;

    public ClassLoaderWrapper() {
        this.systemClassLoader = ClassLoader.getSystemClassLoader();
//    this.defaultClassLoader = this.getClass().getClassLoader();
    }

    /**
     * Get a resource as a URL using the current class path
     *
     * @param resource - the resource to locate
     * @return the resource or null
     */
    public URL getResourceAsURL(String resource) {
        return getResourceAsURL(resource, getClassLoaders(null));
    }

    /**
     * Get a resource from the classpath, starting with a specific class loader
     *
     * @param resource    - the resource to find
     * @param classLoader - the first classloader to try
     * @return the stream or null
     */
    public URL getResourceAsURL(String resource, ClassLoader classLoader) {
        return getResourceAsURL(resource, getClassLoaders(classLoader));
    }

    /**
     * Get a resource from the classpath
     *
     * @param resource - the resource to find
     * @return the stream or null
     */
    public InputStream getResourceAsStream(String resource) {
        return getResourceAsStream(resource, getClassLoaders(null));
    }

    /**
     * Get a resource from the classpath, starting with a specific class loader
     *
     * @param resource    - the resource to find
     * @param classLoader - the first class loader to try
     * @return the stream or null
     */
    public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
        return getResourceAsStream(resource, getClassLoaders(classLoader));
    }

    /**
     * Find a class on the classpath (or die trying)
     *
     * @param name - the class to look for
     * @return - the class
     * @throws ClassNotFoundException Duh.
     */
    public Class<?> classForName(String name) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(null));
    }

    /**
     * Find a class on the classpath, starting with a specific classloader (or die trying)
     *
     * @param name        - the class to look for
     * @param classLoader - the first classloader to try
     * @return - the class
     * @throws ClassNotFoundException Duh.
     */
    public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(classLoader));
    }

    /*
     * Try to get a resource from a group of classloaders
     * 用5个类加载器一个个查找资源，只要其中任何一个找到，就返回
     *
     * @param resource    - the resource to get
     * @param classLoader - the classloaders to examine
     * @return the resource or null
     */
    InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
        for (ClassLoader cl : classLoader) {
            if (null != cl) {

                // try to find the resource as passed
                InputStream returnValue = cl.getResourceAsStream(resource);

                // now, some class loaders want this leading "/", so we'll add it and try again if we didn't find the resource
                if (null == returnValue) {
                    returnValue = cl.getResourceAsStream("/" + resource);
                }

                if (null != returnValue) {
                    return returnValue;
                }
            }
        }
        return null;
    }

    /**
     * Get a resource as a URL using the current class path
     * 用5个类加载器一个个查找资源，只要其中任何一个找到，就返回
     *
     * @param resource     - the resource to locate
     * @param classLoaders - the class loaders to examine
     * @return the resource or null
     */
    private URL getResourceAsURL(String resource, ClassLoader[] classLoaders) {
        for (ClassLoader cl : classLoaders) {
            if (cl != null) {
                URL url = cl.getResource(resource);
                if (url == null) {
                    url = cl.getResource("/" + resource);
                }
                if (url != null) {
                    return url;
                }
            }
        }
        return null;
    }

    /**
     * Attempt to load a class from a group of classloaders
     * 用5个类加载器一个个调用Class.forName(加载类)，只要其中任何一个加载成功，就返回
     *
     * @param name        - the class to load
     * @param classLoader - the group of classloaders to examine
     * @return the class
     * @throws ClassNotFoundException - Remember the wisdom of Judge Smails: Well, the world needs ditch diggers, too.
     */
    Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {

        for (ClassLoader cl : classLoader) {

            if (null != cl) {

                try {

                    return Class.forName(name, true, cl);

                } catch (ClassNotFoundException e) {
                    // we'll ignore this until all classloaders fail to locate the class
                }

            }

        }

        throw new ClassNotFoundException("Cannot find class: " + name);

    }

    //一共5个类加载器
    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{
                classLoader,
                defaultClassLoader,
                Thread.currentThread().getContextClassLoader(),
                getClass().getClassLoader(),
                systemClassLoader};
    }

}
