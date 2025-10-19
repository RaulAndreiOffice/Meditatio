package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "test")
@Getter
@Setter

public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer testid;

    private String testtitle;

    private String description;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdat;

}
