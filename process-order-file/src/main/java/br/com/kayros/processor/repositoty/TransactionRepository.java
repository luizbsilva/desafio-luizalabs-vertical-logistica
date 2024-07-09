package br.com.kayros.processor.repositoty;

import br.com.kayros.processor.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {

  List<Transaction> findAllByOrderByIdUserAscOrderIdAsc();

}
