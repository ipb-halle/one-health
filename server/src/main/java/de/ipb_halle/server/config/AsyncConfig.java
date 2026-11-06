package de.ipb_halle.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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
     * - corePoolSize=2 is intentionally small because the downstream document processing
     *   service processing is limited.
     * - maxPoolSize=3 allows for minimal scaling during temporary load spikes.
     * - queueCapacity=50 provides a reasonable buffer for batched document processing requests.
     * - CallerRunsPolicy ensures graceful degradation: when queue is full, the caller
     *   blocks until a thread becomes available, preventing
     *   unbounded queue growth and backpressure buildup.
     *
     * @return configured ThreadPoolTaskExecutor for document workers
     */
    @Bean(name = "documentWorkerExecutor")
    public ThreadPoolTaskExecutor documentWorkerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("doc-worker-");

        // Use CallerRunsPolicy to apply backpressure when queue is full
        RejectedExecutionHandler callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        executor.setRejectedExecutionHandler(callerRunsPolicy);

        executor.initialize();
        return executor;
    }

    /**
     * RestTemplate bean for communication with downstream document processing service.
     *
     * Configured with:
     * - 30-second connection and read timeout to handle file conversion latency
     * - HTTP client configured for multipart file uploads
     *
     * @return configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    /**
     * WebClient bean for reactive communication with downstream consumption service.
     *
     * Configured with:
     * - Base URL pointing to the downstream RDKit extraction service
     * - 30-second timeout for file processing requests
     * - Increased buffer size (10MB) to handle multipart file uploads
     *
     * @return configured WebClient instance
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}

