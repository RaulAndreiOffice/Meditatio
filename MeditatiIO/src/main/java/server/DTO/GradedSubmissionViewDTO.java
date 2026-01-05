package server.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradedSubmissionViewDTO {
    private String assignmentTitle;
    private Integer grade;
    private String studentSubmissionUrl; // Imaginea originală a studentului
    private String professorDrawingData; // Desenul JSON al profesorului
}
