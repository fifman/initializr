package io.spring.initializr.app.repo;

import io.spring.initializr.app.configuration.ArchetypeConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArchetypeRepositoryTest {

    @Autowired
    ArchetypeRepository archetypeRepository;

    @Test
    public void testRefresh() throws Exception {
        archetypeRepository.refresh();
        ArchetypeConfig archetypeConfig = archetypeRepository.getArchetypeConfig();
        archetypeConfig.getArchetypes().stream().forEach(System.out::println);
        System.out.println(archetypeConfig.getArchetypePropertiesMap().get("test"));
    }

    @Test
    public void testListConfig() {
        Arrays.stream(archetypeRepository.listInitializrConfigFiles()).forEach(System.out::println);
    }

    @Test
    public void testListInitializrConfig() {
        archetypeRepository.listArchetypePropertiesFiles().forEach((key, value) -> System.out.println(key + ":" + value));
    }
}
