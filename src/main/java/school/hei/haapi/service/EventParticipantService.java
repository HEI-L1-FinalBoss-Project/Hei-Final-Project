package school.hei.haapi.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.EventParticipant;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.repository.EventParticipantRepository;

@Service
@AllArgsConstructor
public class EventParticipantService {
  private final EventParticipantRepository repository;

  public List<EventParticipant> getAll(PageFromOne page,
                                       BoundedPageSize pageSize, String eventId, String ref,
                                       String status) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return repository.findByEvent_IdContainingIgnoreCaseAndIdContainingIgnoreCaseAndStatus(eventId, ref,
        EventParticipant.StatusEnum.valueOf(status), pageable);
  }

  public EventParticipant getById(String eventId, String participantId) {
    return repository.getById(participantId);
  }

  public List<EventParticipant> saveAll(List<EventParticipant> toCreate) {
    return repository.saveAll(toCreate);
  }


  public void checkAttendance(byte[] toCompare) {
    Float similarityThreshold = 80F;
  }
}
