package account.account.kafka;

import account.account.dto.AccountMsgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataProducer {

    private final KafkaTemplate<String, AccountMsgDto> kafkaTemplate;

    @Value(value = "${spring.kafka.topics}")
    private String topic;

    public void sendMessage(AccountMsgDto accountMsgDto) {
        CompletableFuture<SendResult<String, AccountMsgDto>> send = kafkaTemplate.send(topic, accountMsgDto);
        send.whenComplete((data, exception) -> log.info("Data sent to account topic: {}", data.toString()));
    }
}
