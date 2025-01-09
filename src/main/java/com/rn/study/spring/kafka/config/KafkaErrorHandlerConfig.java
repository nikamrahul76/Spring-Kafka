package com.rn.study.spring.kafka.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recovered = new DeadLetterPublishingRecoverer(kafkaTemplate,
            (record, exception) -> {
                String deadLetterTopic = record.topic() + ".DLT";
                return new TopicPartition(deadLetterTopic, record.partition());
            });
        FixedBackOff backOff = new FixedBackOff(1000L, 3);
        return new DefaultErrorHandler(recovered, backOff);
    }
}