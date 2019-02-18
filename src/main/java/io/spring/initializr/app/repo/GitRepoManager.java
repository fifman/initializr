package io.spring.initializr.app.repo;

import org.eclipse.jgit.api.errors.GitAPIException;

public interface GitRepoManager {

    void cloneRepo() throws GitAPIException;

    public void updateRepo() throws GitAPIException;

    public void restoreRepo() throws GitAPIException;

}
