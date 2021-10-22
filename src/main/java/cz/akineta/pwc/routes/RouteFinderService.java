package cz.akineta.pwc.routes;

import cz.akineta.pwc.routes.dto.RouteDto;
import cz.akineta.pwc.routes.exceptions.InvalidDestinationException;
import cz.akineta.pwc.routes.exceptions.InvalidOriginException;
import cz.akineta.pwc.routes.exceptions.NoLandCrossingException;
import cz.akineta.pwc.routes.exceptions.RouteNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
public class RouteFinderService {

    private final DefaultDirectedGraph<String, DefaultEdge> countriesGraph;
    private final DijkstraShortestPath<String, DefaultEdge> dijkstra;

    public RouteFinderService(DefaultDirectedGraph<String, DefaultEdge> countriesGraph) {
        Assert.notNull(countriesGraph, "countriesGraph cannot be null");
        Assert.notEmpty(countriesGraph.vertexSet(), "countriesGraph cannot be empty");
        Assert.notEmpty(countriesGraph.edgeSet(), "countriesGraph must have some borders");
        this.countriesGraph = countriesGraph;
        this.dijkstra = new DijkstraShortestPath<>(countriesGraph);
    }

    public RouteDto findRoute(final String origin, final String destination) {
        checkOrigin(origin);
        checkDestination(destination);

        final GraphPath<String, DefaultEdge> path = getShortestPath(origin, destination);
        if (path.getVertexList().size() <= 1) {
            throw new NoLandCrossingException("No land crossing from '" + origin + "' to '" + destination + "'");
        }

        return createRoute(path.getVertexList());
    }

    private void checkOrigin(final String origin) {
        if (StringUtils.isBlank(origin) || !countriesGraph.containsVertex(origin)) {
            throw new InvalidOriginException("Origin country '" + origin + "' not found");
        }
    }

    private void checkDestination(final String destination) {
        if (StringUtils.isBlank(destination) || !countriesGraph.containsVertex(destination)) {
            throw new InvalidDestinationException("Destination country '" + destination + "' not found");
        }
    }

    private GraphPath<String, DefaultEdge> getShortestPath(final String origin, final String destination) {
        final GraphPath<String, DefaultEdge> path = dijkstra.getPath(origin, destination);
        if (path == null) {
            throw new RouteNotFoundException("Route from '" + origin + "' to '" + destination + "' does not exist");
        }
        return path;
    }

    private RouteDto createRoute(final List<String> vertexes) {
        return new RouteDto(vertexes);
    }

}
