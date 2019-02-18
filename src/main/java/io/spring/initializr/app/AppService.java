package io.spring.initializr.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Initializr app application. Enables legacy STS support for older clients.
 *
 * @author Stephane Nicoll
 */
@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
public class AppService {

	public static void main(String[] args) {
		SpringApplication.run(AppService.class, args);
	}

	@Configuration
	@EnableAsync
	static class AsyncConfiguration extends AsyncConfigurerSupport {

		@Override
		public Executor getAsyncExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(1);
			executor.setMaxPoolSize(5);
			executor.setThreadNamePrefix("initializr-");
			executor.initialize();
			return executor;
		}

	}

}
