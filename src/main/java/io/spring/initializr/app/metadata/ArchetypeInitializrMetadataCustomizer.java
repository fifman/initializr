package io.spring.initializr.app.metadata;

import io.spring.initializr.metadata.*;

import java.util.List;
import java.util.stream.Collectors;

public class ArchetypeInitializrMetadataCustomizer implements InitializrMetadataCustomizer {

    private ExtendInitializrProperties initializrProperties;

    public ArchetypeInitializrMetadataCustomizer(ExtendInitializrProperties initializrProperties) {
        this.initializrProperties = initializrProperties;
    }

    @Override
    public void customize(InitializrMetadata metadata) {
        metadata.getConfiguration().merge(initializrProperties);
        List<DependencyGroup> dependencies = initializrProperties.getDependencies().stream().map(dep -> ((DependencyGroup) dep)).collect(Collectors.toList());
        metadata.getDependencies().merge(dependencies);
        metadata.getBootVersions().merge(initializrProperties.getBootVersions());
        metadata.getJavaVersions().merge(initializrProperties.getJavaVersions());
        metadata.getLanguages().merge(initializrProperties.getLanguages());
        metadata.getPackagings().merge(initializrProperties.getPackagings());
        metadata.getTypes().merge(initializrProperties.getTypes());
        metadata.getConfiguration().getEnv().getRepositories().putAll(initializrProperties.getRepositories());
    }
}
