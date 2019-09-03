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
