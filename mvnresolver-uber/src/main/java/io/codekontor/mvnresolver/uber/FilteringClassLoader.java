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

/**
 *
 */
public class FilteringClassLoader extends ClassLoader {

    //
    private static final ClassLoader EXTENSION_CLASS_LOADER;

    //
    private String[] _regexps;

    static {
        EXTENSION_CLASS_LOADER = ClassLoader.getSystemClassLoader().getParent();

        try {
            ClassLoader.registerAsParallelCapable();
        } catch (NoSuchMethodError ignore) {
            // Not supported on Java 6
        }
    }

    /**
     *
     */
    private static class RetrieveSystemPackagesClassLoader extends ClassLoader {

        RetrieveSystemPackagesClassLoader(ClassLoader parent) {
            super(parent);
        }

        protected Package[] getPackages() {
            return super.getPackages();
        }
    }

    public FilteringClassLoader(ClassLoader parent, String... regexps) {
        super(parent);

        _regexps = regexps;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        //
        try {
            return EXTENSION_CLASS_LOADER.loadClass(name);
        } catch (ClassNotFoundException ignore) {
            // ignore
        }

        //
        if (!isClassAllowed(name)) {
            throw new ClassNotFoundException(name + " not found.");
        }

        //
        Class<?> cl = super.loadClass(name, false);
        if (resolve) {
            resolveClass(cl);
        }

        //
        return cl;
    }

    @Override
    public String toString() {
        return FilteringClassLoader.class.getSimpleName() + "(" + getParent() + ")";
    }

    /**
     * @param className
     * @return
     */
    private boolean isClassAllowed(String className) {

        //
        for (String regexp : _regexps) {
            if (className.matches(regexp)) {
                return true;
            }
        }

        //
        return false;
    }
}
