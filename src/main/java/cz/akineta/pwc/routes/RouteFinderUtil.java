package cz.akineta.pwc.routes;

public final class RouteFinderUtil {

    public static String countryCode(final String name) {
        return name != null ? name.toUpperCase() : null;
    }

}
