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
package io.codekontor.core.mvnresolver.itest;

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class MvnResolverServiceImplementationTest {

    private IMvnResolverService mvnResolverService;

    @Before
    public void setup() {
        mvnResolverService = MvnResolverServiceFactoryFactory
                .createNewResolverServiceFactory().newMvnResolverService().create();
    }

    @Test
    public void testResolve() {

        //
        File[] files = mvnResolverService.resolve("org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(files).hasSize(74);
    }

    @Test
    public void testResolveArtifact() {

        //
        File file = mvnResolverService.resolveArtifact("org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(file).isNotNull();
    }

}
