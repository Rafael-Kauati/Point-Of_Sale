package iess.pt.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "Name", nullable = false, unique = true)
    @JsonProperty("product")
    private String name;

    @Column(name = "Category", nullable = false)
    @JsonProperty("category")
    private String category;

    @Column(name = "Curr_quantity", nullable = false)
    @JsonProperty("curr_quantity")
    private int curr_quantity =10;


    @Column(name = "Price", nullable = false)
    @JsonProperty("price")
    private double price;


    @Column(name = "Provider", nullable = false)
    @JsonProperty("provider")
    private String provider;
}
