package org.aprikhodskiy.otus.kafka.producer.integration;

import org.aprikhodskiy.otus.kafka.producer.dto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class PersonMessageProducer {
    @Autowired
    private KafkaTemplate<String, Person> kt;

    @Value("${integration.topic.person}")
    private String topicName;

    public void sendMessage(Person person) {

        kt.setDefaultTopic(topicName);
        Message<Person> msg = MessageBuilder.withPayload(person)
                .setHeader("event", "personUpdated")
                .setHeader("person_id", person.getId())
                .build();

        kt.send(msg);
    }
}
