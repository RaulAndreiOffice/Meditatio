package server.DTO;


import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class JwtAuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Integer userId; // ID-ul de care ai nevoie
    private String username;
    private List<String> roles;


    public JwtAuthResponse(String token, Integer userId, String username,List<String> roles) {
            this.token = token;
            this.userId = userId;
            this.username = username;
            this.roles = roles;
    }

}
