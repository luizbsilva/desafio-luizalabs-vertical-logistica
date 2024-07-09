package br.com.kayros.model.request;

public record OrderTransaction(
    Integer idUser,
    String name,
    Integer orderId,
    Integer productId,
    String valueProduct,
    String dateOrder) {

}
