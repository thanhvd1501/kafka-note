package card.card.service.impl;

import card.card.entity.Cards;
import card.card.exception.ResourceNotFoundException;
import card.card.mapper.CardMapper;
import card.card.repository.CardRepository;
import card.card.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;


  private final CardMapper cardMapper;


  @Override
  @Transactional
  public void createCard(String mobileNumber) {
    Optional<Cards> optionalCards= cardRepository.findByMobileNumber(mobileNumber);
    if(optionalCards.isPresent()){
      throw new RuntimeException("Card already registered with given mobileNumber "+mobileNumber);
    }
    cardRepository.save(createNewCard(mobileNumber));
  }

  private Cards createNewCard(String mobileNumber) {
    Cards newCard = new Cards();
    long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
    newCard.setCardNumber(Long.toString(randomCardNumber));
    newCard.setMobileNumber(mobileNumber);
    newCard.setCardType("Credit Card");
    newCard.setTotalLimit(1_000_000);
    newCard.setAmountUsed(0);
    newCard.setAvailableAmount(1_000_000);
    return newCard;
  }

  @Override
  @Transactional
  public boolean deleteCard(String mobileNumber) {
    Cards cards = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
            () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
    );
    cardRepository.deleteById(cards.getCardId());
    return true;
  }
}
