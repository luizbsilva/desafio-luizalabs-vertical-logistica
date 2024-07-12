package br.com.kayros.processor.controler.imp;

import br.com.kayros.model.reponse.TransactionReport;
import br.com.kayros.processor.controler.OrderController;
import br.com.kayros.processor.service.OrderService;
import br.com.kayros.processor.service.TransactionService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class OrderControllerImp implements OrderController {

  private final OrderService orderService;
  private final TransactionService transactionService;

  @Override
  public ResponseEntity<String> save(MultipartFile file) throws Exception {
    orderService.uploadOrderFile(file);
    return ResponseEntity.ok().body("Processamento Iniciado");
  }

  @Override
  public ResponseEntity<List<TransactionReport>> listAll(Integer orderId, LocalDate initialDate, LocalDate finalDate) {
    return ResponseEntity.ok().body(transactionService.findAll(orderId, initialDate, finalDate));
  }

}
