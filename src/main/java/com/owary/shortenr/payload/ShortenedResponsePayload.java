package com.owary.shortenr.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.owary.shortenr.domain.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ShortenedResponsePayload {

    private int status;
    private Long id;
    private String original;

    @JsonIgnore
    public static ShortenedResponsePayload created(Link link){
        return ShortenedResponsePayload
                .builder()
                    .original(link.getOriginal())
                    .id(link.getId())
                    .status(HttpStatus.CREATED.value())
                .build();
    }

}
