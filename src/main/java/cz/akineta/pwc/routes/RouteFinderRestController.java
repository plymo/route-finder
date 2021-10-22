package cz.akineta.pwc.routes;

import cz.akineta.pwc.routes.dto.RouteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static cz.akineta.pwc.routes.RouteFinderUtil.countryCode;

@RestController
@RequestMapping("/routing")
@RequiredArgsConstructor
public class RouteFinderRestController {

    private final RouteFinderService finderService;

    @GetMapping("/{origin}/{destination}")
    @ResponseStatus(HttpStatus.OK)
    public RouteDto findRoute(@PathVariable final String origin, @PathVariable final String destination) {
        return finderService.findRoute(countryCode(origin), countryCode(destination));
    }

}
