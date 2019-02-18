package io.spring.initializr.app.repo;

import io.spring.initializr.app.configuration.AppConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration()
@EnableConfigurationProperties
@Import({AppConfig.class, TestGitRepoManager.class, DefaultGitRepoManager.class})
public class TestRepoContext {

}
