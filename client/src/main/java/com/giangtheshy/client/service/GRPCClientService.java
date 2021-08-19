package com.giangtheshy.client.service;

import com.giangtheshy.client.interceptor.TodoInterceptor;
import com.giangtheshy.client.interceptor.TodoInterceptorSub;
import com.giangtheshy.client.dto.TodoRequest;
import com.giangtheshy.client.dto.TodoResponse;
import com.giangtheshy.proto.Params;
import com.giangtheshy.proto.Todo;
import com.giangtheshy.proto.TodoServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class GRPCClientService {
  @GrpcClient("client-service")
  private TodoServiceGrpc.TodoServiceBlockingStub stub;

  @GrpcClient("client-service")
  private TodoServiceGrpc.TodoServiceStub asyncStub;

  private final TodoInterceptor todoInterceptor = new TodoInterceptor();
  private final TodoInterceptorSub todoInterceptorSub = new TodoInterceptorSub();

  public TodoResponse addTodo(TodoRequest request) throws Exception {
    log.info("add todo");

    Todo todo =
        this.stub.withInterceptors(todoInterceptor, todoInterceptorSub).addOne(buildTodo(request));
    try {
      TodoResponse response = buildTodoResponse(todo);
      System.out.println(response);
      return response;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }

  public List<TodoResponse> getAll() {
    Params params = Params.newBuilder().setId(0).setPage(0).setSort(false).build();
    Iterator<Todo> listTodo = this.stub.getAll(params);
    List<TodoResponse> listRes = new ArrayList<>();
    listTodo.forEachRemaining(
        todo -> {
          TodoResponse response = buildTodoResponse(todo);
          listRes.add(response);
        });
    return listRes;
  }

  public String addMany(List<TodoRequest> requests) {
    List<TodoResponse> responses = new ArrayList<>();

    StreamObserver<Todo> requestObserver =
        this.asyncStub.addMany(
            new StreamObserver<Todo>() {
              @Override
              public void onNext(Todo todo) {
                log.info("add many todo : {}", todo);
                responses.add(buildTodoResponse(todo));
              }

              @Override
              public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
              }

              @Override
              public void onCompleted() {
                log.info("finish add todo");
              }
            });
    requests.forEach(
        todo -> {
          requestObserver.onNext(buildTodo(todo));
        });
    System.out.println("service res" + responses);
    requestObserver.onCompleted();

    return "Add todo successfully!";
  }

  public TodoResponse getOne(Params request) {
    Todo todo = this.stub.getOne(request);
    return buildTodoResponse(todo);
  }

  public TodoResponse removeOne(Params request) {
    Todo todo = this.stub.removeOne(request);
    return buildTodoResponse(todo);
  }

  public String removeMany(List<Params> paramsList) {
    StreamObserver<Params> requestObserver =
        this.asyncStub.removeMany(
            new StreamObserver<Todo>() {
              @Override
              public void onNext(Todo todo) {
                log.info("todo removed : {}", todo.getName());
              }

              @Override
              public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
              }

              @Override
              public void onCompleted() {
                log.info("finish remove todos");
              }
            });
    paramsList.forEach(requestObserver::onNext);
    requestObserver.onCompleted();
    return "Deleted successfully!";
  }

  public TodoResponse makeDoneTodo(Params request) {
    Todo todo = this.stub.makeDoneTodo(request);
    return buildTodoResponse(todo);
  }

  private TodoResponse buildTodoResponse(Todo todo) {
    return TodoResponse.builder()
        .id(todo.getId())
        .status(todo.getStatus())
        .name(todo.getName())
        .description(todo.getDescription())
        .owner(todo.getOwner())
        .build();
  }

  private Todo buildTodo(TodoRequest request) {
    return Todo.newBuilder()
        .setName(request.getName())
        .setDescription(request.getDescription())
        .setOwner(request.getOwner())
        .build();
  }
}
