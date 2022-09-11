package school.hei.haapi.endpoint.rest.controller;

import static java.util.stream.Collectors.toUnmodifiableList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.PlaceMapper;
import school.hei.haapi.endpoint.rest.model.Place;
import school.hei.haapi.service.PlaceService;

@AllArgsConstructor
@RestController
public class PlaceController {
  private final PlaceService placeService;
  private final PlaceMapper placeMapper;

  @GetMapping("/places/{id}")
  public Place getByiD(@PathVariable String id) {
    return placeMapper.toRest(placeService.getById(id));
  }

  @GetMapping("/places")
  public List<Place> getPlaces() {
    return placeService.getAll().stream()
        .map(placeMapper::toRest)
        .collect(toUnmodifiableList());
  }

  @PutMapping("/places")
  public List<Place> createOrUpdate(List<Place> toWrite) {
    var saved = placeService.saveAll(toWrite.stream()
        .map(placeMapper::toDomain)
        .collect(toUnmodifiableList()));
    return saved.stream()
        .map(placeMapper::toRest)
        .collect(toUnmodifiableList());
  }
}
