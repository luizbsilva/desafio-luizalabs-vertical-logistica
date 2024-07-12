package br.com.kayros.processor.service;

import org.springframework.web.multipart.MultipartFile;

public interface OrderService {

  void uploadOrderFile(MultipartFile file) throws Exception;

}
