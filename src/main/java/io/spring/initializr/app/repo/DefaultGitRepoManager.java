package io.spring.initializr.app.repo;

import io.spring.initializr.app.configuration.AppConfig;
import io.spring.initializr.app.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.merge.MergeStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultGitRepoManager implements GitRepoManager {

    private Git git;

    private AppConfig appConfig;

    private Log logger = LogFactory.getLog(DefaultGitRepoManager.class);

    public DefaultGitRepoManager(AppConfig appConfig) throws GitAPIException, IOException {
        this.appConfig = appConfig;
        try {
            this.git = Git.open(appConfig.getArchetypeRoot());
        } catch (RepositoryNotFoundException ex) {
            restoreRepo();
        }
    }

    @Override
    public void cloneRepo() throws GitAPIException {
        this.git = Git.cloneRepository().setDirectory(appConfig.getArchetypeRoot()).setURI(appConfig.getUrl()).call();
    }

    @Override
    public void updateRepo() throws GitAPIException {
        try {
            PullResult result = git.pull().setStrategy(MergeStrategy.THEIRS).call();
            if (!result.isSuccessful()) {
                throw new Exception("pull failed!");
            }
        } catch (Exception ex) {
            logger.warn(ex);
            restoreRepo();
        }
    }

    @Override
    public void restoreRepo() throws GitAPIException {
        FileUtils.cleanDir(this.appConfig.getArchetypeRoot());
        cloneRepo();
    }

}
