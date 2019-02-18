package io.spring.initializr.app.repo;

import io.spring.initializr.app.configuration.AppConfig;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Primary
public class TestGitRepoManager extends DefaultGitRepoManager {
    public TestGitRepoManager(AppConfig appConfig) throws GitAPIException, IOException {
        super(appConfig);
    }

    @Override
    public void cloneRepo() throws GitAPIException {
    }

    @Override
    public void updateRepo() throws GitAPIException {
    }

    @Override
    public void restoreRepo() throws GitAPIException {
    }
}
