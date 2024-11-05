package invoice.models.requests;

import invoice.models.Stocks;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.ArrayList;

@Value
@AllArgsConstructor
public class InvoiceRequest {

    @NotEmpty
    ArrayList<Stocks> stocks;

    @NonNull
    BigDecimal cost;

}
