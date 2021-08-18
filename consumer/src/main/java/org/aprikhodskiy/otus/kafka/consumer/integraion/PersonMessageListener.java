package org.aprikhodskiy.otus.kafka.consumer.integraion;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.aprikhodskiy.otus.kafka.consumer.dto.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class PersonMessageListener {
    @Value("${spring.kafka.bootstrap-servers}")
    private String server;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }

    @Bean
    public ConsumerFactory<String, Person> standConsumerFactory() {

        JsonDeserializer<Person> deserializer = new JsonDeserializer<>(Person.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Person> personMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Person> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(standConsumerFactory());
        return factory;
    }

    @KafkaListener(topics = "${integration.topic.person}",
            containerFactory = "personMessageKafkaListenerContainerFactory")
    public void listen(Message<Person> msg) {
        log.info("Person: " + msg.getPayload());
    }
}
