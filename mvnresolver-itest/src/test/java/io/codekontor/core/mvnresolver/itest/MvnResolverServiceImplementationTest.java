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
import io.codekontor.mvnresolver.api.IMvnResolverServiceFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class MvnResolverServiceImplementationTest {

    private static IMvnResolverServiceFactory mvnResolverServiceFactory;

    @BeforeClass
    public static void setup() {

        try {

            // creating the MvnResolverServiceFactory
            mvnResolverServiceFactory = MvnResolverServiceFactoryFactory
                    .createNewResolverServiceFactory();
            mvnResolverServiceFactory.newMvnResolverService();

        } catch (Exception e) {
            System.out.println("**************************");
            System.out.println("Don't run from within the IDE.");
            Assert.fail("as");
        }

    }

    @Test
    public void testResolve() {

        // creating a new IMvnResolverService
        IMvnResolverService mvnResolverService = mvnResolverServiceFactory.newMvnResolverService().withMavenCentralRepo(true).create();

        //
        File[] files = mvnResolverService.resolve("org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(files).hasSize(74);
    }

    @Test
    public void testResolveTransitive() {

        // creating a new IMvnResolverService
        IMvnResolverService mvnResolverService = mvnResolverServiceFactory.newMvnResolverService().withMavenCentralRepo(true).create();

        //
        File[] files = mvnResolverService.resolve(true, "org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(files).hasSize(74);
    }

    @Test
    public void testResolveNonTransitive() {

        // creating a new IMvnResolverService
        IMvnResolverService mvnResolverService = mvnResolverServiceFactory.newMvnResolverService().withMavenCentralRepo(true).create();

        //
        File[] files = mvnResolverService.resolve(false, "org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(files).hasSize(1);
    }

    @Test
    public void testResolveArtifact() {

        // creating a new IMvnResolverService
        IMvnResolverService mvnResolverService = mvnResolverServiceFactory.newMvnResolverService().withMavenCentralRepo(true).create();

        //
        File file = mvnResolverService.resolveArtifact("org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(file).isNotNull();
    }

    @Test
    public void testResolveJob() {

        // creating a new IMvnResolverService
        IMvnResolverService mvnResolverService = mvnResolverServiceFactory.newMvnResolverService().withMavenCentralRepo(true).create();

        //
        File[] files = mvnResolverService.newMvnResolverJob()
                .withDependency("org.neo4j.test:neo4j-harness:2.3.3")
                .resolve();

        //
        assertThat(files).isNotNull();
        assertThat(files).hasSize(74);

        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getAbsolutePath());
        }
    }
}
