package br.com.kayros.processor.controler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.kayros.model.exceptions.StandardError;
import br.com.kayros.model.reponse.TransactionReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "OrderController", description = "Controller responsible for order operations")
@RequestMapping("/api/order")
public interface OrderController {

  @Operation(summary = "Upload Order")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Upload order Success"),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
      )})
  @PostMapping("upload")
  ResponseEntity<String> save(@RequestParam("file") MultipartFile file) throws Exception;

  @Operation(summary = "Upload Order")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Upload order Success"),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
      )})
  @GetMapping()
  ResponseEntity<List<TransactionReport>> listAll( @RequestParam(required = false) Integer pedidoId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal);


}