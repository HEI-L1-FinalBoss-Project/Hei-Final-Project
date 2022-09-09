package school.hei.haapi.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.Course;
import school.hei.haapi.repository.CourseRepository;

@Service
@AllArgsConstructor
public class CourseService {
  private final CourseRepository courseRepository;

  public Course getById(String id) {
    return courseRepository.getById(id);
  }

  public List<Course> getAll() {
    return courseRepository.findAll();
  }

  public Course saveCourse(Course courseToSave) {
    return courseRepository.save(courseToSave);
  }
}
