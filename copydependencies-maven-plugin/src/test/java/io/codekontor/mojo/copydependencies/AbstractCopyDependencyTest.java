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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.codekontor.mojo.copydependencies.fwk.Booter;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractCopyDependencyTest {

  @Rule
  public MojoRule      rule      = new MojoRule();

  @Rule
  public TestResources resources = new TestResources();

  /** - */
  private String       _projectName;

  /** - */
  private File         _targetDirectory;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractCopyDependencyTest}.
   * </p>
   *
   * @param projectName
   */
  public AbstractCopyDependencyTest(String projectName) {
    _projectName = checkNotNull(projectName);
  }

  @Before
  public void before() throws Exception {

    // check project dir
    File baseDir = this.resources.getBasedir(_projectName);
    assertThat(baseDir).isNotNull();
    assertThat(baseDir.isDirectory()).isTrue();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   * @throws Exception
   */
  protected CopyDependenciesMojo findCopyDependencyMojo() throws Exception {

    // Find the project
    File baseDir = this.resources.getBasedir(_projectName);
    Assert.assertNotNull(baseDir);
    Assert.assertTrue(baseDir.isDirectory());

    File pom = new File(baseDir, "pom.xml");
    CopyDependenciesMojo mojo = (CopyDependenciesMojo) this.rule.lookupMojo("copyDependencies", pom);
    Assert.assertNotNull(mojo);

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(pom);

    this.rule.setVariableValueToObject(mojo, "project", mvnProject);
    Assert.assertNotNull(this.rule.getVariableValueFromObject(mojo, "project"));

    this.rule.setVariableValueToObject(mojo, "repoSession", Booter.newRepositorySystemSession(mojo.getRepoSystem()));
    Assert.assertNotNull(this.rule.getVariableValueFromObject(mojo, "repoSession"));

    this.rule.setVariableValueToObject(mojo, "remoteRepos",
        Booter.newRepositories(mojo.getRepoSystem(), mojo.getRepoSession()));
    Assert.assertNotNull(this.rule.getVariableValueFromObject(mojo, "remoteRepos"));

    String targetDir = (String) this.rule.getVariableValueFromObject(mojo, "targetDirectory");
    _targetDirectory = new File(baseDir, targetDir);

    return mojo;
  }

  /**
   * <p>
   * </p>
   */
  protected List<File> getCopiedFiles() {
    return Arrays.asList(_targetDirectory.listFiles());
  }
}
