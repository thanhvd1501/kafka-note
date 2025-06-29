package account.account.mapper;

import account.account.dto.AccountsDto;
import account.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper extends AbtractMapper<Account, AccountsDto> {

  @Override
  public Class<AccountsDto> getDtoClass() {
    return AccountsDto.class;
  }

  @Override
  public Class<Account> getEntityClass() {
    return Account.class;
  }
}
