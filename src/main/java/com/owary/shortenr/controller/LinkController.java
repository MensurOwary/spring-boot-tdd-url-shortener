package com.owary.shortenr.controller;

import com.owary.shortenr.domain.Link;
import com.owary.shortenr.exception.LinkNotFoundException;
import com.owary.shortenr.exception.UrlShorteningProcessException;
import com.owary.shortenr.payload.ShortUrlResponsePayload;
import com.owary.shortenr.payload.ShortenedResponsePayload;
import com.owary.shortenr.service.LinkService;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

import static com.owary.shortenr.payload.ShortenedResponsePayload.created;
import static com.owary.shortenr.payload.ShortUrlResponsePayload.success;

@RestController
public class LinkController {

    private LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/shorten")
    public ShortenedResponsePayload shorten(@RequestBody String url) throws UrlShorteningProcessException {
        Link link = linkService.shorten(url);
        if (link != null) {
            return created(link);
        }
        throw new UrlShorteningProcessException();
    }

    @GetMapping("/url/{id}")
    public ShortUrlResponsePayload get(@PathVariable Long id){
        Link link = linkService.getLink(id);
        if(link != null){
            return success(link);
        }
        throw new LinkNotFoundException();
    }



}
