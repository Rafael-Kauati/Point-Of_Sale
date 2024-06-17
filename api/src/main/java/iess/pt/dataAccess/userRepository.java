package iess.pt.dataAccess;

import iess.pt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface userRepository extends JpaRepository<User, Long>
{
    @Query(value = "select * from users where Name = :name",nativeQuery = true)
    User findByName(String name);


    @Query(value = "select id from users",nativeQuery = true)
    List<Long> getEmpsId();

    @Query(value = "select name from users where id = :id",nativeQuery = true)
    String getEmpsName(long id);


    @Query(value = "select * from users where Email = :email",nativeQuery = true)
    User findByEmail(String email);


    @Query(value = "select total_price from sale where emp_id = :id",nativeQuery = true)
    List<Integer> getAllEmployeesInvoices(@Param("id") Long id);


}
