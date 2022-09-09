package school.hei.haapi.endpoint.rest.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.CourseMapper;
import school.hei.haapi.endpoint.rest.model.Course;
import school.hei.haapi.service.CourseService;


@RestController
@AllArgsConstructor
public class CourseController {
  private final CourseService courseService;
  private final CourseMapper courseMapper;

  @GetMapping("/courses/{id}")
  public Course getById(@RequestParam String idCourse) {
    return courseMapper.toRest(courseService.getById(idCourse));
  }

  @GetMapping("/courses")
  public List<Course> showAll() {
    return courseService.getAll().stream()
        .map(courseMapper::toRest)
        .collect(Collectors.toUnmodifiableList());
  }

  @PutMapping("/courses")
  public Course createOrUpdateCourse(@RequestBody Course saveIt) {
    school.hei.haapi.model.Course course =
        courseService.saveCourse(courseMapper.toDomain(saveIt));
    return courseMapper.toRest(course);
  }
}
