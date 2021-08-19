package com.giangtheshy.client.dto;

import com.giangtheshy.proto.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponse {
    private Long id;
    private String name;
    private String description;
    private Todo.Status status;
    private String owner;
}
