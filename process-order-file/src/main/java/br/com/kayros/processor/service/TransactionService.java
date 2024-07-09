package br.com.kayros.processor.service;

import br.com.kayros.model.reponse.TransactionReport;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

  List<TransactionReport> findAll(Integer pedidoId, LocalDate dataInicial, LocalDate dataFinal);
}
