package iess.pt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class cartProd {
    @JsonProperty("product")
    private Long product;

    @JsonProperty("units")
    private int units;

    @JsonProperty("Price")
    private double Price;
}
