package io.spring.initializr.app.configuration;

import io.spring.initializr.app.utils.FileUtils;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.SystemReader;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
@ConfigurationProperties(prefix = "archetype")
public class AppConfig {

    private String path;

    private String url;

    private String propFile;

    private File archetypeRoot;

    private String metaDir;

    private String conf;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getArchetypeRoot() {
        return archetypeRoot;
    }

    public String getPropFile() {
        return propFile;
    }

    public void setPropFile(String propFile) {
        this.propFile = propFile;
    }

    public String getMetaDir() {
        return metaDir;
    }

    public void setMetaDir(String metaDir) {
        this.metaDir = metaDir;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    @PostConstruct
    public void init() throws IOException, ConfigInvalidException {
        initRepo();
        initGitConfig();
    }

    private void initRepo() {
        this.archetypeRoot = FileUtils.createDir(path);
    }

    private void initGitConfig() throws IOException, ConfigInvalidException {
        FileBasedConfig fileBasedConfig = SystemReader.getInstance().openUserConfig( null, FS.DETECTED );
        fileBasedConfig.load();
        fileBasedConfig.setBoolean("http", null, "sslverify", false);
        fileBasedConfig.save();
    }
}
