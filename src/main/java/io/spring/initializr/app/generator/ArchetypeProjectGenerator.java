package io.spring.initializr.app.generator;

import io.spring.initializr.app.configuration.AppConfig;
import io.spring.initializr.app.repo.ArchetypeRepository;
import io.spring.initializr.app.utils.FileUtils;
import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArchetypeProjectGenerator extends ProjectGenerator {

    @Autowired
    ArchetypeRepository archetypeRepository;

    @Autowired
    AppConfig appConfig;

    protected File generateProjectStructure(ProjectRequest request,
                                            Map<String, Object> model) {
        File file = super.generateProjectStructure(request, model);
        ExtendProjectRequest extendProjectRequest = (ExtendProjectRequest) request;
        List<String> archetypes = extendProjectRequest.getArchetypes();
        if (CollectionUtils.isEmpty(archetypes))
            return file;
        for (String archetype : archetypes) {
            try {
                generateArchetype(extendProjectRequest, file, archetype, model);
            } catch (IOException e) {
                throw new RuntimeException("generate archetype error:", e);
            }
        }
        return file;
    }

    protected void generateArchetype(ExtendProjectRequest request, File root, String archetype, Map<String, Object> model)
            throws IOException {
        File dir = root;
        if (request.getBaseDir() != null)
            dir = new File(root, request.getBaseDir());
        Map<String, Object> extraModel = new HashMap<>();
        extraModel.putAll(model);
        extraModel.putAll(archetypeRepository.getArchetypeConfig().getArchetypePropertiesMap().get(archetype));
        File archetypeRoot = new File(appConfig.getPath(), archetype);
        File javaRoot = new File(archetypeRoot, "src/main/java");
        File testRoot = new File(archetypeRoot, "src/test/java");
        FileUtils.copyRecursivelyWithRules(archetypeRoot, dir, null, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.toPath().startsWith(javaRoot.toPath()) || file.toPath().startsWith(testRoot.toPath());
            }
        });
        String packagePath = request.getPackageName().replace(".", "/");
        if (javaRoot.exists()) {
            File javaDir = new File(dir, "src/main/java/" + packagePath);
            if (!javaDir.exists())
                javaDir.mkdirs();
            FileUtils.templateRecursivelyWithRules(javaRoot, javaDir, null, null, extraModel);
        }
        if (testRoot.exists()) {
            File testDir = new File(dir, "src/test/java/" + packagePath);
            if (!testDir.exists())
                testDir.mkdirs();
            FileUtils.templateRecursivelyWithRules(testRoot, testDir, null, null, extraModel);
        }
    }
}
