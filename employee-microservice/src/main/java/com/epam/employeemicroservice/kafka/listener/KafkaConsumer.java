package com.epam.employeemicroservice.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "DEPARTMENTS", groupId = "default_group")
    public void consume(String message) {
        System.out.println("HEERERERERERE" + message);
    }
}
