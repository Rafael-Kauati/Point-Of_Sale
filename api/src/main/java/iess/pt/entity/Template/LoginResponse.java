package iess.pt.entity.Template;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private String Email;

    private String password;

    private String status;
}
