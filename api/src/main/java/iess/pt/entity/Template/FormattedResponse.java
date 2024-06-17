package iess.pt.entity.Template;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormattedResponse {

    private String request;

    private String response;

    private Object object;

    private HttpStatus status;

    public ResponseEntity<FormattedResponse> response() {
        return new ResponseEntity<>(this, status);
    }
}
