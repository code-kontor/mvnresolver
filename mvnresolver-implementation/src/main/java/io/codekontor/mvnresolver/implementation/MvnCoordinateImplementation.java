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

import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;
import io.codekontor.mvnresolver.api.IMvnCoordinate;

import static com.google.common.base.Preconditions.checkNotNull;

public class MvnCoordinateImplementation implements IMvnCoordinate {

  /** - */
  private MavenCoordinate _mavenCoordinate;

  /**
   *
   * @param mavenCoordinate
   */
  public MvnCoordinateImplementation(MavenCoordinate mavenCoordinate) {
    this._mavenCoordinate = checkNotNull(mavenCoordinate);
  }

  /**
   *
   * @return
   */
  @Override
  public String getGroupId() {
    return _mavenCoordinate.getGroupId();
  }

  /**
   *
   * @return
   */
  @Override
  public String getArtifactId() {
    return _mavenCoordinate.getArtifactId();
  }

  /**
   *
   * @return
   */
  @Override
  public String getPackagingType() {
    return _mavenCoordinate.getPackaging().toString();
  }

  /**
   *
   * @return
   */
  @Override
  public String getClassifier() {
    return _mavenCoordinate.getClassifier();
  }

  /**
   *
   * @return
   */
  @Override
  public String getVersion() {
    return _mavenCoordinate.getVersion();
  }

  /**
   *
   * @return
   */
  @Override
  public String toCanonicalForm() {
    return _mavenCoordinate.toCanonicalForm();
  }
}
