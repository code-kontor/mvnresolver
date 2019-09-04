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
package io.codekontor.mvnresolver;

import io.codekontor.mvnresolver.api.IMvnResolverServiceFactory;
import io.codekontor.mvnresolver.uber.UberServiceInvocationHandler;

/**
 *
 */
public class MvnResolverServiceFactoryFactory {

  // The classes that can be loaded via the parent class loader
  public static final String[] ALLOWED_CLASSES = {"io\\.codekontor\\.mvnresolver\\.api\\..*", "com\\.google\\.common\\..*"};

  /**
   *
   * @return
   */
  public static IMvnResolverServiceFactory createNewResolverServiceFactory() {

    return UberServiceInvocationHandler.createNewResolverService(IMvnResolverServiceFactory.class, (jcl) -> {

      // load the class...
      Class<?> clazz = jcl
          .loadClass("io.codekontor.mvnresolver.implementation.MvnResolverServiceFactoryImplementation");

      // ... and create a new instance.
      return (IMvnResolverServiceFactory) clazz.newInstance();
    });
  }

}
