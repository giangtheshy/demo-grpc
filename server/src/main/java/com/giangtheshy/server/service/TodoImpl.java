package com.giangtheshy.server.service;

import com.giangtheshy.proto.Params;
import com.giangtheshy.proto.Todo;
import com.giangtheshy.proto.TodoServiceGrpc;
import com.giangtheshy.server.exception.SpringException;
import com.giangtheshy.server.entiry.TodoEntity;
import com.giangtheshy.server.repository.TodoRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
@Slf4j
public class TodoImpl extends TodoServiceGrpc.TodoServiceImplBase {
  @Autowired private TodoRepository todoRepository;


  @Override
  public void addOne(Todo request, StreamObserver<Todo> response) {
    log.info("add one todo : {}", request);

    TodoEntity entity = buildEntity(request, Todo.Status.NONE);
    TodoEntity todoEntity = todoRepository.save(entity);
    Todo todo = buildTodo(todoEntity);
    response.onNext(todo);
    response.onCompleted();
  }

  @Override
  public void getAll(Params request, StreamObserver<Todo> response) {
    List<TodoEntity> listTodos = todoRepository.findAll();
    listTodos.forEach(
        todo -> {
          response.onNext(buildTodo(todo));
        });
    response.onCompleted();
  }

  @Override
  public StreamObserver<Todo> addMany(StreamObserver<Todo> response) {
    return new StreamObserver<Todo>() {
      @Override
      public void onNext(Todo todo) {
        TodoEntity todoEntity = todoRepository.save(buildEntity(todo, Todo.Status.NONE));
        log.info("add todo : {}", todoEntity.getName());
        response.onNext(buildTodo(todoEntity));
      }

      @Override
      public void onError(Throwable throwable) {
        log.error(throwable.getMessage());
      }

      @Override
      public void onCompleted() {
        log.info("finish!");
        response.onCompleted();
      }
    };
  }

  @Override
  public void getOne(Params request, StreamObserver<Todo> response) {
    TodoEntity todoEntity =
        todoRepository
            .findById(request.getId())
            .orElseThrow(() -> new SpringException("Not found Todo"));
    response.onNext(buildTodo(todoEntity));
    response.onCompleted();
  }

  @Override
  public void removeOne(Params request, StreamObserver<Todo> response) {
    TodoEntity todoEntity =
        todoRepository
            .findById(request.getId())
            .orElseThrow(() -> new SpringException("Not found Todo"));
    todoRepository.deleteById(request.getId());
    response.onNext(buildTodo(todoEntity));
    response.onCompleted();
  }

  @Override
  public StreamObserver<Params> removeMany(StreamObserver<Todo> response) {
    return new StreamObserver<Params>() {
      @Override
      public void onNext(Params params) {
        TodoEntity todoEntity =
            todoRepository
                .findById(params.getId())
                .orElseThrow(() -> new SpringException("Not found Todo"));
        todoRepository.deleteById(params.getId());
        response.onNext(buildTodo(todoEntity));
      }

      @Override
      public void onError(Throwable throwable) {
        log.error(throwable.getMessage());
      }

      @Override
      public void onCompleted() {
        response.onCompleted();
      }
    };
  }
  @Override
  public void makeDoneTodo(Params request,StreamObserver<Todo> response){
    TodoEntity todoEntity = todoRepository.findById(request.getId()).orElseThrow(() -> new SpringException("Not found Todo"));
    todoEntity.setStatus(Todo.Status.DONE);
    TodoEntity entity = todoRepository.save(todoEntity);
    response.onNext(buildTodo(entity));
    response.onCompleted();
  }

  private TodoEntity buildEntity(Todo todo) {
    return TodoEntity.builder()
        .id(todo.getId())
        .name(todo.getName())
        .description(todo.getDescription())
        .owner(todo.getOwner())
        .status(todo.getStatus())
        .build();
  }

  private TodoEntity buildEntity(Todo todo, Todo.Status status) {
    return TodoEntity.builder()
        .id(todo.getId())
        .name(todo.getName())
        .description(todo.getDescription())
        .owner(todo.getOwner())
        .status(status)
        .build();
  }

  private Todo buildTodo(TodoEntity entity) {
    return Todo.newBuilder()
        .setName(entity.getName())
        .setDescription(entity.getDescription())
        .setOwner(entity.getOwner())
        .setStatus(entity.getStatus())
        .setId(entity.getId())
        .build();
  }
}
