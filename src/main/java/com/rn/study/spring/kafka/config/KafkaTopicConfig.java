package com.rn.study.spring.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic deadLetterTopic() {
        return new NewTopic("my-topic.DLT", 1, (short) 1);
    }
}