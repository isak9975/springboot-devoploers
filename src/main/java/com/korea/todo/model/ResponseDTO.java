package com.korea.todo.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//http 응답으로 사용할 DTo
public class ResponseDTO<T> {
	private String error;
	private List<T> data;
}
