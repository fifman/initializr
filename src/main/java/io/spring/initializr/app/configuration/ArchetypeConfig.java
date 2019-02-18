package io.spring.initializr.app.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.spring.initializr.app.model.Archetype;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    public Archetype getArchetype(String id) {
        if (!CollectionUtils.isEmpty(this.archetypes))
            for (Archetype archetype: this.archetypes) {
                if (ObjectUtils.nullSafeEquals(id, archetype.getId())) {
                    return archetype;
                }
            }
        return null;
    }

    public Map<String, Map<String, Object>> getArchetypePropertiesMap() {
        return archetypePropertiesMap;
    }


}
