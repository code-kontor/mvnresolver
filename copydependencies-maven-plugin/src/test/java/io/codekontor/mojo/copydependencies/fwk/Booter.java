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
package io.codekontor.mojo.copydependencies.fwk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;

/**
 * A helper to boot the repository system and a repository system session.
 */
public class Booter
{

  /** - */
  private static final String LOCAL_REPO = System.getProperty("user.home") + File.separator + ".m2"
      + File.separator + "repository";

  /**
   * <p>
   * </p>
   *
   * @param system
   * @return
   */
  public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system)
  {
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

    LocalRepository localRepo = new LocalRepository(LOCAL_REPO);
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

    session.setTransferListener(new ConsoleTransferListener());
    session.setRepositoryListener(new NullConsoleRepositoryListener());

    // uncomment to generate dirty trees
    // session.setDependencyGraphTransformer( null );

    return session;
  }

  /**
   * <p>
   * </p>
   *
   * @param system
   * @param session
   * @return
   */
  public static List<RemoteRepository> newRepositories(RepositorySystem system,
      RepositorySystemSession session)
  {
    return new ArrayList<RemoteRepository>(Arrays.asList(newCentralRepository()));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static RemoteRepository newCentralRepository()
  {
    return new RemoteRepository.Builder("central", "default", "http://repo1.maven.org/maven2")
        .build();
  }
}
