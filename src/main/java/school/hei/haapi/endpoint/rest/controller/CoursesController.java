package school.hei.haapi.endpoint.rest.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.model.Course;

import java.util.List;

@RestController
@AllArgsConstructor
public class CoursesController {

    @GetMapping("/courses")
    public List<Course> getCourses(

    )
}