package school.hei.haapi.endpoint.rest.mapper;

import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.Place;

@Component
public class PlaceMapper {

  public Place toRest(school.hei.haapi.model.Place place) {
    var restPlace = new Place();
    restPlace.setId(place.getId());
    restPlace.setPlaceName(place.getPlaceName());
    return restPlace;
  }

  public school.hei.haapi.model.Place toDomain(Place restPlace) {
    return school.hei.haapi.model.Place.builder()
        .id(restPlace.getId())
        .placeName(restPlace.getPlaceName())
        .build();
  }
}
