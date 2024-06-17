package iess.pt.dataAccess;

import java.util.List;
import java.util.Optional;

import iess.pt.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = "SELECT * FROM token WHERE user_email = :email", nativeQuery = true)
    List<Token> findAllValidTokenByUser(@Param("email") String email);


    Optional<Token> findByToken(String token);
}