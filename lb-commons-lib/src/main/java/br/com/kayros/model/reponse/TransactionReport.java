package br.com.kayros.model.reponse;

import java.util.List;

public record TransactionReport(
    Integer userId,
    String name,
    List<TransactionOrder> orders) {
}
