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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResult;

/**
 * <p>
 * Helper mojo that copies all specified maven artifacts to the target location folder.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Mojo(name = "copyDependencies", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class CopyDependenciesMojo extends AbstractMojo {

  /**
   * The entry point to Maven Artifact Resolver, i.e. the component doing all the work.
   */
  @Component
  private RepositorySystem        repoSystem;

  /**
   * The current maven project
   */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject            project;

  /**
   * The current repository/network configuration of Maven.
   */
  @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
  private RepositorySystemSession repoSession;

  /**
   * The project's remote repositories to use for the resolution.
   */
  @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
  private List<RemoteRepository>  remoteRepos;

  /**
   * The target directory
   */
  @Parameter(defaultValue = "${project.build.directory}/libs", property = "targetDir", required = true)
  private String                  targetDirectory;

  /**
   * The coordinates of the maven artifacts to copy.
   */
  @Parameter(property = "coords", required = true)
  private List<String>            coords;

  /**
   * The (comma-separated) list of group ids to exclude.
   */
  @Parameter(property = "excludeGroupIds", required = false)
  private String                  excludeGroupIds;

  /**
   * The (comma-separated) list of artifact ids to exclude.
   */
  @Parameter(property = "excludeArtifactIds", required = false)
  private String                  excludeArtifactIds;

  // the resolved list of group is to exclude
  private List<String>            excludeGroupIdsList    = Collections.emptyList();

  // the resolved list of artifact is to exclude
  private List<String>            excludeArtifactIdsList = Collections.emptyList();

  /**
   * Returns the {@link RepositorySystem}.
   *
   * @return the {@link RepositorySystem}
   */
  public RepositorySystem getRepoSystem() {
    return repoSystem;
  }

  /**
   * Returns the {@link RepositorySystemSession}.
   *
   * @return the {@link RepositorySystemSession}
   */
  public RepositorySystemSession getRepoSession() {
    return repoSession;
  }

  /**
   * Returns the list of {@link RemoteRepository RemoteRepositories}.
   *
   * @return the list of {@link RemoteRepository RemoteRepositories}
   */
  public List<RemoteRepository> getRemoteRepos() {
    return remoteRepos;
  }

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException {

    try {

      if (excludeGroupIds != null) {
        excludeGroupIdsList = Arrays.stream(excludeGroupIds.split(",")).map(r -> r.trim()).collect(Collectors.toList());
      }

      if (excludeArtifactIds != null) {
        excludeArtifactIdsList = Arrays.stream(excludeArtifactIds.split(",")).map(r -> r.trim())
            .collect(Collectors.toList());
      }

      CollectRequest collectRequest = new CollectRequest();

      for (String coordinate: coords) {

        // create the dependency...
        Dependency dependency = new Dependency(new DefaultArtifact(coordinate), "compile");

        // ..and add it to the request
        collectRequest.addDependency(dependency);
      }

      for (RemoteRepository remoteRepository : remoteRepos) {
        collectRequest.addRepository(remoteRepository);
      }

      DependencyRequest dependencyRequest = new DependencyRequest();
      dependencyRequest.setCollectRequest(collectRequest);
      DependencyResult result = repoSystem.resolveDependencies(repoSession, dependencyRequest);

      // the targetDirectory
      File targetDirectoryAsFile = new File(project.getBasedir(), targetDirectory);

      for (ArtifactResult artifactResult : result.getArtifactResults()) {

        if (!excludeGroupIdsList.contains(artifactResult.getArtifact().getGroupId())
            && !excludeArtifactIdsList.contains(artifactResult.getArtifact().getArtifactId())) {

          FileUtils.copyFile(artifactResult.getArtifact().getFile(),
              new File(targetDirectoryAsFile, artifactResult.getArtifact().getFile().getName()));
        }
      }

    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

}
