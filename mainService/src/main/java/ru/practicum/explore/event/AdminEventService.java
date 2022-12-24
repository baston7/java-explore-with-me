package ru.practicum.explore.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    public Event publishEventById(Integer id){
        Event event=eventRepository.findById(id).orElseThrow(()->new RuntimeException("net takogo eventa"));
        if(LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())){
           throw  new RuntimeException("дата начала события должна быть не ранее чем за час от даты публикации.");
        }
        if(!event.getState().equals(State.PENDING)){
            throw  new RuntimeException("событие должно быть в состоянии ожидания публикации");
        }
        event.setState(State.PUBLISHED);
        return eventRepository.save(event);
    }
    public Event cancelEventById(Integer id){
        Event event=eventRepository.findById(id).orElseThrow(()->new RuntimeException("net takogo eventa"));

        if(event.getState().equals(State.PUBLISHED)){
            throw  new RuntimeException("событие уже опубликовано.");
        }
        event.setState(State.CANCELED);
        return eventRepository.save(event);
    }
    public Event editingEvent(Integer eventId, Event adminEvent){
        Event event= eventRepository.findById(eventId).orElseThrow(()->new RuntimeException("net takogo eventa"));
        setNewFieldsForEditingAdminEvent(adminEvent,event);
        return eventRepository.save(event);
    }
    private void setNewFieldsForEditingAdminEvent(Event adminEvent, Event updatingEvent) {

        updatingEvent.setPaid(adminEvent.isPaid());
        updatingEvent.setRequestModeration(adminEvent.isRequestModeration());
        if (adminEvent.getAnnotation() != null) {
            updatingEvent.setAnnotation(adminEvent.getAnnotation());
        }
        if (adminEvent.getCategory() != null) {
            updatingEvent.setCategory(adminEvent.getCategory());
        }
        if (adminEvent.getDescription() != null) {
            updatingEvent.setDescription(adminEvent.getDescription());
        }
        if (updatingEvent.getEventDate() != null) {
            updatingEvent.setEventDate(adminEvent.getEventDate());
        }
        if (updatingEvent.getLocation() != null) {
            updatingEvent.setLocation(adminEvent.getLocation());
        }
        if (adminEvent.getParticipantLimit() != null) {
            updatingEvent.setParticipantLimit(adminEvent.getParticipantLimit());
        }
        if (adminEvent.getTitle() != null) {
            updatingEvent.setTitle(adminEvent.getTitle());
        }
    }
}
