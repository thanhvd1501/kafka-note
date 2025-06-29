package account.account.kafka;

import account.account.dto.AccountMsgDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataConsumer {

    @KafkaListener(topics = "#{'${spring.kafka.topics}'}", groupId = "#{'${spring.kafka.group-id}'}")
    public void listen(@Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       AccountMsgDto accountMsgDto) {
        System.out.println("Received partition: " + partition);
        log.info("Received partition: {}, account: {}", partition, accountMsgDto);
    }
}
