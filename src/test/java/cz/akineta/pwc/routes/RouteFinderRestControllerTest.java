package cz.akineta.pwc.routes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local-test")
public class RouteFinderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void fromCzeToNonExistentCountryShouldReturn400Status() throws Exception {
        mockMvc.perform(get("/routing/cze/non-existent"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fromNonExistentCountryToCzeShouldReturn400Status() throws Exception {
        mockMvc.perform(get("/routing/non-existent/ita"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fromCzeToGtmShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/routing/cze/gtm"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fromCzeToAutShouldReturnValidRouteAnd200Status() throws Exception {
        List<String> list = Arrays.asList("CZE", "AUT");
        mockMvc.perform(get("/routing/cze/aut"))
                .andExpect(jsonPath("$.route", is(list)))
                .andExpect(status().isOk());
    }

    @Test
    public void fromCzeToItaShouldReturnValidRouteAnd200Status() throws Exception {
        List<String> list = Arrays.asList("CZE", "AUT", "ITA");
        mockMvc.perform(get("/routing/cze/ita"))
                .andExpect(jsonPath("$.route", is(list)))
                .andExpect(status().isOk());
    }

    @Test
    public void fromCzeToYemShouldReturnValidRouteAnd200Status() throws Exception {
        List<String> list = Arrays.asList("CZE", "POL", "RUS", "AZE", "IRN", "IRQ", "SAU", "YEM");
        mockMvc.perform(get("/routing/cze/yem"))
                .andExpect(jsonPath("$.route", is(list)))
                .andExpect(status().isOk());
    }

    @Test
    public void fromCanToCzeShouldReturn404Status() throws Exception {
        mockMvc.perform(get("/routing/can/cze"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fromCzeToCzeShouldReturn400Status() throws Exception {
        mockMvc.perform(get("/routing/cze/cze"))
                .andExpect(status().isBadRequest());
    }

}