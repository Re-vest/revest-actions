package school.sptech.re_vest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.re_vest.dto.DayDTO;
import school.sptech.re_vest.dto.ResponseDTO;
import school.sptech.re_vest.services.WeatherApi;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherApiController {

    @Autowired
    private WeatherApi weatherApi;

    @Operation(
            summary = "buscar dados da API",
            description = "endpoint GET que busca os dados de temperatura e clima da API externa"
    )
    @GetMapping
    public Object weatherApi() {
        return new WeatherApi().getRestClient();
    }

    @Operation(
            summary = "ordena os dados de temperatura pelas máximas",
            description = "endpoint GET que busca os dias ordenados pela temperatura máxima"
    )
    @GetMapping("/max-temp")
    public List<DayDTO> getMaxTemperaturas() {
        ResponseDTO response = (ResponseDTO) weatherApi();
        List<DayDTO> forecastOrdenada = response.results().forecast();

        weatherApi.bubbleSortDayMaxTempDTO(forecastOrdenada);

        return forecastOrdenada;
    }

    @Operation(
            summary = "ordena os dados de temperatura pelas mínimas",
            description = "endpoint GET que busca os dias ordenados pela temperatura mínima"
    )
    @GetMapping("/min-temp")
    public List<DayDTO> getMinTemperaturas() {
        ResponseDTO response = (ResponseDTO) weatherApi();
        List<DayDTO> forecastOrdenada = response.results().forecast();

        weatherApi.bubbleSortDayMinTempDTO(forecastOrdenada);

        return forecastOrdenada;
    }
}
