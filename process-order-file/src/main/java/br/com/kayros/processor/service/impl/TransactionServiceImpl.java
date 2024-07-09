package br.com.kayros.processor.service.impl;

import br.com.kayros.model.reponse.Product;
import br.com.kayros.model.reponse.TransactionOrder;
import br.com.kayros.model.reponse.TransactionReport;
import br.com.kayros.processor.entity.Transaction;
import br.com.kayros.processor.repositoty.TransactionRepository;
import br.com.kayros.processor.service.TransactionService;
import br.com.kayros.processor.specifications.TransactionSpecifications;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository repository;

  @Override
  public List<TransactionReport> findAll(Integer pedidoId, LocalDate dataInicial,
      LocalDate dataFinal) {

    Specification<Transaction> spec = Specification.where(null);

    if (pedidoId != null) {
      spec = spec.and(TransactionSpecifications.hasTransactionByOrderId(pedidoId));
    }
    if (dataInicial != null) {
      spec = spec.and(TransactionSpecifications.hasDynamicMovementDateBefore(dataInicial));
    }
    if (dataFinal != null) {
      spec = spec.and(TransactionSpecifications.hasDynamicMovementDateAfter(dataFinal));
    }

    List<Transaction> transactions = repository.findAll(spec);

    List<Map<String, String>> listOfMaps = transactions.stream()
        .map(transaction -> {
          Map<String, String> map = new LinkedHashMap<>();
          map.put("id_user", transaction.getIdUser().toString());
          map.put("name", transaction.getName());
          map.put("id_order", transaction.getOrderId().toString());
          map.put("product_id", transaction.getProductId().toString());
          map.put("values", transaction.getValueProduct().toString());
          map.put("date", transaction.getDateOrder().toString());
          return map;
        })
        .toList();

    return listOfMaps.stream()
        .collect(Collectors.groupingBy(
            entry -> Map.entry(entry.get("id_user"), entry.get("name")),
            Collectors.groupingBy(
                entry -> Map.entry(entry.get("id_order"), entry.get("date")),
                Collectors.mapping(entry -> new Product(
                    Integer.parseInt(entry.get("product_id")),
                    new BigDecimal(entry.get("values").trim())
                ), Collectors.toList())
            )
        ))
        .entrySet().stream()
        .map(userEntry -> {
          List<TransactionOrder> orders = userEntry.getValue().entrySet().stream()
              .map(orderEntry -> new TransactionOrder(
                  Integer.parseInt(orderEntry.getKey().getKey()),
                  orderEntry.getValue().stream().map(Product::value)
                      .reduce(BigDecimal.ZERO, BigDecimal::add),
                  LocalDate.parse(orderEntry.getKey().getValue()),
                  orderEntry.getValue()
              ))
              .toList();

          return new TransactionReport(
              Integer.parseInt(userEntry.getKey().getKey()),
              userEntry.getKey().getValue(),
              orders
          );
        })
        .toList();
  }
}
