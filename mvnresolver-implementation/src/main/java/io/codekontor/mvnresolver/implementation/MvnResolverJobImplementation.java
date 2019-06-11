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
/**
 *
 */
package io.codekontor.mvnresolver.implementation;

import io.codekontor.mvnresolver.api.IMvnResolverService.IMvnResolverJob;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
class MvnResolverJobImplementation implements IMvnResolverJob {

  /** - */
  private MvnResolverServiceImplementation _service;

  /** - */
  private List<String>                     _coords;

  /** - */
  private List<String>                     _exclusionPatterns;

  /** - */
  private List<String>                     _inclusionPatterns;

  /**
   * <p>
   * Creates a new instance of type {@link MvnResolverJobImplementation}.
   * </p>
   *
   * @param service
   */
  public MvnResolverJobImplementation(MvnResolverServiceImplementation service) {
    this._service = checkNotNull(service);
    this._coords = new ArrayList<>();
    this._exclusionPatterns = new ArrayList<>();
    this._inclusionPatterns = new ArrayList<>();
  }

  @Override
  public IMvnResolverJob withDependency(String coordinate) {
    this._coords.add(checkNotNull(coordinate));
    return this;
  }

  @Override
  public IMvnResolverJob withDependencies(String... coords) {
    for (String coord : checkNotNull(coords)) {
      this._coords.add(coord);
    }
    return this;
  }

  @Override
  public IMvnResolverJob withExclusionPattern(String pattern) {
    this._exclusionPatterns.add(checkNotNull(pattern));
    return this;
  }

  @Override
  public IMvnResolverJob withExclusionPatterns(String... patterns) {
    for (String pattern : checkNotNull(patterns)) {
      this._exclusionPatterns.add(pattern);
    }
    return this;
  }

  @Override
  public IMvnResolverJob withInclusionPattern(String pattern) {
    this._inclusionPatterns.add(checkNotNull(pattern));
    return this;
  }

  @Override
  public IMvnResolverJob withInclusionPattern(String... patterns) {
    for (String pattern : checkNotNull(patterns)) {
      this._inclusionPatterns.add(pattern);
    }
    return this;
  }

  @Override
  public File[] resolve() {
    return this._service.resolve(this);
  }

  @Override
  public URL[] resolveToUrlArray() {

    File[] files = resolve();

    //
    List<URL> urls = new ArrayList<>(files.length);
    for (File file : files) {
      // if (file.getName().contains("io.codekontor.slizaa.core.spi-api")) {
      // continue;
      // }
      try {
        urls.add(file.toURI().toURL());
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    //
    return urls.toArray(new URL[0]);
  }

  List<String> getCoords() {
    return this._coords;
  }

  List<String> getExclusionPatterns() {
    return this._exclusionPatterns;
  }

  List<String> getInclusionPatterns() {
    return this._inclusionPatterns;
  }

}
