package com.owary.shortenr.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.owary.shortenr.domain.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.owary.shortenr.utils.Utils.getFullUrl;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ShortUrlResponsePayload {

    private int status;
    private String shortened;
    private String original;

    @JsonIgnore
    public static ShortUrlResponsePayload success(Link link){
        return ShortUrlResponsePayload
                .builder()
                    .shortened(getFullUrl(link.getShortened()))
                    .original(link.getOriginal())
                    .status(HttpStatus.OK.value())
                .build();
    }

}
