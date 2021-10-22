package cz.akineta.pwc.routes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.akineta.pwc.routes.exceptions.CountriesJsonDataParsingException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static cz.akineta.pwc.routes.RouteFinderUtil.countryCode;

@Slf4j
@RequiredArgsConstructor
public class CountriesJsonToGraphParser {

    private static final String COUNTRY_NAME_FIELD_NAME = "cca3";
    private static final String COUNTRY_BORDERS_FIELD_NAME = "borders";
    private final ObjectMapper objectMapper;

    public DefaultDirectedGraph<String, DefaultEdge> parseByTokens(final URL jsonUrl) {
        if (jsonUrl == null) {
            throw new CountriesJsonDataParsingException("JSON url cannot be null");
        }
        log.debug("Loading graph from: {}", jsonUrl);

        try {
            final JsonParser parser = objectMapper.tokenStreamFactory().createParser(jsonUrl);
            final DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

            final JsonToken startArrayToken = parser.nextToken();
            if (startArrayToken == null) {
                throw new CountriesJsonDataParsingException("Cannot parse input JSON file with countries");
            }

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                final Country country = parseCountry(parser);
                if (!graph.containsVertex(country.code)) {
                    graph.addVertex(country.code);
                }

                if (!country.borderCountries.isEmpty()) {
                    country.borderCountries.forEach((borderCountryCode) -> {
                        if (!graph.containsVertex(borderCountryCode)) {
                            graph.addVertex(borderCountryCode);
                        }
                        graph.addEdge(country.code, borderCountryCode);
                    });
                }
            }
            parser.close();
            log.info("JSON to Graph: countries={}, borders={}", graph.vertexSet().size(), graph.edgeSet().size());
            return graph;
        } catch (IOException e) {
            throw new CountriesJsonDataParsingException("Cannot parse input JSON file with countries", e);
        }
    }

    private Country parseCountry(final JsonParser parser) throws IOException {
        String countryCode = null;
        List<String> borderCountries = new ArrayList<>();

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (COUNTRY_NAME_FIELD_NAME.equalsIgnoreCase(parser.getCurrentName())) {
                countryCode = parseCountryCode(parser);
            } else {
                if (COUNTRY_BORDERS_FIELD_NAME.equalsIgnoreCase(parser.getCurrentName())) {
                    borderCountries.addAll(parseBorderCountries(parser));
                } else {
                    parser.skipChildren();
                }
            }
        }

        if (StringUtils.isBlank(countryCode)) {
            throw new CountriesJsonDataParsingException("Cannot parse country code");
        }

        return new Country(countryCode, borderCountries);
    }

    private String parseCountryCode(final JsonParser parser) throws IOException {
        parser.nextValue();
        return countryCode(parser.getText());
    }

    private List<String> parseBorderCountries(final JsonParser parser) throws IOException {
        List<String> borderCountries = new ArrayList<>();
        JsonToken arrayToken = parser.nextToken(); // JsonToken.START_ARRAY;
        if (arrayToken.isStructStart()) {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                borderCountries.add(countryCode(parser.getText()));
            }
        }
        return borderCountries;
    }

    public DefaultDirectedGraph<String, DefaultEdge> parseByTree(final URL jsonUrl) {
        if (jsonUrl == null) {
            throw new CountriesJsonDataParsingException("JSON url cannot be null");
        }
        log.debug("Loading graph from: {}", jsonUrl);

        try {
            final JsonNode tree = objectMapper.readTree(jsonUrl);
            final DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

            if (tree == null) {
                throw new CountriesJsonDataParsingException("Cannot parse input JSON file with countries");
            }

            tree.forEach((countryNode) -> {
                JsonNode countryNameNode = countryNode.findValue(COUNTRY_NAME_FIELD_NAME);
                if (countryNameNode != null) {
                    final String countryName = countryCode(countryNameNode.asText());
                    if (!graph.containsVertex(countryName)) {
                        graph.addVertex(countryName);
                    }

                    JsonNode borderCountries = countryNode.get(COUNTRY_BORDERS_FIELD_NAME);
                    if (borderCountries != null) {
                        borderCountries.forEach((borderCountryNode) -> {
                            String borderCountry = countryCode(borderCountryNode.asText());
                            if (!graph.containsVertex(borderCountry)) {
                                graph.addVertex(borderCountry);
                            }
                            graph.addEdge(countryName, borderCountry);
                        });
                    }
                }
            });
            log.info("JSON to Graph: countries={}, borders={}", graph.vertexSet().size(), graph.edgeSet().size());
            return graph;
        } catch (IOException e) {
            throw new CountriesJsonDataParsingException("Cannot parse input JSON file with countries", e);
        }
    }

    @Value
    private static class Country {
        String code;
        List<String> borderCountries;
    }

}
