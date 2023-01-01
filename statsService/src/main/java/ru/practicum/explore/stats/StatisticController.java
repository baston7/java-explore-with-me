package ru.practicum.explore.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public void addHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Получен запрос на публикацию статистики");
        statisticService.addHit(EndpointMapper.toEndpoint(endpointHitDto));
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") boolean unique,
                                    @RequestParam(required = false) String start,
                                    @RequestParam(required = false) String end) {
        log.info("Получен запрос на получение статистики");
        return statisticService.getStats(uris, unique, start, end);
    }
}
