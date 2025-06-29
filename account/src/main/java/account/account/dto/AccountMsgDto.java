package account.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMsgDto {
    String accountNumber;
    String name;
    String email;
    String mobileNumber;
}
