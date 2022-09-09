package school.hei.haapi.model.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import school.hei.haapi.model.Course;

@Component
public class CourseValidator implements Consumer<Course> {
  public void accept(List<Course> courses) {
    courses.forEach(this);
  }

  @Override
  public void accept(Course o) {
    Set<String> violationAlert = new HashSet<>();
    if (o.getCredits() < 0) {
      violationAlert.add("Credits must be positive or equals to zero");
    }
    if (o.getTotalHours() < 0) {
      violationAlert.add("Total hours must be positive");
    }
  }

}
