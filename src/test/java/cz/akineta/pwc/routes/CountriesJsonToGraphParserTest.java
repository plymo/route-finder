package cz.akineta.pwc.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.akineta.pwc.routes.exceptions.CountriesJsonDataParsingException;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Slf4j
public class CountriesJsonToGraphParserTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final CountriesJsonToGraphParser PARSER = new CountriesJsonToGraphParser(OBJECT_MAPPER);

    // parse by tokens

    @Test
    public void parseByTokensNullUrl() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTokens(null));
    }

    @Test
    public void parseByTokensInvalidJson() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTokens(getResource("countries-invalid.json")));
    }

    private URL getResource(String resourceName) {
        return this.getClass().getClassLoader().getResource(resourceName);
    }

    @Test
    public void parseByTokensNonExistentJson() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTokens(getResource("countries-non-existent.json")));
    }

    @Test
    public void parseByTokensEmptyJson() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTokens(getResource("countries-empty.json")));
    }

    @Test
    public void parseByTokensDifferentJson() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTokens(getResource("countries-different.json")));
    }

    @Test
    public void parseByTokensValidJson() {
        DefaultDirectedGraph<String, DefaultEdge> graph = PARSER.parseByTokens(getResource("countries.json"));
        assertThat(graph.vertexSet().size(), is(equalTo(250)));
    }

    // parse by tree

    @Test
    public void parseByTreeNullUrl() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTree(null));
    }

    @Test
    public void parseByTreeInvalidJson() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTree(getResource("countries-invalid.json")));
    }

    @Test
    public void parseByTreeNonExistentJson() {
        Assertions.assertThrows(CountriesJsonDataParsingException.class,
                () -> PARSER.parseByTree(getResource("countries-non-existent.json")));
    }

    @Test
    public void parseByTreeEmptyJson() {
        DefaultDirectedGraph<String, DefaultEdge> graph = PARSER.parseByTree(getResource("countries-empty.json"));
        assertThat(graph.vertexSet().size(), is(equalTo(0)));
    }

    @Test
    public void parseByTreeDifferentJson() {
        DefaultDirectedGraph<String, DefaultEdge> graph = PARSER.parseByTree(getResource("countries-different.json"));
        assertThat(graph.vertexSet().size(), is(equalTo(0)));
    }

    @Test
    public void parseByTreeValidJson() {
        DefaultDirectedGraph<String, DefaultEdge> graph = PARSER.parseByTree(getResource("countries.json"));
        assertThat(graph.vertexSet().size(), is(equalTo(250)));
    }

}