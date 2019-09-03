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
import java.net.URL;

/**
 * <p>
 * The {@link IMvnResolverService} allows you to resolve arbitrary maven artifacts (with its transitive dependencies).
 * If a requested dependency does not exists in the local repository, the service automatically downloads the required
 * artifact and stores it in the local maven repository.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IMvnResolverService {

    /**
     * <p>
     * Resolves the specified maven coordinate. The coordinate havs to be in the following
     * format: {@code groupId:artifactId[:packaging][:classifier]:version}
     * </p>
     *
     * @param coordinate the coordinate to resolve
     * @return an instance of type IMvnCoordinate
     */
    IMvnCoordinate parseCoordinate(String coordinate);

    /**
     * <p>
     * Resolves the maven artifacts with the specified coordinates. The artifact coordinates have to be in the following
     * format: {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </p>
     *
     * @param transitive  whether or not the dependencies should be resolved transitively
     * @param coordinates The artifact coordinates in the format
     *                    {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     * @return the resolved files in the local file system
     */
    File[] resolve(boolean transitive, String... coordinates);

    /**
     * <p>
     * Resolves the maven artifacts with the specified coordinates. The artifact coordinates have to be in the following
     * format: {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </p>
     *
     * @param coordinates The artifact coordinates in the format
     *                    {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     * @return the resolved files in the local file system
     */
    File[] resolve(String... coordinates);

    /**
     * <p>
     * Resolves the maven artifact with the specified coordinate. The artifact coordinate has to be in the following
     * format: {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </p>
     *
     * @param coordinate coordinates in the format
     *                   {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     * @return the resolved file in the local file system
     */
    File resolveArtifact(String coordinate);

    /**
     * <p>
     * Create a new {@link IMvnResolverJob}. Using a {@link IMvnResolverJob} instead of calling
     * {@link IMvnResolverService#resolve(boolean, String...)} allows you to specify more fine-grained request (e.g. with support
     * for exclusion and inclusion patterns.
     * </p>
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * File[] files = mvnResolverService.newMvnResolverJob()
     *    .withDependencies("net.bytebuddy:byte-buddy:jar:1.8.5", "org.mockito:mockito-core:jar:2.18.3")
     *    .withExclusionPattern("*:byte-buddy-*").resolve();
     * </code>
     * </pre>
     * </p>
     *
     * @return a new {@link IMvnResolverJob}.
     */
    IMvnResolverJob newMvnResolverJob();

    /**
     * <p>
     * </p>
     *
     * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
     */
    interface IMvnResolverJob {

        /**
         * <p>
         * </p>
         *
         * @param coord
         * @return
         */
        IMvnResolverJob withDependency(String coord);

        /**
         * <p>
         * </p>
         *
         * @param coords
         * @return
         */
        IMvnResolverJob withDependencies(String... coords);

        /**
         * <p>
         * </p>
         *
         * @param pattern
         * @return
         */
        IMvnResolverJob withExclusionPattern(String pattern);

        /**
         * <p>
         * </p>
         *
         * @param patterns
         * @return
         */
        IMvnResolverJob withExclusionPatterns(String... patterns);

        /**
         * <p>
         * Adds a simple filter to include artifacts. The artifact pattern syntax is of the form:
         *
         * <pre>
         * [groupId]:[artifactId]:[extension]:[version]
         * </pre>
         * <p>
         * Where each pattern segment is optional and supports full and partial <code>*</code> wildcards. An empty pattern
         * segment is treated as an implicit wildcard. Version can be a range in case a {@link VersionScheme} is specified.
         * </p>
         * <p>
         * For example, <code>org.eclipse.*</code> would match all artifacts whose group id started with
         * <code>org.eclipse.</code> , and <code>:::*-SNAPSHOT</code> would match all snapshot artifacts.
         * </p>
         *
         * @param pattern
         * @return
         */
        IMvnResolverJob withInclusionPattern(String pattern);

        /**
         * <p>
         * Adds a simple filter to include artifacts from a list of patterns. The artifact pattern syntax is of the form:
         *
         * <pre>
         * [groupId]:[artifactId]:[extension]:[version]
         * </pre>
         * <p>
         * Where each pattern segment is optional and supports full and partial <code>*</code> wildcards. An empty pattern
         * segment is treated as an implicit wildcard. Version can be a range in case a {@link VersionScheme} is specified.
         * </p>
         * <p>
         * For example, <code>org.eclipse.*</code> would match all artifacts whose group id started with
         * <code>org.eclipse.</code> , and <code>:::*-SNAPSHOT</code> would match all snapshot artifacts.
         * </p>
         *
         * @param patterns
         * @return
         */
        IMvnResolverJob withInclusionPattern(String... patterns);

        /**
         * <p>
         * Resolves the specified artifacts and returns the resulting files.
         * </p>
         *
         * @return
         */
        File[] resolve();

        /**
         * <p>
         * </p>
         *
         * @return
         */
        URL[] resolveToUrlArray();
    }

}
