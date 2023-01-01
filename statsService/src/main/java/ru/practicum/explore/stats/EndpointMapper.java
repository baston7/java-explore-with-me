package ru.practicum.explore.stats;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@UtilityClass
public class EndpointMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Endpoint toEndpoint(EndpointHitDto endpointHitDto) {
        Endpoint endpoint = new Endpoint();
        endpoint.setIp(endpointHitDto.getIp());
        endpoint.setApp(endpointHitDto.getApp());
        endpoint.setUri(endpointHitDto.getUri());
        endpoint.setTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(),formatter));
        return endpoint;
    }
}
