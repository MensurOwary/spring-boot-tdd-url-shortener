package com.owary.shortenr.service;

import com.owary.shortenr.domain.Link;
import com.owary.shortenr.exception.IllegalParameterProvidedException;
import com.owary.shortenr.exception.LinkNotFoundException;
import com.owary.shortenr.exception.WrongURLFormatException;
import com.owary.shortenr.repository.LinkRepository;
import com.owary.shortenr.utils.Utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.owary.shortenr.utils.Utils.decode;
import static com.owary.shortenr.utils.Utils.isURLCorrect;

@Service
@Transactional
public class LinkServiceImpl implements LinkService{

    private LinkRepository repository;

    public LinkServiceImpl(LinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public Link shorten(String url){
        url = decode(url);
        if (url == null || !isURLCorrect(url))
            throw new WrongURLFormatException();
        String shortened = Utils.shorten();
        LocalDate localDate = LocalDate.now().plusMonths(1);
        Link link = Link
                .builder()
                    .original(url)
                    .shortened(shortened)
                    .expiration(localDate)
                .build();
        return repository.save(link);
    }

    @Override
    public Link getLink(Long id) {
        if(id == null) throw new IllegalParameterProvidedException();
        return repository.findById(id).orElseThrow(LinkNotFoundException::new);
    }
}
