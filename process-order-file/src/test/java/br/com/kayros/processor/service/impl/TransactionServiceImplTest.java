package br.com.kayros.processor.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import br.com.kayros.model.reponse.TransactionOrder;
import br.com.kayros.model.reponse.TransactionReport;
import br.com.kayros.processor.entity.Transaction;
import br.com.kayros.processor.repositoty.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
  @InjectMocks
  private TransactionServiceImpl transactionService;

  @Mock
  private TransactionRepository transactionRepository;

  @Test
  public void testGetTotaisTransactionByUserId() {

    final String nameUserA = "Palmer Prosacco", nameUserB = "Bobbie Batz";
    var transaction1 = new Transaction(
        1L, 70, nameUserA, 1, 1, BigDecimal.valueOf(100.00), LocalDate.now());
    var transaction2 = new Transaction(
        2L, 75, nameUserB, 2, 1,  BigDecimal.valueOf(50.00), LocalDate.now());
    var transaction3 = new Transaction(
        3L, 70, nameUserA, 1, 3, BigDecimal.valueOf(75.00), LocalDate.now());

    List<Transaction> mockTransactions = List.of(transaction1, transaction2, transaction3);


    when(transactionRepository.findAll(any(Specification.class))).thenReturn(mockTransactions);

    List<TransactionReport> reports = transactionService.findAll(anyInt(), LocalDate.now(), LocalDate.now());

    // Assert
    assertEquals(2, reports.size());

    reports.forEach(report -> {
      if (report.name().equals(nameUserA)) {
        var totalUserA = report.orders()
            .stream()
                .filter(order -> order.orderId().equals(1)).findFirst().get().total();
        assertEquals(1, report.orders().size());
        assertEquals(BigDecimal.valueOf(175.00), totalUserA);
      } else if (report.name().equals(nameUserB)) {
        var totalUserB = report.orders()
            .stream()
            .filter(order -> order.orderId().equals(2)).findFirst().get().total();

        assertEquals(1, report.orders().size());
        assertEquals(BigDecimal.valueOf(50.00), totalUserB);
      }
    });
  }

}
