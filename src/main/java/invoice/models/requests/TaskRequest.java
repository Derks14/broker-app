package invoice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TaskRequest {
    private String name;
}
