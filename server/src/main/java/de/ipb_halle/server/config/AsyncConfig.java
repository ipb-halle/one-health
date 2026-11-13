package de.ipb_halle.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@EnableScheduling
@EnableRetry
public class AsyncConfig {

    /**
     * ThreadPoolTaskExecutor bean for document processing workers.
     *
     * Configuration rationale:
     * - corePoolSize is kept small because the downstream document processing
     *   service is limited by a semaphore.
     * - maxPoolSize allows for minimal scaling during temporary load spikes.
     * - queueCapacity provides a buffer for batched document processing requests.
     * - CallerRunsPolicy ensures graceful degradation: when queue is full, the caller
     *   blocks until a thread becomes available, preventing
     *   unbounded queue growth and backpressure buildup.
     *
     * All values are injected from application properties to allow runtime configuration
     * without code changes.
     *
     * @return configured ThreadPoolTaskExecutor for document workers
     */
    @Bean(name = "documentWorkerExecutor")
    public ThreadPoolTaskExecutor documentWorkerExecutor(
            @Value("${document.worker.thread-core-size}") int corePoolSize,
            @Value("${document.worker.thread-max-size}") int maxPoolSize,
            @Value("${document.worker.queue-capacity}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("doc-worker-");

        // Use CallerRunsPolicy to apply backpressure when queue is full
        RejectedExecutionHandler callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        executor.setRejectedExecutionHandler(callerRunsPolicy);

        executor.initialize();
        return executor;
    }

    /**
     * RestClient bean for communication with downstream consumption service.
     *
     * Configured with:
     * - Base URL pointing to the downstream extraction service
     * - Read timeout for file processing requests
     * - JdkClientHttpRequestFactory for reliable timeout handling
     *
     * All values are injected from application properties to allow runtime configuration
     * without code changes.
     *
     * @return configured RestClient instance
     */
    @Bean
    public RestClient downstreamRestClient(
            RestClient.Builder builder,
            @Value("${document.downstream.base-url}") String baseUrl,
            @Value("${document.downstream.timeout-seconds}") int timeoutSeconds) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(timeoutSeconds));
        
        return builder
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }
}

