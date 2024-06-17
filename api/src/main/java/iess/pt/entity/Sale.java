package iess.pt.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@Table(name = "Sale")
@Entity(name = "Sale")
@AllArgsConstructor
public class Sale
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "date", nullable = false, unique = false)
    private LocalDateTime date;

    @Column(name = "total_price", nullable = false, unique = false)
    private double total_price;

    @Column(name = "total_units", nullable = false, unique = false)
    private int total_units;


    @Column(name = "emp_id", nullable = false, unique = false)
    private Long emp_id;
}
