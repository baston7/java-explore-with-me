package ru.practicum.explore.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.dto.ViewStats;
import ru.practicum.explore.stats.model.Endpoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final EndpointRepository endpointRepository;

    public void addHit(Endpoint endpoint) {
        endpointRepository.save(endpoint);
    }

    public List<ViewStats> getStats(List<String> uris, boolean unique, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (start != null) {
            startTime = LocalDateTime.parse(start, formatter);
        } else {
            startTime = LocalDateTime.now().minusYears(300);
        }
        if (end != null) {
            endTime = LocalDateTime.parse(end, formatter);
        } else {
            endTime = LocalDateTime.now().plusYears(300);
        }
        if (unique) {
            return endpointRepository.findUnique(uris, startTime, endTime);
        } else {
            return endpointRepository.findAll(uris, startTime, endTime);
        }
    }
}
