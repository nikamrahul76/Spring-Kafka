package com.rn.study.spring.kafka.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ConsumerService {
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        System.out.println("Consuming message: " + record.value() + "OffSet: " + record.offset());
        //Process Message
        acknowledgment.acknowledge();
    }

    @DltHandler
    public void dltHandle(ConsumerRecord<String, String> record) {
        System.out.println("It's dlt handler catch message: " + record.value());
    }
}
