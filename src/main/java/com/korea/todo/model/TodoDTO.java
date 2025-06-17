package com.korea.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDTO {

	private String id; //이 객체의 id
	private String title; // Todo의 타이틀
	private boolean done;//Todo의 완료 여부
	
	//생성자
	public TodoDTO(TodoEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
	}
	
	//TodoDto -> TodoEntity
	public static TodoEntity toEntity(TodoDTO dto) {
		return TodoEntity.builder()
							.id(dto.getId())
							.title(dto.getTitle())
							.done(dto.isDone())
							.build();
	}
	
}
