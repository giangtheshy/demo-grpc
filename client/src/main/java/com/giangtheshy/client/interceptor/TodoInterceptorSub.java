package com.giangtheshy.client.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoInterceptorSub implements ClientInterceptor {
  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    log.info("interceptorSub is calling...");
    return channel.newCall(methodDescriptor, callOptions);
  }
}
