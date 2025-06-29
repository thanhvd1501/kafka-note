package card.card.repository;

import card.card.entity.Cards;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Cards, Long> {

  Optional<Cards> findByMobileNumber(String mobileNumber);
}
