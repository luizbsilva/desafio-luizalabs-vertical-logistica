package br.com.kayros.model.reponse;

import java.math.BigDecimal;

public record Product(
    Integer productId,
    BigDecimal value) {

}
