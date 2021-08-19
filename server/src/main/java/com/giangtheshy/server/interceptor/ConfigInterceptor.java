package com.giangtheshy.server.interceptor;

import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigInterceptor {
  private final TodoInterceptor todoInterceptor = new TodoInterceptor();

  @Bean
  public GlobalServerInterceptorConfigurer gsic() {
    return registry -> registry.add(todoInterceptor);
  }
}
