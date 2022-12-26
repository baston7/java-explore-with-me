package ru.practicum.explore.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findByRequesterIdAndEventId(int requester_id, int eventId);

    List<Request> findAllByRequesterId(int requester_id);

    List<Request> findAllByRequesterIdAndEvent_IdOrEvent_Initiator_IdAndEvent_Id(int requester_id, int eventId, int initiator_id, int duplicateEventId);

    List<Request> findAllByEventIdAndEventInitiatorId(int eventId, int initiatorId);

    Optional<Request> findByIdAndEventIdAndEventInitiatorIdAndEventRequestModeration(int reqId, int eventId, int userId, boolean moderation);
    List <Request> findAllByEventIdAndEventInitiatorIdAndEventRequestModeration(int eventId, int userId, boolean moderation);
}