package io.spring.initializr.app.repo;

import io.spring.initializr.app.configuration.AppConfig;
import io.spring.initializr.app.configuration.ArchetypeConfig;
import io.spring.initializr.app.metadata.ArchetypePropertiesLoader;
import io.spring.initializr.app.metadata.ArchetypeInitializrMetadataCustomizer;
import io.spring.initializr.app.metadata.ExtendInitializrProperties;
import io.spring.initializr.app.model.Archetype;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataBuilder;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.metadata.InitializrProperties;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

@Component
public class ArchetypeRepository {

    private final AppConfig appConfig;

    private final InitializrMetadataProvider initializrMetadataProvider;

    private final InitializrProperties initializrProperties;

    private final GitRepoManager gitRepoManager;

    private ArchetypeConfig archetypeConfig;

    public ArchetypeConfig getArchetypeConfig() {
        return archetypeConfig;
    }

    public void setArchetypeConfig(ArchetypeConfig archetypeConfig) {
        this.archetypeConfig = archetypeConfig;
    }

    public ArchetypeRepository(InitializrProperties initializrProperties, InitializrMetadataProvider initializrMetadataProvider, AppConfig appConfig, GitRepoManager gitRepoManager) throws IOException, GitAPIException {
        this.initializrProperties = initializrProperties;
        this.appConfig = appConfig;
        this.gitRepoManager = gitRepoManager;
        this.initializrMetadataProvider = initializrMetadataProvider;
        this.refresh();
    }

    private void initialize(){
        InitializrMetadata initializrMetadata = this.initializrMetadataProvider.get();
        InitializrMetadata backupInitializrMetadata = InitializrMetadataBuilder.fromInitializrProperties(initializrProperties).withInitializrProperties(initializrProperties).build();
        initializrMetadata.getDependencies().getContent().clear();
        initializrMetadata.getTypes().getContent().clear();
        initializrMetadata.getPackagings().getContent().clear();
        initializrMetadata.getLanguages().getContent().clear();
        initializrMetadata.getJavaVersions().getContent().clear();
        initializrMetadata.getBootVersions().getContent().clear();
        initializrMetadata.merge(backupInitializrMetadata);
    }

    public void refresh() throws IOException, GitAPIException {
        initialize();
        gitRepoManager.updateRepo();
        this.archetypeConfig = ArchetypePropertiesLoader.load(new File(this.appConfig.getArchetypeRoot(), this.appConfig.getConf()), ArchetypeConfig.class);
        this.archetypeConfig.setArchetypePropertiesMap(new HashMap<>());
        for (File configFile : listInitializrConfigFiles()) {
            ExtendInitializrProperties initializrProperties = ArchetypePropertiesLoader.load(configFile, ExtendInitializrProperties.class);
            new ArchetypeInitializrMetadataCustomizer(initializrProperties).customize(this.initializrMetadataProvider.get());
        }
        Map<String, File> archetypePropertiesFiles = listArchetypePropertiesFiles();
        if (archetypePropertiesFiles == null)
            return;
        for (String key: archetypePropertiesFiles.keySet()){
            Map<String, Object> archetypeProperties = ArchetypePropertiesLoader.load(archetypePropertiesFiles.get(key), Map.class);
            this.archetypeConfig.getArchetypePropertiesMap().put(key, archetypeProperties);
        }
    }

    Map<String, File> listArchetypePropertiesFiles(){
        File archetypeRoot = this.appConfig.getArchetypeRoot();
        File[] subDirs = archetypeRoot.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (!file.isDirectory())
                    return false;
                for (Archetype archetype: archetypeConfig.getArchetypes()){
                    if (archetype.getId().equals(file.getName()))
                        return true;
                }
                return false;
            }
        });
        if (subDirs == null || subDirs.length == 0) {
            return null;
        }
        Map<String, File> archetypePropertiesFiles = new HashMap<>();
        for (File subDir : subDirs) {
            File[] files = subDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && file.getName().equals(appConfig.getPropFile());
                }
            });
            if (files == null || files.length == 0)
                continue;
            archetypePropertiesFiles.put(subDir.getName(), files[0]);
        }
        return archetypePropertiesFiles;
    }

    File[] listInitializrConfigFiles() {
        return new File(this.appConfig.getArchetypeRoot(), this.appConfig.getMetaDir()).listFiles();
    }

}
