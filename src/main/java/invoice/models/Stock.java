package invoice.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock extends BaseDocument {
    private String name;
    private BigDecimal price;
    private double growthRate;
}
