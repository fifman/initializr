package io.spring.initializr.app.model;

import io.spring.initializr.metadata.Describable;
import io.spring.initializr.metadata.MetadataElement;

public class Archetype extends MetadataElement implements Describable {

    private String description;

    private String[] dependencies;

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }
}
