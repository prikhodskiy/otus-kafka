package org.aprikhodskiy.otus.kafka.consumer.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person implements Serializable {
    private Long id;
    private String name;
    private LocalDateTime modifyDate;
}
