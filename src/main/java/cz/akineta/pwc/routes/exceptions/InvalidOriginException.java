package cz.akineta.pwc.routes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid origin country")
public class InvalidOriginException extends RouteFinderException {

    public InvalidOriginException(String message) {
        super(message);
    }

}
