package account.account.service;

import account.account.dto.CustomerDto;

public interface AccountService {

  void createCustomer(CustomerDto customerDto);

  CustomerDto getCustomerByPhoneNumber(String phoneNumber);

}
