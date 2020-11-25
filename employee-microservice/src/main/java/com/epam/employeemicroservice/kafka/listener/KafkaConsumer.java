package com.epam.employeemicroservice.kafka.listener;

import com.epam.employeemicroservice.service.DepartmentSnapshotServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class KafkaConsumer {

    @Autowired
    private Set<Long> changedDepartments;

    static final Logger logger = LogManager.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "DEPARTMENTS", groupId = "default_group")
    public void consume(Long message) {

        logger.info("Consumed new message on topic 'DEPARTMENTS' " + message);
        changedDepartments.add(message);
    }
}
