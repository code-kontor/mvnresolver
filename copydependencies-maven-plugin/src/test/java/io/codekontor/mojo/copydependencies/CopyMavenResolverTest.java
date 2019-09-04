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
package io.codekontor.mojo.copydependencies;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;

/**
 */
public class CopyMavenResolverTest extends AbstractCopyDependencyTest {

  /**
   * <p>
   * Creates a new instance of type {@link CopyMavenResolverTest}.
   * </p>
   */
  public CopyMavenResolverTest() {
    super("copy-maven-resolver");
  }

  @Test
  public void testCopyDependencyMojo() throws Exception {

    //
    CopyDependenciesMojo mojo = (CopyDependenciesMojo) super.findCopyDependencyMojo();
    mojo.execute();

    List<File> copiedFiles = getCopiedFiles();
    assertThat(copiedFiles).hasSize(55);
  }
}
