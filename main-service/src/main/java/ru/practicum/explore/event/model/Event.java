package ru.practicum.explore.event.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.Location;
import ru.practicum.explore.event.State;
import ru.practicum.explore.user.model.User;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String annotation;

    @OneToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    String description;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    State state;

    int views;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @Embedded
    Location location;

    boolean paid;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "request_moderation")
    boolean requestModeration;

    String title;
}
