package account.account.repository;

import account.account.dto.CustomerDto;
import account.account.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

  Customer findCustomerByMobileNumber(String mobileNumber);
}
