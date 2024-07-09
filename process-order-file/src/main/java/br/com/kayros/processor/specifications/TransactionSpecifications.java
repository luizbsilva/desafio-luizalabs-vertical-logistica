package br.com.kayros.processor.specifications;

import br.com.kayros.processor.entity.Transaction;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecifications {

  public static final String SHIPPING_CAPACITY = "shippingCapacity";

  public static Specification<Transaction> hasDynamicCapacitySlot(List<String> slots) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.in(root.get("slot")).value(slots);
  }

  public static Specification<Transaction> hasTransactionByOrderId(Integer orderId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("id_order"), orderId);
  }

  public static Specification<Transaction> hasDynamicMovementDate(LocalDate initialDate,
      LocalDate finalDate) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.between(root.get("date_order"), initialDate, finalDate);
  }

  public static Specification<Transaction> hasDynamicMovementDateBefore(LocalDate finalDate) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("date_order"), finalDate);
  }

  public static Specification<Transaction> hasDynamicMovementDateAfter(LocalDate initialDate) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("date_order"), initialDate);
  }
}
