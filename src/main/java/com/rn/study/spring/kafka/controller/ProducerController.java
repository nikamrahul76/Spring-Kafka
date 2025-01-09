package com.rn.study.spring.kafka.controller;

import com.rn.study.spring.kafka.kafka.ProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("kafka/")
public class ProducerController {
    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("produce")
    public Mono<String> produce(@RequestBody String message) {
        return Mono.just(message)
                .flatMap(s -> {
                    producerService.sendMessage("my-topic", message);
                    return Mono.just("message has been Produce on Topic my-topic");
                });
    }
}
