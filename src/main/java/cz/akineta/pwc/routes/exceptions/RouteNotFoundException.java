package cz.akineta.pwc.routes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Route not found")
public class RouteNotFoundException extends RouteFinderException {

    public RouteNotFoundException(String message) {
        super(message);
    }

}
