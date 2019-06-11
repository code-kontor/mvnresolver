/**
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.codekontor.mvnresolver.uber;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;

public class UberServiceInvocationHandler<T> implements InvocationHandler {

    /**
     * -
     */
    private Class<?> _proxyType;

    /**
     * -
     */
    private T _service;

    /**
     * -
     */
    private InstanceCreator<T> _instanceCrator;

    //
    private static ClassLoader _instance;

    /**
     * <p>
     * Creates a new instance of type {@link UberServiceInvocationHandler}.
     * </p>
     *
     * @param instanceCrator
     */
    public UberServiceInvocationHandler(Class<?> proxyType, InstanceCreator<T> instanceCrator) {
        _proxyType = checkNotNull(proxyType, "Parameter proxyType must not be null.");
        _instanceCrator = checkNotNull(instanceCrator, "Parameter instanceCrator must not be null.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T createNewResolverService(Class<T> proxyType, InstanceCreator<T> instanceCreator) {

        try {

            //
            UberServiceInvocationHandler<T> invocationHandler = new UberServiceInvocationHandler<>(proxyType,
                    instanceCreator);
            invocationHandler.service();

            //
            return (T) Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[]{proxyType}, invocationHandler);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * <p>
     * </p>
     *
     * @param reference
     * @param errorMessage
     * @return
     */
    private static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * <p>
     * </p>
     *
     * @param expression
     * @param errorMessage
     */
    private static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    private static boolean includeSlf4j() {

        //
        try {
            UberServiceInvocationHandler.class.getClassLoader().loadClass("org.slf4j.impl.StaticLoggerBinder");
            return false;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        checkState(service() != null, "Service is null!");
        return method.invoke(service(), args);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    private final T service() {

        //
        intitialize();

        //
        return _service;
    }

    /**
     * <p>
     * </p>
     */
    private void intitialize() {

        //
        if (_service != null) {
            return;
        }

        try {

            _service = _instanceCrator.apply(dynamicClassLoader(_proxyType));

        } catch (Exception e) {
            e.printStackTrace();
            new RuntimeException(e);
        }

    }

    /**
     * @return
     * @throws IOException
     */
    private static ClassLoader dynamicClassLoader(Class<?> proxyType) throws IOException {

        //
        if (_instance == null) {

            //
            List<URL> urlList = new ArrayList<>();

            //
            URL codeSource = proxyType.getProtectionDomain().getCodeSource().getLocation();
            InputStream inputStream = codeSource.openStream();

            ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry zipEntry = null;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                //
                if (!zipEntry.isDirectory() && zipEntry.getName().startsWith("libs/")) {

                    // Create a temporary file
                    Path path = Files.createTempFile(null, ".jar");

                    // Delete the file on exit
                    path.toFile().deleteOnExit();

                    // Copy the content of my jar into the temporary file
                    Files.copy(zipInputStream, path, StandardCopyOption.REPLACE_EXISTING);

                    // add the url to the url list
                    urlList.add(path.toFile().toURI().toURL());
                }
            }

            // create the url class loader
            FilteringClassLoader filteringClassLoader = new FilteringClassLoader(proxyType.getClassLoader(), MvnResolverServiceFactoryFactory.ALLOWED_CLASSES);
            _instance = new URLClassLoader(urlList.toArray(new URL[0]), filteringClassLoader);
        }

        //
        return _instance;
    }

    /**
     * @param <T>
     */
    @FunctionalInterface
    public interface InstanceCreator<T> {

        /**
         * Applies this function to the given argument.
         *
         * @return the function result
         */
        T apply(ClassLoader classLoader) throws Exception;
    }
}
