package com.owary.shortenr.integration;

import com.owary.shortenr.domain.Link;
import com.owary.shortenr.repository.LinkRepository;
import com.owary.shortenr.service.LinkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LinkServiceIntegrationTest {

    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkRepository repository;

    private static Long ID;
    private static String shortened = "SHORT";
    private static String original = "LONG";
    private static LocalDate expires = LocalDate.now().plusMonths(1);
    private static Link link;

    @Before
    public void init(){
        link = Link.builder()
                    .shortened(shortened)
                    .original(original)
                    .expiration(expires)
                .build();
        ID = repository.save(link).getId();
    }

    @Test
    public void shorten(){
        String URL = "https://stackoverflow.com/questions/43608529/no-code-coverage-in-intellij-2017";
        assertThat(linkService.shorten(URL))
                .isNotNull()
                .isInstanceOf(Link.class)
                .hasFieldOrPropertyWithValue("original", URL);
    }

    @Test
    public void getLink(){
        assertThat(linkService.getLink(ID))
                .isNotNull()
                .isInstanceOf(Link.class)
                .hasFieldOrPropertyWithValue("shortened", shortened)
                .hasFieldOrPropertyWithValue("original", original)
                .hasFieldOrPropertyWithValue("expiration", expires);
    }

}
