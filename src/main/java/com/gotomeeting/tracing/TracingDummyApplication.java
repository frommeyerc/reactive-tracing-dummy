package com.gotomeeting.tracing;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.opentelemetry.instrumentation.reactor.ContextPropagationOperator;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class TracingDummyApplication {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
		SpringApplication.run(TracingDummyApplication.class, args);
	}

    @Bean
    public ReactorTracingBean tracingForReactor() {
        return new ReactorTracingBean();
    }

    @Slf4j
    private static class ReactorTracingBean {

        private final ContextPropagationOperator cpo = ContextPropagationOperator.create();

        @PostConstruct
        public void init() {
            log.info("Setting up reactor tracing...");
            cpo.registerOnEachOperator();
        }

        @PreDestroy
        public void tearDown() {
            log.info("Tearing down reactor tracing...");
            cpo.resetOnEachOperator();
        }
    }
}
