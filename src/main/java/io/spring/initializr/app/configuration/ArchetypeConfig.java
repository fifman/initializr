package io.spring.initializr.app.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.spring.initializr.app.model.Archetype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchetypeConfig {

    private List<Archetype> archetypes;

    @JsonIgnore
    private Map<String, Map<String, Object>> archetypePropertiesMap = new HashMap<>();

    public List<Archetype> getArchetypes() {
        return archetypes;
    }

    public void setArchetypes(List<Archetype> archetypes) {
        this.archetypes = archetypes;
    }

    public void setArchetypePropertiesMap(Map<String, Map<String, Object>> archetypePropertiesMap) {
        this.archetypePropertiesMap = archetypePropertiesMap;
    }

    public Map<String, Map<String, Object>> getArchetypePropertiesMap() {
        return archetypePropertiesMap;
    }


}
