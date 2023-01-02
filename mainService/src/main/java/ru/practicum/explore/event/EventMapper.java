package ru.practicum.explore.event;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.category.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequestDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.user.UserMapper;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn().format(formatter));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(formatter) : null);
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate().format(formatter));
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public static Event toEventFromNewEventDto(NewEventDto newEventDto, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit() : 0);
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);
        event.setViews(0);
        return event;
    }

    public static Event toEventFromUpdateEventDto(UpdateEventRequestDto updateEventRequestDto,
                                                  Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(updateEventRequestDto.getAnnotation());
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setDescription(updateEventRequestDto.getDescription());
        event.setEventDate(LocalDateTime.parse(updateEventRequestDto.getEventDate(), formatter));
        event.setId(updateEventRequestDto.getEventId());
        event.setPaid(updateEventRequestDto.isPaid());
        event.setParticipantLimit(updateEventRequestDto.getParticipantLimit());
        event.setTitle(updateEventRequestDto.getTitle());
        return event;
    }

    public static Event toEventFromAdminUpdateEventRequestDto(AdminUpdateEventRequestDto adminUpdateEventRequestDto,
                                                              Category category) {
        Event event = new Event();
        event.setAnnotation(adminUpdateEventRequestDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(adminUpdateEventRequestDto.getDescription());
        event.setEventDate(LocalDateTime.parse(adminUpdateEventRequestDto.getEventDate(), formatter));
        event.setLocation(adminUpdateEventRequestDto.getLocation());
        event.setPaid(adminUpdateEventRequestDto.isPaid());
        event.setParticipantLimit(adminUpdateEventRequestDto.getParticipantLimit());
        event.setRequestModeration(adminUpdateEventRequestDto.isRequestModeration());
        event.setTitle(adminUpdateEventRequestDto.getTitle());
        return event;
    }
}
