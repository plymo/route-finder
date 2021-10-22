package cz.akineta.pwc.routes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid destination country")
public class InvalidDestinationException extends RouteFinderException {

    public InvalidDestinationException(String message) {
        super(message);
    }

}
