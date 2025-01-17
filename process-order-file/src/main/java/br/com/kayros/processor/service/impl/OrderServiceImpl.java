package br.com.kayros.processor.service.impl;

import br.com.kayros.processor.service.OrderService;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final JobLauncher jobLauncher;
  private final Job job;
  private final Path fileStorageLocation;

  public OrderServiceImpl(
      @Qualifier("jobLauncherAsync") JobLauncher jobLauncher,
      Job job,
      @Value("${file.upload-dir}") String fileUploadDir) {
    this.jobLauncher = jobLauncher;
    this.job = job;
    this.fileStorageLocation = Paths.get(fileUploadDir);
  }

  @Override
  public void uploadOrderFile(MultipartFile file) throws Exception {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    Path targetLocation = fileStorageLocation.resolve(fileName);
    file.transferTo(targetLocation);
    var jobParameters = new JobParametersBuilder()
        .addJobParameter("order-", file.getOriginalFilename(), String.class, true)
        .addJobParameter("orderFile", "file:" + targetLocation.toString(), String.class, false)
        .toJobParameters();

    jobLauncher.run(job, jobParameters);

  }
}
