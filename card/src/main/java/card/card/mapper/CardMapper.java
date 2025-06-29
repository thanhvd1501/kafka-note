package card.card.mapper;

import card.card.dto.CardDto;
import org.springframework.stereotype.Component;

import javax.smartcardio.Card;

@Component
public class CardMapper extends AbtractMapper<Card, CardDto>{
    @Override
    public Class<CardDto> getDtoClass() {
        return CardDto.class;
    }

    @Override
    public Class<Card> getEntityClass() {
        return Card.class;
    }
}
