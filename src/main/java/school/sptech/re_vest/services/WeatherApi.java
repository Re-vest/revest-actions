package school.sptech.re_vest.services;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import school.sptech.re_vest.dto.DayDTO;
import school.sptech.re_vest.dto.ResponseDTO;

import java.util.List;

@Service
public class WeatherApi {
    private RestClient restClient = RestClient.builder().baseUrl("https://api.hgbrasil.com/weather?key=5b26eb7a&woeid=45582")
            .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
            .build();

    public Object getRestClient() {
        return restClient.get().retrieve().body(ResponseDTO.class);
    }

    public List<DayDTO> bubbleSortDayMaxTempDTO(List<DayDTO> forecastOrdenada) {
        DayDTO aux;
        for (int i = 0; i < forecastOrdenada.size() - 1; i++) {
            for (int j = 1; j < forecastOrdenada.size() - i; j++) {
                if (forecastOrdenada.get(j - 1).max() < forecastOrdenada.get(j).max()) {
                    aux = forecastOrdenada.get(j - 1);
                    forecastOrdenada.set(j - 1, forecastOrdenada.get(j));
                    forecastOrdenada.set(j, aux);
                }
            }
        }
        return forecastOrdenada;
    }

    public List<DayDTO> bubbleSortDayMinTempDTO(List<DayDTO> forecastOrdenada) {
        DayDTO aux;
        for (int i = 0; i < forecastOrdenada.size() - 1; i++) {
            for (int j = 1; j < forecastOrdenada.size() - i; j++) {
                if (forecastOrdenada.get(j - 1).min() > forecastOrdenada.get(j).min()) {
                    aux = forecastOrdenada.get(j - 1);
                    forecastOrdenada.set(j - 1, forecastOrdenada.get(j));
                    forecastOrdenada.set(j, aux);
                }
            }
        }
        return forecastOrdenada;
    }
}
