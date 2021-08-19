package com.giangtheshy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

  public static void main(String[] args)  {
    SpringApplication.run(ServerApplication.class, args);
    //    Server server = ServerBuilder.forPort(9091).addService(new PingPongServiceImpl()).build();
    //    server.start();
    //    server.awaitTermination();
  }
}
