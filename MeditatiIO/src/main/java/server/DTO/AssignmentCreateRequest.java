package server.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AssignmentCreateRequest {
    private String title;
    private String description;
    private Long studentId;
    private MultipartFile file;
}
