package invoice.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class StockRequest {
    @NotBlank
    String name;

    @NotBlank
    BigDecimal price;

    @NotBlank
    double growthRate;
}
