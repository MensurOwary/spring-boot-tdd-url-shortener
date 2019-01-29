package com.owary.shortenr.unit.controller;

import com.owary.shortenr.aspect.LinkControllerAdvice;
import com.owary.shortenr.controller.LinkController;
import com.owary.shortenr.domain.Link;
import com.owary.shortenr.exception.WrongURLFormatException;
import com.owary.shortenr.service.LinkService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.owary.shortenr.utils.Utils.getFullUrl;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LinkControllerTest {

    // properties
    private LinkController linkController;
    private MockMvc mockMvc;

    // Mocked service
    @Mock
    private LinkService linkService;

    // parameters
    private final String POST_URL_REQUEST = "/shorten";
    private final String GET_URL_REQUEST = "/url/{id}";

    private final Long ID = 13654L;
    private final Long INVALID_ID = -13654L;
    private String LONG_CORRECT_URL = "https://hackernoon.com/spring-boot-rest-tdd-from-scratch-15f13ed799e0";
    private final String LONG_INCORRECT_URL = "this ain't a url";
    private final String RANDOM_INPUT = "thisisaninputthatcausesservicelayertoreturnnull";

    private final String SHORTENED = "d4s65ad4";

    private final int STATUS_CREATED = HttpStatus.CREATED.value();
    private final int STATUS_RETRIEVED = HttpStatus.OK.value();

    @Before
    public void init(){
        try {
            // init all the @Mock annotated beans
            MockitoAnnotations.initMocks(this);
            // the controller instance
            linkController = new LinkController(linkService);
            // get the MockMVC to test controller
            mockMvc = MockMvcBuilders.standaloneSetup(linkController)
                    // controller advice should be set in order to
                    // use custom error messages
                    .setControllerAdvice(new LinkControllerAdvice())
                    .build();
            // we encode the URL so that it won't cause any problems
            // because of its structure
            LONG_CORRECT_URL = URLEncoder.encode(LONG_CORRECT_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shorten_LegalParam_ReturnsCreated() throws Exception{
        // arrange
        when(linkService.shorten(LONG_CORRECT_URL))
                .thenReturn(Link.builder()
                        .id(ID)
                        .original(LONG_CORRECT_URL)
                        .build());

        // perform
        this.mockMvc.perform(post(POST_URL_REQUEST).contentType(MediaType.TEXT_PLAIN).content(LONG_CORRECT_URL))
                // assert
                .andExpect(jsonPath("status").value(STATUS_CREATED))
                .andExpect(jsonPath("id").value(ID))
                .andExpect(jsonPath("original").value(LONG_CORRECT_URL));
        // assert
        verify(linkService, times(1)).shorten(LONG_CORRECT_URL);
    }

    @Test
    public void shorten_NotLegalString_ReturnsBadRequest() throws Exception {
        // arrange
        when(linkService.shorten(LONG_INCORRECT_URL))
                .thenThrow(new WrongURLFormatException());
        // perform and assert
        mockMvc.perform(post(POST_URL_REQUEST).contentType(MediaType.TEXT_PLAIN).content(LONG_INCORRECT_URL))
                .andExpect(status().isBadRequest());
        // assert
        verify(linkService, times(1)).shorten(LONG_INCORRECT_URL);
    }

    @Test
    public void shorten_EmptyGiven_ReturnsBadRequest() throws Exception {
        // arrange
        // perform
        mockMvc.perform(post(POST_URL_REQUEST).contentType(MediaType.TEXT_PLAIN).content(""))
                .andExpect(status().isBadRequest());
        // expect
        verify(linkService, times(0)).shorten(anyString());
    }

    @Test
    public void shorten_PayloadReturnsNull_UrlShorteningProcessExceptionThrown() throws Exception {
        // arrange
        when(linkService.shorten(RANDOM_INPUT)).thenReturn(null);
        // perform and assert
        mockMvc.perform(post(POST_URL_REQUEST).contentType(MediaType.TEXT_PLAIN).content(RANDOM_INPUT))
                .andExpect(status().isInternalServerError());
        // assert
        verify(linkService, times(1)).shorten(RANDOM_INPUT);
    }

    @Test
    public void get_LegalParam_ReturnsPayload() throws Exception{
        // arrange
        when(linkService.getLink(ID))
                .thenReturn(Link.builder()
                            .id(ID)
                            .original(LONG_CORRECT_URL)
                            .shortened(SHORTENED)
                            .build());

        // perform and assert
        mockMvc.perform(get(GET_URL_REQUEST, ID))
                .andExpect(jsonPath("status").value(STATUS_RETRIEVED))
                .andExpect(jsonPath("shortened").value(getFullUrl(SHORTENED)))
                .andExpect(jsonPath("original").value(LONG_CORRECT_URL));

        // assert
        verify(linkService, times(1)).getLink(ID);
    }

    @Test
    public void get_NonExistingParam_ReturnsNotFound() throws Exception{
        // arrange
        when(linkService.getLink(INVALID_ID))
                .thenReturn(null);
        // perform & assert
        mockMvc.perform(get(GET_URL_REQUEST, INVALID_ID))
                .andExpect(status().isNotFound());
        // assert
        verify(linkService, times(1)).getLink(INVALID_ID);
    }

    @Test
    public void get_StringGiven_ReturnsBadRequest() throws Exception{
        // arrange
        // perform & assert
        mockMvc.perform(get(GET_URL_REQUEST, "invalid"))
                .andExpect(status().isBadRequest());
        // assert
        verify(linkService, times(0)).getLink(any());
    }

    @Test
    public void get_EmptyGiven_ReturnsNotFound() throws Exception{
        // arrange
        // perform & assert
        mockMvc.perform(get("/url"))
                .andExpect(status().isNotFound());
        // assert
        verify(linkService, times(0)).getLink(any());
    }



}
