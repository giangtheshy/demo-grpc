package com.giangtheshy.client.controller;

import com.giangtheshy.client.dto.TodoRequest;
import com.giangtheshy.client.dto.TodoResponse;
import com.giangtheshy.client.service.GRPCClientService;
import com.giangtheshy.proto.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1.0/todos")
public class TodoController {
  GRPCClientService grpcClientService;

  @Autowired
  public TodoController(GRPCClientService grpcClientService) {
    this.grpcClientService = grpcClientService;
  }

  @PostMapping("/")
  public TodoResponse addOne(@RequestBody TodoRequest todoRequest) throws Exception {
    return grpcClientService.addTodo(todoRequest);
  }

  @PostMapping("/many")
  public String addMany(@RequestBody List<TodoRequest> todoRequest) {
    return grpcClientService.addMany(todoRequest);
  }

  @GetMapping("/")
  public List<TodoResponse> getAll() {
    return grpcClientService.getAll();
  }

  @GetMapping("/{id}")
  public TodoResponse getOne(@PathVariable Long id) {
    return grpcClientService.getOne(
        Params.newBuilder().setId(id).setPage(0).setSort(false).build());
  }

  @DeleteMapping("/{id}")
  public TodoResponse removeOne(@PathVariable Long id) {
    return grpcClientService.removeOne(
        Params.newBuilder().setId(id).setPage(0).setSort(false).build());
  }

  @PostMapping("/delete")
  public String removeMany(@RequestBody List<Long> listIds) {
    List<Params> params =
        listIds.stream()
            .map(id -> Params.newBuilder().setId(id).setSort(false).setPage(0).build())
            .collect(Collectors.toList());
    return grpcClientService.removeMany(params);
  }

  @PutMapping("/{id}")
  public TodoResponse makeDoneTodo(@PathVariable Long id) {
    return grpcClientService.makeDoneTodo(
        Params.newBuilder().setId(id).setSort(false).setPage(0).build());
  }
}
