package iess.pt.dataAccess;

import com.mysql.cj.log.Log;
import iess.pt.entity.Product;
import iess.pt.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface productRepository extends JpaRepository<Product, Long> {
    public Product findByName(String name);

    @Query(value = "SELECT * FROM products WHERE id = :id", nativeQuery = true)
    Product searchByID(Long id);

    @Query(value = "SELECT * FROM products WHERE Name = :name AND Provider = :provider", nativeQuery = true)
    Product searchByNameAndProvider(@Param("name") String name, @Param("provider") String provider);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET Curr_quantity = Curr_quantity - :units WHERE id = :id", nativeQuery = true)
    void sellProduct(@Param("units") int units, @Param("id") Long id);

    @Query(value = "SELECT Price FROM products WHERE  id = :id", nativeQuery = true)
    double getPrice( @Param("id") Long id);

    @Query(value = "SELECT Name FROM products WHERE  id = :id", nativeQuery = true)
    String getProductName( @Param("id") Long id);

    /*

    @Query(value = "SELECT price, curr_quantity FROM products WHERE id = :id", nativeQuery = true)
     getQuantityAndPrice(@Param("id") Long id);

     */

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET Name = :name, Category = :category, Curr_quantity = :currQuantity, Price = :price, Provider = :provider WHERE id = :id", nativeQuery = true)
    void updateProductById(@Param("id") Long id, @Param("name") String name, @Param("category") String category, @Param("currQuantity") int currQuantity, @Param("price") double price, @Param("provider") String provider);

    @Transactional
    @Modifying
   // @Query(value = "UPDATE products SET  Curr_quantity = 20 WHERE id = ", nativeQuery = true)
    @Query(value = "UPDATE products SET  Curr_quantity = :currQuantity WHERE id = :id", nativeQuery = true)
    void updateProductByName(@Param("id") long id,@Param("currQuantity") int currQuantity);


}