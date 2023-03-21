package school.hei.haapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import school.hei.haapi.model.Course;

import javax.servlet.ServletException;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class CourseIT {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();
    private static Course expectedCourse() {
        return Course.builder()
                .id(2)
                .name("PROG3")
                .code(1)
                .credits(6)
                .total_hours(60)
                .build();
    }

    @Test
    void read_course_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/course"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Course actual = objectMapper.readValue(
                response.getContentAsString(), Course.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedCourse(), actual);
    }
    @Test
    void create_course_ok() throws Exception {
        Course toCreate = Course.builder()
                .id(2)
                .name("SYS1")
                .code(2)
                .credits(4)
                .total_hours(40)
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(post("/course")
                        .content(objectMapper.writeValueAsString(List.of(toCreate)))
                        .contentType("application/json")
                        .accept("application/json"))
                .andReturn()
                .getResponse();
        List<Course> actual = convertFromHttpResponse(response);

        assertEquals(2, actual.size());
        assertEquals(toCreate, actual.get(0));
    }
    @Test
    void read_course_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/course/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        school.hei.haapi.endpoint.rest.model.Course actual = objectMapper.readValue(
                response.getContentAsString(), school.hei.haapi.endpoint.rest.model.Course.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedCourse(), actual);
    }
    @Test
    void read_course_by_id_ko() throws Exception {
        String courseId = "5";
        String errorMessage = "Request processing failed: Course#"+courseId+" not found.";
        assertThrowsExceptionMessage(errorMessage, ServletException.class,
                ()->mockMvc.perform(get("/course/"+courseId)).andExpect(status().isNotFound())
        );
    }
    private List<Course> convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType playerListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Course.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                playerListType);
    }
    public static void assertThrowsExceptionMessage(String message, Class exceptionClass, Executable executable) {
        Throwable exception = assertThrows(exceptionClass, executable);
        assertEquals(message, exception.getMessage());
    }
}
