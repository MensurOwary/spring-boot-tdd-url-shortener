package com.owary.shortenr.aspect;

import com.owary.shortenr.exception.IllegalParameterProvidedException;
import com.owary.shortenr.exception.LinkNotFoundException;
import com.owary.shortenr.exception.UrlShorteningProcessException;
import com.owary.shortenr.exception.WrongURLFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LinkControllerAdvice {

    @ExceptionHandler({UrlShorteningProcessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleInternalException(){}

    @ExceptionHandler({WrongURLFormatException.class, IllegalParameterProvidedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequest(){}

    @ExceptionHandler({LinkNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(){}


}
