package cz.akineta.pwc.routes.exceptions;

public class CountriesJsonDataParsingException extends RouteFinderException {

    public CountriesJsonDataParsingException(String message) {
        super(message);
    }

    public CountriesJsonDataParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}
