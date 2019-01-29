package com.owary.shortenr.service;

import com.owary.shortenr.domain.Link;

public interface LinkService {

    Link shorten(String url);
    Link getLink(Long id);
}
