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
package io.codekontor.mvnresolver.implementation;

import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.mvnresolver.api.IMvnResolverServiceFactory;
import org.jboss.shrinkwrap.resolver.api.InvalidConfigurationFileException;
import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepositories;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepository;

import java.io.File;

public class MvnResolverServiceFactoryImplementation implements IMvnResolverServiceFactory {

    @Override
    public MvnResolverServiceFactoryBuilder newMvnResolverService() {

        ClassLoader current = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            return new MvnResolverServiceFactoryBuilderImplementation();
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    /**
     * <p>
     * </p>
     *
     * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
     */
    public static class MvnResolverServiceFactoryBuilderImplementation implements MvnResolverServiceFactoryBuilder {

        /** - */
        private ConfigurableMavenResolverSystem _resolverSystem;

        /**
         * <p>
         * Creates a new instance of type
         * {@link MvnResolverServiceFactoryBuilderImplementation}.
         * </p>
         */
        public MvnResolverServiceFactoryBuilderImplementation() {
            _resolverSystem = Resolvers.use(ConfigurableMavenResolverSystem.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url, String layout) {
            MavenRemoteRepository mavenRemoteRepository = MavenRemoteRepositories.createRemoteRepository(id, url, layout);
            _resolverSystem.withRemoteRepo(mavenRemoteRepository);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url) {
            return withRemoteRepository(id, url, "default");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withMavenCentralRepo(boolean withMavenCentralRepo) {
            _resolverSystem.withMavenCentralRepo(withMavenCentralRepo);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withOffline(boolean b) {
            _resolverSystem.workOffline(b);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withOffline() {
            _resolverSystem.workOffline();
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withSettingsFile(File file) throws IllegalArgumentException, InvalidConfigurationFileException {
            _resolverSystem.fromFile(file);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withSettingsFile(String s) throws IllegalArgumentException, InvalidConfigurationFileException {
            _resolverSystem.fromFile(s);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IMvnResolverService create() {
            MvnResolverServiceImplementation serviceImplementation = new MvnResolverServiceImplementation();
            serviceImplementation.initialize(_resolverSystem);
            return serviceImplementation;
        }
    }
}
