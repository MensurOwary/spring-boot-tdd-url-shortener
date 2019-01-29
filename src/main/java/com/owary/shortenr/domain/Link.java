package com.owary.shortenr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Link {

    @Id
    @GeneratedValue
    private Long id;
    private String original;
    private String shortened;
    private LocalDate expiration;

}
