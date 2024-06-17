package iess.pt.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Builder
@Data
@NoArgsConstructor
@Table(name = "SoldProduct")
@Entity(name = "SoldProduct")
@AllArgsConstructor
public class SoldProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "sale_id", nullable = false, unique = false)
    private Long sale_id;

    @Column(name = "Name", nullable = false, unique = false)
    @JsonProperty("product")
    private String name;

    @Column(name = "Category", nullable = false)
    @JsonProperty("category")
    private String category;

    @Column(name = "units", nullable = false)
    @JsonProperty("units")
    private int units = 1;


    @Column(name = "Price", nullable = false)
    @JsonProperty("price")
    private double price;



}
