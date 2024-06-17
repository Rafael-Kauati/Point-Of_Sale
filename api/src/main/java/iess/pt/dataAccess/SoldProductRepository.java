package iess.pt.dataAccess;

import iess.pt.entity.SoldProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoldProductRepository  extends JpaRepository<SoldProduct, Long>
{
}
