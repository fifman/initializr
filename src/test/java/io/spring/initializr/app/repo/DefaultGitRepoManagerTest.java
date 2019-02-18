package io.spring.initializr.app.repo;

import io.spring.initializr.app.configuration.AppConfig;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DefaultGitRepoManagerTest {

    private AppConfig appConfig;

    private DefaultGitRepoManager defaultGitRepoManager;

    @Before
    public void init() throws GitAPIException, IOException {
        this.appConfig = new AppConfig();
        this.appConfig.setPath("/var/testgit");
        this.appConfig.setUrl("https://gogs.msplat.io/fengyc/testarch");
        this.defaultGitRepoManager = new DefaultGitRepoManager(this.appConfig);
    }

    @Test
    public void testUpdateRepo() throws Exception {
        defaultGitRepoManager.updateRepo();
    }

    @Test
    public void testRestoreRepo() throws Exception {
        defaultGitRepoManager.restoreRepo();
    }
}
