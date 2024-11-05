package invoice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    String status = "Success";
    String message = "Operation completed successfully";
    T data;
    Pagination pagination;
}
