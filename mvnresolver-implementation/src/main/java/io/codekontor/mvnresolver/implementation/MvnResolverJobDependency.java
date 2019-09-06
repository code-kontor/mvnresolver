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

import io.codekontor.mvnresolver.api.IMvnResolverService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MvnResolverJobDependency implements IMvnResolverService.IMvnResolverJobDependency {

    private MvnResolverJobImplementation _resolverJobImplementation;

    private String _coordinate;

    private boolean _optional;

    private List<String> _exclusionPatterns;

    private IMvnResolverService.Scope _scope;

    public MvnResolverJobDependency(MvnResolverJobImplementation resolverJobImplementation, String coordinate) {
        this._coordinate = checkNotNull(coordinate);
        this._resolverJobImplementation = checkNotNull(resolverJobImplementation);
        this._resolverJobImplementation.dependencies().add(this);
        this._exclusionPatterns = new ArrayList<>();
        this._scope = IMvnResolverService.Scope.COMPILE;
    }

    @Override
    public IMvnResolverService.IMvnResolverJobDependency withDependency(String coordinate) {
        return _resolverJobImplementation.withDependency(coordinate);
    }

    @Override
    public File[] resolve() {
        return _resolverJobImplementation.resolve();
    }

    @Override
    public URL[] resolveToUrlArray() {
        return _resolverJobImplementation.resolveToUrlArray();
    }

    @Override
    public IMvnResolverService.IMvnResolverJobDependency withExclusionPattern(String pattern) {
        this._exclusionPatterns.add(checkNotNull(pattern));
        return this;
    }

    @Override
    public IMvnResolverService.IMvnResolverJobDependency withExclusionPatterns(String... patterns) {
        this._exclusionPatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    @Override
    public IMvnResolverService.IMvnResolverJobDependency withOptional(boolean isOptional) {
        _optional = isOptional;
        return this;
    }

    @Override
    public IMvnResolverService.IMvnResolverJobDependency withScope(IMvnResolverService.Scope scope) {
        _scope = checkNotNull(scope);
        return this;
    }

    String coordinate() {
        return _coordinate;
    }

    List<String> exclusionPatterns() {
        return _exclusionPatterns;
    }

    boolean optional() {
        return _optional;
    }

    IMvnResolverService.Scope scope() {
        return _scope;
    }
}
