package com.owary.shortenr.integration;

import com.owary.shortenr.ShortenrApplication;
import com.owary.shortenr.domain.Link;
import com.owary.shortenr.repository.LinkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static com.owary.shortenr.utils.Utils.decode;
import static com.owary.shortenr.utils.Utils.getFullUrl;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShortenrApplication.class, webEnvironment = RANDOM_PORT)
public class LinkControllerIntegration {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private LinkRepository repository;

    private MockMvc mockMvc;

    // parameters
    private final String POST_URL_REQUEST = "/shorten";
    private final String GET_URL_REQUEST = "/url/{id}";

    private String URL = "https://hackernoon.com/spring-boot-rest-tdd-from-scratch-15f13ed799e0";

    private static Long ID;
    private static String shortened = "SHORT";
    private static String original = "LONG";
    private static LocalDate expires = LocalDate.now().plusMonths(1);
    private static Link link;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders
                    .webAppContextSetup(wac)
                    .build();

        link = Link.builder()
                    .shortened(shortened)
                    .original(original)
                    .expiration(expires)
                .build();
        ID = repository.save(link).getId();
    }

    @Test
    public void postLink() throws Exception {
        mockMvc.perform(post(POST_URL_REQUEST)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("original").value(decode(URL)));
    }

    @Test
    public void getLink() throws Exception{
        mockMvc.perform(get(GET_URL_REQUEST, ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("shortened").value(getFullUrl(shortened)))
                .andExpect(jsonPath("original").value(original));
    }

}
