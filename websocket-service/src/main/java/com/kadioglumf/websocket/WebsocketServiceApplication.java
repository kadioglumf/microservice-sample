package com.kadioglumf.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class WebsocketServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebsocketServiceApplication.class, args);
  }
}

@RestController
class HealthCheckController {

  /**
   * Added to return 200 OK on default path of "/" Kubernetes Ingress, GKE health checks from
   * default path of "/" Also allowed on security layer Do not remove It's ok to return any type of
   * response with status 200
   *
   * @return void 200 OK
   */
  @RequestMapping("/")
  Object defaultPath() {
    return "";
  }
}
