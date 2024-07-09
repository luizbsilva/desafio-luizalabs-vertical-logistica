package br.com.kayros.model.reponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TransactionOrder(
    Integer orderId,
    BigDecimal total,
    LocalDate dateTransaction,
    List<Product> products) {
}
