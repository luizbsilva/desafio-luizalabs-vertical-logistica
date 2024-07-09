package br.com.kayros.processor.job;


import br.com.kayros.model.request.OrderTransaction;
import br.com.kayros.processor.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  private final PlatformTransactionManager transactionManager;
  private final JobRepository jobRepository;

  public BatchConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
    this.transactionManager = transactionManager;
    this.jobRepository = jobRepository;
  }

  @Bean
  Job job(Step step) {
    return new JobBuilder("job", jobRepository)
        .start(step)
        .build();
  }

  @Bean
  Step step(FlatFileItemReader<OrderTransaction> reader,
      ItemProcessor<OrderTransaction, Transaction> processor,
      ItemWriter<Transaction> writer) {
    return new StepBuilder("step", jobRepository)
        .<OrderTransaction, Transaction>chunk(1000, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @StepScope
  @Bean
  FlatFileItemReader<OrderTransaction> reader(
      @Value("#{jobParameters['orderFile']}") Resource resource) {
    return new FlatFileItemReaderBuilder<OrderTransaction>()
        .name("reader")
        .resource(resource)
        .fixedLength()
        .columns( new Range(1, 10),
            new Range(11, 55),
            new Range(56, 65),
            new Range(66, 75),
            new Range(76, 87),
            new Range(88, 95))
        .names("idUser", "name", "orderId", "productId", "valueProduct", "dateOrder")
        .targetType(OrderTransaction.class)
        .build();
  }


  @Bean
  ItemProcessor<OrderTransaction, Transaction> processor() {
    return item -> {
      var valorNormalizado = new BigDecimal(item.valueProduct().trim());
      var nomeNormalizado = item.name().trim();
      return new Transaction(
          null, item.idUser(), nomeNormalizado,
      item.orderId(), item.productId(), valorNormalizado,
          convertToLocalDate(item.dateOrder()));

    };
  }

  public static LocalDate convertToLocalDate(String dateStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    try {
      return LocalDate.parse(dateStr, formatter);
    } catch (DateTimeParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Bean
  JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Transaction>()
        .dataSource(dataSource)
        .sql("""
              INSERT INTO transaction (
                id_user, name, id_order, product_id, value_product,
                date_order
              ) VALUES (
                :idUser, :name, :orderId, :productId, :valueProduct,
                :dateOrder
              )
            """)
        .beanMapped()
        .build();
  }

  @Bean
  JobLauncher jobLauncherAsync(JobRepository jobRepository) throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

}
