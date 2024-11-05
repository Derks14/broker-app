package invoice.models.requests;

import invoice.models.Stock;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class StocksRequest {
    @NotBlank
    String stockId;
    @NotBlank
    int quantity;
}
