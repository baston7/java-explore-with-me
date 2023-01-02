package ru.practicum.explore.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequesterId(int requesterId);

    List<Request> findAllByRequesterIdAndEvent_IdOrEvent_Initiator_IdAndEvent_Id(int requesterId,
                                                                                 int eventId, int initiatorId,
                                                                                 int duplicateEventId);

    List<Request> findAllByEventIdAndEventInitiatorId(int eventId, int initiatorId);

    Optional<Request> findByIdAndEventIdAndEventInitiatorIdAndEventRequestModeration(int reqId, int eventId,
                                                                                     int userId, boolean moderation);

    List<Request> findAllByEventIdAndEventInitiatorIdAndEventRequestModeration(int eventId, int userId,
                                                                               boolean moderation);
}