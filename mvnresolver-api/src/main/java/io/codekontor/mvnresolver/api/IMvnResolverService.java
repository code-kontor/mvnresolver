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
     * Encapsulates a maven coordinate (see https://maven.apache.org/pom.html#Maven_Coordinates).
     * </p>
     */
    public interface IMvnCoordinate {

        /**
         * Returns the 'groupId' of this maven coordinate.
         *
         * @return The 'groupId' of this maven coordinate.
         */
        String getGroupId();

        /**
         * Returns the 'artifactId' of this maven coordinate.
         *
         * @return The 'artifactId' of this maven coordinate.
         */
        String getArtifactId();

        /**
         * Returns the 'packagingType' of this maven coordinate.
         *
         * @return The 'packagingType' of this maven coordinate.
         */
        String getPackagingType();

        /**
         * Returns the 'classifier' of this maven coordinate.
         *
         * @return The 'classifier' of this maven coordinate.
         */
        String getClassifier();

        /**
         * Returns the 'version' of this maven coordinate.
         *
         * @return The 'version' of this maven coordinate.
         */
        String getVersion();

        /**
         * Returns the string representation of this maven coordinate in it's canonical form.
         *
         * @return the string representation of this maven coordinate in it's canonical form.
         */
        String toCanonicalForm();
    }

    /**
     * represents the maven depenency scopes. Dependency scope is used to limit the transitivity of a dependency
     * (see https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Dependency_Scope).
     */
    enum Scope {
        COMPILE, PROVIDED, RUNTIME, TEST, SYSTEM, IMPORT
    }


    /**
     * <p>
     * The {@link IMvnResolverJob} interface allows the fine-grained specification of maven resolve jobs.
     * </p>
     *
     * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
     */
    interface IMvnResolverJob {

        /**
         * <p>
         * Adds a new {@link IMvnResolverJobDependency} to this job.
         * </p>
         *
         * @param coordinate
         * @return
         */
        IMvnResolverJobDependency withDependency(String coordinate);

        /**
         * <p>
         * Resolves the specified artifacts and returns the resulting files as an array.
         * </p>
         *
         * @return the resolved files
         */
        File[] resolve();

        /**
         * <p>
         * Resolves the specified artifacts and returns the resulting file URLs as an array.
         * </p>
         *
         * @return the resulting file URLs
         */
        URL[] resolveToUrlArray();
    }

    /**
     * <p>
     * Represents a maven dependency to resolve.
     * </p>
     */
    interface IMvnResolverJobDependency extends IMvnResolverJob {

        /**
         * <p>
         * Adds the specified pattern (in format <code>groupId:artifactId</code>) to the list of exclusion patterns.
         * </p>
         *
         * @param pattern the pattern to exclude in format <code>groupId:artifactId</code>. Must not be null.
         * @return this {@link IMvnResolverJobDependency}
         */
        IMvnResolverJobDependency withExclusionPattern(String pattern);

        /**
         * <p>
         * Adds the specified patterns (in format <code>groupId:artifactId</code>) to the list of exclusion patterns .
         * </p>
         *
         * @param patterns the patterns to exclude in format <code>groupId:artifactId</code>
         * @return this {@link IMvnResolverJobDependency}
         */
        IMvnResolverJobDependency withExclusionPatterns(String... patterns);

        /**
         * <p>
         * Specifies this dependency a optional (paramater isOptional == true) or mandatory
         * (parameter isOptional == false).
         * </p>
         *
         * @param isOptional whether or not this dependency is optional
         * @return this {@link IMvnResolverJobDependency}
         */
        IMvnResolverJobDependency withOptional(boolean isOptional);

        /**
         * <p>
         * Sets the specified scope for this dependency.
         * </p>
         *
         * @param scope the maven scope. Must not be null.
         * @return this {@link IMvnResolverJobDependency}
         */
        IMvnResolverJobDependency withScope(Scope scope);
    }
}
