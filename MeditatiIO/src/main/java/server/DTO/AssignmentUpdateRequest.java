package server.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentUpdateRequest {
    private String title;
    private String description;
    private Long studentId;
}
