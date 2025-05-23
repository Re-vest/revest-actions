package school.sptech.re_vest.dto;

import java.util.List;

public record ResultsDTO(int temp, String date, String city, List<DayDTO> forecast) {}
