package account.account.mapper;

import account.account.dto.AccountsDto;
import account.account.dto.CustomerDto;
import account.account.entity.Account;
import account.account.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends AbtractMapper<Customer, CustomerDto> {

  @Override
  public Class<CustomerDto> getDtoClass() {
    return CustomerDto.class;
  }

  @Override
  public Class<Customer> getEntityClass() {
    return Customer.class;
  }
}
