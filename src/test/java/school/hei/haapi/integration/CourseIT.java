package school.hei.haapi.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import school.hei.haapi.SentryConf;
import school.hei.haapi.endpoint.rest.api.TeachingApi;
import school.hei.haapi.endpoint.rest.client.ApiClient;
import school.hei.haapi.endpoint.rest.client.ApiException;
import school.hei.haapi.endpoint.rest.model.Course;
import school.hei.haapi.endpoint.rest.model.Group;
import school.hei.haapi.endpoint.rest.security.cognito.CognitoComponent;
import school.hei.haapi.integration.conf.AbstractContextInitializer;
import school.hei.haapi.integration.conf.TestUtils;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static school.hei.haapi.integration.conf.TestUtils.BAD_TOKEN;
import static school.hei.haapi.integration.conf.TestUtils.GROUP1_ID;
import static school.hei.haapi.integration.conf.TestUtils.MANAGER1_TOKEN;
import static school.hei.haapi.integration.conf.TestUtils.STUDENT1_TOKEN;
import static school.hei.haapi.integration.conf.TestUtils.TEACHER1_TOKEN;
import static school.hei.haapi.integration.conf.TestUtils.anAvailableRandomPort;
import static school.hei.haapi.integration.conf.TestUtils.assertThrowsForbiddenException;
import static school.hei.haapi.integration.conf.TestUtils.setUpCognito;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = CourseIT.ContextInitializer.class)
@AutoConfigureMockMvc
public class CourseIT {
  @MockBean
  private SentryConf sentryConf;

  @MockBean
  private CognitoComponent cognitoComponentMock;

  private static ApiClient anApiClient(String token) {
    return TestUtils.anApiClient(token, GroupIT.ContextInitializer.SERVER_PORT);
  }

  public static Course course1() {
    Course course = new Course();
    course.setId("course1_id");
    course.setName("course1_name");
    course.setCredits(10000);
    course.setRef("CRS21001");
    course.setTotalHours(10);
    return course;
  }

  public static Course course2() {
    Course course = new Course();
    course.setId("course2_id");
    course.setName("course2_name");
    course.setCredits(20000);
    course.setRef("CRS21002");
    course.setTotalHours(12);
    return course;
  }

  public static Course creatableCourse() {
    Course course = new Course();
    course.setName("course1_name");
    course.setCredits(10000);
    course.setRef("CRS21-" + randomUUID());
    course.setTotalHours(10);
    return course;
  }

  @BeforeEach
  public void setUp() {
    setUpCognito(cognitoComponentMock);
  }

  @Test
  void badtoken_read_ko() {
    ApiClient anonymousClient = anApiClient(BAD_TOKEN);
    TeachingApi api = new TeachingApi(anonymousClient);
    assertThrowsForbiddenException(api::getCourses);
  }

  @Test
  void badtoken_write_ko() {
    ApiClient anonymousClient = anApiClient(BAD_TOKEN);

    TeachingApi api = new TeachingApi(anonymousClient);
    assertThrowsForbiddenException(() -> api.createOrUpdateCourses(creatableCourse()));
  }

  @Test
  void student_read_ok() throws ApiException {
    ApiClient student1Client = anApiClient(STUDENT1_TOKEN);

    TeachingApi api = new TeachingApi(student1Client);
    Group actual1 = api.getGroupById(GROUP1_ID);
    List<Course> actualGroups = api.getCourses();

    assertEquals(course1(), actual1);
    assertTrue(actualGroups.contains(course1()));
    assertTrue(actualGroups.contains(course2()));
  }

  @Test
  void student_write_ko() {
    ApiClient student1Client = anApiClient(STUDENT1_TOKEN);

    TeachingApi api = new TeachingApi(student1Client);
    assertThrowsForbiddenException(() -> api.createOrUpdateCourses(creatableCourse()));
  }

  @Test
  void teacher_write_ko() {
    ApiClient teacher1Client = anApiClient(TEACHER1_TOKEN);

    TeachingApi api = new TeachingApi(teacher1Client);
    assertThrowsForbiddenException(() -> api.createOrUpdateCourses(creatableCourse()));
  }

  @Test
  void manager_write_create_ok() throws ApiException {
    ApiClient manager1Client = anApiClient(MANAGER1_TOKEN);
    Course coursToCreate = creatableCourse();
    TeachingApi api = new TeachingApi(manager1Client);
    Course createdCourse = api.createOrUpdateCourses(coursToCreate);
    assertEquals(coursToCreate, createdCourse);
  }

  @Test
  void manager_write_update_ok() {
    ApiClient manager1Client = anApiClient(MANAGER1_TOKEN);
    TeachingApi api = new TeachingApi(manager1Client);
    try {
      Course courseToUpdate = api.createOrUpdateCourses(creatableCourse());
      courseToUpdate.setName("course_to_update");
      Course updated = api.createOrUpdateCourses(courseToUpdate);
      assertEquals(updated, courseToUpdate);
    } catch (ApiException e) {
      throw new RuntimeException(e);
    }
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}
