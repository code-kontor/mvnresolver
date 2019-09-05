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

import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.mvnresolver.api.IMvnResolverService.IMvnResolverJob;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

class MvnResolverJobImplementation implements IMvnResolverJob {

  private MvnResolverServiceImplementation _service;

  private List<MvnResolverJobDependency>                     _dependencies;

  public MvnResolverJobImplementation(MvnResolverServiceImplementation service) {
    this._service = checkNotNull(service);
    this._dependencies = new ArrayList<>();
  }

  @Override
  public IMvnResolverService.IMvnResolverJobDependency withDependency(String coordinate) {
    return new MvnResolverJobDependency(this, coordinate);
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

  List<MvnResolverJobDependency> dependencies() {
    return _dependencies;
  }
}
