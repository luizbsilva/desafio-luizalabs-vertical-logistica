package br.com.kayros.processor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

  @Id
  private Long id;
  @Column(name = "id_user")
  private Integer idUser;
  @Column(name = "name")
  private String name;
  @Column(name = "id_order")
  private Integer orderId;
  @Column(name = "product_id")
  private Integer productId;
  @Column(name = "value_product")
  private BigDecimal valueProduct;
  @Column(name = "date_order")
  private LocalDate dateOrder;

}
