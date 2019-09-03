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

import io.codekontor.mvnresolver.api.IMvnCoordinate;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependency;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencyExclusion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * A Shrinkwrap-Resolver based implementation (https://github.com/shrinkwrap/resolver) of the {@link IMvnResolverService} interface.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class MvnResolverServiceImplementation implements IMvnResolverService {

	// the resolver system
	ConfigurableMavenResolverSystem _resolverSystem;

	/**
	 */
	public void initialize(ConfigurableMavenResolverSystem resolverSystem) {
		_resolverSystem = checkNotNull(resolverSystem);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public IMvnResolverJob newMvnResolverJob() {
		return new MvnResolverJobImplementation(this);
	}

	/**
	 *
	 * @param coordinate
	 * @return
	 */
	@Override
	public IMvnCoordinate parseCoordinate(String coordinate) {
		return new MvnCoordinateImplementation(MavenCoordinates.createCoordinate(coordinate));
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public File[] resolve(boolean transitive, String... coords) {
		MavenStrategyStage mavenStrategyStage = _resolverSystem.resolve(checkNotNull(coords));
		MavenFormatStage mavenFormatStage = transitive ? mavenStrategyStage.withTransitivity() : mavenStrategyStage.withoutTransitivity();
		return mavenFormatStage.asFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File[] resolve(String... coords) {
		return resolve(true, coords);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File resolveArtifact(String canonicalForm) {
		return _resolverSystem.resolve(checkNotNull(canonicalForm)).withoutTransitivity().asSingleFile();
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param job
	 * @return
	 */
	File[] resolve(MvnResolverJobImplementation job) {

		//
		List<MavenDependency> dependencies = new ArrayList<>();
		List<MavenDependencyExclusion> exclusions = new ArrayList<>();
		
		for (String exclusionPattern : job.getExclusionPatterns()) {
			exclusions.add(MavenDependencies.createExclusion(exclusionPattern));
		}
		
		for (String coord : job.getCoords()) {
			MavenDependency mavenDependency = MavenDependencies.createDependency(coord, ScopeType.COMPILE, false,exclusions.toArray(new MavenDependencyExclusion[0]));
			dependencies.add(mavenDependency);
		}
		
		return _resolverSystem
		  .addDependencies(dependencies).resolve().withTransitivity().asFile();

	}
}
