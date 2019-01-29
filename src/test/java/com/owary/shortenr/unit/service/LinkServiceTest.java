package com.owary.shortenr.unit.service;

import com.owary.shortenr.domain.Link;
import com.owary.shortenr.exception.IllegalParameterProvidedException;
import com.owary.shortenr.exception.LinkNotFoundException;
import com.owary.shortenr.exception.WrongURLFormatException;
import com.owary.shortenr.repository.LinkRepository;
import com.owary.shortenr.service.LinkService;
import com.owary.shortenr.service.LinkServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Optional;

import static com.owary.shortenr.utils.Utils.shorten;
import static org.mockito.Mockito.*;

public class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;

    private LinkService linkService;

    // Used Params Start
    private String LEGAL_URL = "https://dzone.com/articles/unit-and-integration-tests-in-spring-boot-2";
    private String MALFORMED = "//:thisdasjdasda.";

    private final Long ID = 12364L;
    // Used Params End

    @Before
    public void init() throws UnsupportedEncodingException {
        MockitoAnnotations.initMocks(this);
        linkService = new LinkServiceImpl(linkRepository);
        LEGAL_URL = URLEncoder.encode(LEGAL_URL, "UTF-8");
        MALFORMED = URLEncoder.encode(MALFORMED, "UTF-8");
    }

    @Test
    public void shorten_LegalParam_ShouldReturnLink(){
        // arrange
        Link linkExpected = Link.builder()
                                    .id(12364L)
                                    .original(LEGAL_URL)
                                    .shortened(shorten())
                                .build();

        when(linkRepository.save(any(Link.class)))
                .thenReturn(linkExpected);
        // perform
        Link linkResponse = linkService.shorten(LEGAL_URL);

        // assert
        Assert.assertEquals(linkResponse, linkExpected);
        verify(linkRepository, times(1)).save(any(Link.class));
    }

    @Test(expected = WrongURLFormatException.class)
    public void shorten_MalformedUrl_ShouldThrowWrongURLFormatException() {
        linkService.shorten(MALFORMED);
    }

    @Test(expected = WrongURLFormatException.class)
    public void shorten_NullGiven_ShouldThrowWrongURLFormatException(){
        linkService.shorten(null);
    }

    @Test
    public void getLink_LegalParam_ShouldReturnLink(){
        // arrange
        Link link = Link
                        .builder()
                            .id(ID)
                            .original(LEGAL_URL)
                            .shortened(shorten())
                            .expiration(LocalDate.now().plusMonths(1))
                        .build();
        when(linkRepository.findById(anyLong()))
                .thenReturn(Optional.of(link));

        // perform
        Link linkResponse = linkService.getLink(ID);

        // assert
        Assert.assertEquals(link.getId(), linkResponse.getId());
        verify(linkRepository, times(1)).findById(anyLong());
    }

    @Test(expected = IllegalParameterProvidedException.class)
    public void getLink_GivenNullParam_ShouldThrowIllegalParameterProvidedException(){
        // perform
        linkService.getLink(null);
    }

    @Test(expected = LinkNotFoundException.class)
    public void getLink_InvalidId_ShouldThrowLinkNotFoundException(){
        // arrange
        when(linkRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // perform
        linkService.getLink(anyLong());

        // assert
        verify(linkRepository, times(1)).findById(anyLong());
    }

}
