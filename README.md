# MvnResolver Uber-jar

Version 1.0.0.RC1 - 2019-09-03

The MvnResolver project aims at providing an easy to use, non-invasive solution to resolve maven artifacts 
from within java applications. Simply add the MvnResolver uber-jar to your project's classpath - all required libraries 
are embedded within the MvnResolver Uber jar, so no additional dependencies are added to your project.

The uber-Jar internally relies on shrinkwrap-resolver.

## Usage 

### Adding MvnResolver to your project

You can add the Mvnresolver uber-jar to your project by adding the following dependency:

```xml
<project>
    ...
    <dependencies>
        ...
        <dependency>
            <groupId>io.codekontor.mvnresolver</groupId>
            <artifactId>mvnresolver-uber</artifactId>
            <version>1.0.0.RC1</version>
        </dependency>
    </dependencies>
</project>
```

### Creating an instance of IMvnResolverServiceFactory

In order to create a new MvnResolverService, you have to create a MvnResolverServie Factory first.

```java
// creating the MvnResolverServiceFactory
IMvnResolverServiceFactory mvnResolverServiceFactory = MvnResolverServiceFactoryFactory
        .createNewResolverServiceFactory();
```

### Configuring and creating a MvnResolverService

```java
// creating a new IMvnResolverService
IMvnResolverService mvnResolverService = mvnResolverServiceFactory.newMvnResolverService().create();
```

### Resolving artifacts

```java
//
File[] files = mvnResolverService.resolve("org.neo4j.test:neo4j-harness:2.3.3");
```

```java
//
File[] files = mvnResolverService.resolve(false, "org.neo4j.test:neo4j-harness:2.3.3");
```

```java
//
File file = mvnResolverService.resolveArtifact("org.neo4j.test:neo4j-harness:2.3.3");
```

#### License

MvnResolver is licensed under the Apache License version 2.0.
