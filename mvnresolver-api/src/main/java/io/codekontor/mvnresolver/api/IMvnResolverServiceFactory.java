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
package io.codekontor.mvnresolver.api;

import java.io.File;

/**
 * <p>
 * Factory to configure and create instances of type {@link IMvnResolverService}.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IMvnResolverServiceFactory {

    /**
     * <p>
     * Creates a new {@link MvnResolverServiceFactoryBuilder} to create a {@link IMvnResolverService}.
     * </p>
     *
     * @return a new {@link MvnResolverServiceFactoryBuilder}
     */
    MvnResolverServiceFactoryBuilder newMvnResolverService();

    /**
     * <p>
     * Builder interface to configure and create an instance of type {@link IMvnResolverService}.
     * </p>
     *
     * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
     */
    interface MvnResolverServiceFactoryBuilder {

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} with the specified remote repository.
         * </p>
         *
         * @param id the id of the specified repository
         * @param url the url of the specified repository
         * @param layout the layout of the specified repository
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url, String layout);

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} with the specified remote repository
         * using the default repository layout.
         * </p>
         *
         * @param id the id of the specified repository
         * @param url the url of the specified repository
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url);

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} with the maven central repository.
         * </p>
         *
         * @param withMavenCentralRepo whether or not this {@link MvnResolverServiceFactoryBuilder} is configured
         *                             with the maven central repository.
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withMavenCentralRepo(boolean withMavenCentralRepo);

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} to work offline.
         * </p>
         *
         * @param offline
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withOffline(boolean offline);

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} to work offline.
         * </p>
         *
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withOffline();

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} from the specified settings file.
         * </p>
         *
         * @param file the settings file to use
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withSettingsFile(File file) throws IllegalArgumentException, InvalidSettingsFileException;

        /**
         * <p>
         * Configures the {@link MvnResolverServiceFactoryBuilder} from the specified settings file.
         * </p>
         *
         * @param filePath the path of the settings file to use
         * @return the current MvnResolverServiceFactoryBuilder
         */
        MvnResolverServiceFactoryBuilder withSettingsFile(String filePath) throws IllegalArgumentException, InvalidSettingsFileException;

        /**
         * <p>
         * Creates a new instance of type {@link IMvnResolverService} with the configuration taken from this {@link MvnResolverServiceFactoryBuilder}.
         * </p>
         *
         * @return a new instance of type {@link IMvnResolverService} with the configuration taken from this {@link MvnResolverServiceFactoryBuilder}.
         */
        IMvnResolverService create();
    }
}
