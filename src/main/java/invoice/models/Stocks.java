package invoice.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stocks extends BaseDocument {

    @DocumentReference
    @NonNull
    private Stock stock;

    @NotNull
    private int quantity;
}
