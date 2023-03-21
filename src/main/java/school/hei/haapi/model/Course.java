package school.hei.haapi.model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Course implements Serializable {
    @Id
    private Integer id;
    private Integer code;
    private String name;
    private Integer credits;
    private Integer total_hours;
}
