package io.spring.initializr.app.metadata;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.IOException;

public class ArchetypePropertiesLoader {

    public static <T> T load(Resource resource, Class<T> clazz) throws IOException {
        Yaml yaml = new Yaml(new Constructor(clazz));
        return yaml.load(resource.getInputStream());
    }

    public static <T> T load(File file, Class<T> clazz) throws IOException {
        return load(new FileSystemResource(file), clazz);
    }

    public static <T> T load(String path, Class<T> clazz) throws IOException {
        return load(new File(path), clazz);
    }
}
