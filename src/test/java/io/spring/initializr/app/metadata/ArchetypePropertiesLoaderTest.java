package io.spring.initializr.app.metadata;

import io.spring.initializr.metadata.InitializrProperties;
import org.junit.Test;

import java.io.IOException;

public class ArchetypePropertiesLoaderTest {

    @Test
    public void testLoad() throws IOException {
        ExtendInitializrProperties initializrProperties = ArchetypePropertiesLoader.load("/var/test/testarch/config/properties.yml", ExtendInitializrProperties.class);
    }

}
