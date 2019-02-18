package io.spring.initializr.app.generator;

import io.spring.initializr.generator.ProjectRequest;

import java.util.ArrayList;
import java.util.List;

public class ExtendProjectRequest extends ProjectRequest {

    private List<String> archetypes = new ArrayList<>();

    public List<String> getArchetypes() {
        return archetypes;
    }

    public void setArchetypes(List<String> archetypes) {
        this.archetypes = archetypes;
    }

}
