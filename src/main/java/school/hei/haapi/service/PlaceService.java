package school.hei.haapi.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.Place;
import school.hei.haapi.repository.PlaceRepository;

@AllArgsConstructor
@Service
public class PlaceService {
  private final PlaceRepository repository;

  public Place getById(String id) {
    return repository.getById(id);
  }

  public List<Place> getAll() {
    return repository.findAll();
  }

  public List<Place> saveAll(List<Place> places) {
    return repository.saveAll(places);
  }
}
