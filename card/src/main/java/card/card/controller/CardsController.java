package card.card.controller;

import card.card.dto.ErrorResponseDto;
import card.card.dto.ResponseDto;
import card.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "Accounts REST APIs",
    description = "CRUD APIs in EasyBank to create, update, fetch, and delete account details"
)
@RestController
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CardsController {

  private final CardService cardService;

  @Operation(
          summary = "Create Card REST API",
          description = "REST API to create new Card inside EazyBank"
  )
  @ApiResponses({
          @ApiResponse(
                  responseCode = "201",
                  description = "HTTP Status CREATED"
          ),
          @ApiResponse(
                  responseCode = "500",
                  description = "HTTP Status Internal Server Error",
                  content = @Content(
                          schema = @Schema(implementation = ErrorResponseDto.class)
                  )
          )
  }
  )
  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createCard(@RequestParam String mobileNumber) {
    cardService.createCard(mobileNumber);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ResponseDto("201", "Card created successfully"));
  }
}
