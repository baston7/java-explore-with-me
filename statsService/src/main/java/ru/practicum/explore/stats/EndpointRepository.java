package ru.practicum.explore.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.stats.dto.ViewStats;
import ru.practicum.explore.stats.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Integer> {
    @Query("select new ru.practicum.explore.stats.dto.ViewStats(e.app,e.uri,count(distinct e.ip)) " +
            "from Endpoint e Where ((?1 is null) or (e.uri in ?1))and (e.timestamp between ?2 and ?3)" +
            " group by e.app,e.uri")
    List<ViewStats> findUnique(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explore.stats.dto.ViewStats(e.app,e.uri,count(e.ip))" +
            " from Endpoint e Where ((?1 is null) or (e.uri in ?1))and (e.timestamp between ?2 and ?3)" +
            " group by e.app,e.uri")
    List<ViewStats> findAll(List<String> uris, LocalDateTime start, LocalDateTime end);
}