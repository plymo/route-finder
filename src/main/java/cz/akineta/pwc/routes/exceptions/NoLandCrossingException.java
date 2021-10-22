package cz.akineta.pwc.routes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No land crossing from origin to destination")
public class NoLandCrossingException extends RouteFinderException {

    public NoLandCrossingException(String message) {
        super(message);
    }

}
