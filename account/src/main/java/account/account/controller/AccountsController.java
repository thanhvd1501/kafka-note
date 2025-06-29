package account.account.controller;

import account.account.dto.CustomerDto;
import account.account.dto.ResponseDto;
import account.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "Accounts REST APIs",
    description = "CRUD APIs in EasyBank to create, update, fetch, and delete account details"
)
@RestController
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AccountsController {

  private final AccountService accountService;

  @Operation(
      summary = "Create Account REST API",
      description = "REST API to create new account inside EasyBank"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Account created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {

    accountService.createCustomer(customerDto);
    return ResponseEntity.ok(new ResponseDto("Creat account successfully", HttpStatus.OK.toString()));
  }

  @GetMapping("/{phoneNumber}/account")
  public ResponseEntity<CustomerDto> getAccountByPhone(@PathVariable(name = "phoneNumber") String phoneNumber) {
    return ResponseEntity.status(HttpStatus.OK).body(accountService.getCustomerByPhoneNumber(phoneNumber));
  }

}
