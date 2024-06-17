package iess.pt.dataAccess;

import iess.pt.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Sale, Long>
{
    @Query(value = "select * from sale where emp_id = :id", nativeQuery = true)
    public List<Sale> getAllByEmp(@Param("id") Long id);
}
