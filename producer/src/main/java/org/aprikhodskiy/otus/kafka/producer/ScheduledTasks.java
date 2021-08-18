package org.aprikhodskiy.otus.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aprikhodskiy.otus.kafka.producer.dto.Person;
import org.aprikhodskiy.otus.kafka.producer.integration.PersonMessageProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final PersonMessageProducer producer;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    public void reportPersonUpdated() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        Long id = ThreadLocalRandom.current().nextLong(10, 1000);
        Person person = Person.builder()
                .id(id)
                .name("Agent " + id)
                .modifyDate(LocalDateTime.now())
                .build();

        producer.sendMessage(person);
    }
}
