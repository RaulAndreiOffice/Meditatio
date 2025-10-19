package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Getter
@Setter
public class Assignments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assid;

    private String asstitle;

    private String description;


    @Column(name ="createdat")
    private LocalDateTime createdat;
}
