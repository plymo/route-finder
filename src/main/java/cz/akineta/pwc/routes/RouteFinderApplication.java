package cz.akineta.pwc.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@SpringBootApplication
@Configuration
@Slf4j
public class RouteFinderApplication {

    @Value("${route-finder.url}")
    private URL jsonUrl;

    public static void main(String... args) {
        SpringApplication.run(RouteFinderApplication.class, args);
    }

    @Bean
    public RouteFinderService routeFinderService(final ObjectMapper objectMapper) {
        return new RouteFinderService(new CountriesJsonToGraphParser(objectMapper).parseByTokens(jsonUrl));
    }

}
