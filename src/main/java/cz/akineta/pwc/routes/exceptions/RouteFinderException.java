package cz.akineta.pwc.routes.exceptions;

public class RouteFinderException extends RuntimeException {

    public RouteFinderException(String message) {
        super(message);
    }

    public RouteFinderException(String message, Throwable cause) {
        super(message, cause);
    }

}
