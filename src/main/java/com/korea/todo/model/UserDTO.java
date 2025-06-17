package com.korea.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	private String token;
	private String username;
	private String password;
	private String id;
	
	public UserDTO(UserEntity entity) {
		username = entity.getUsername();
		password = entity.getPassword();
		id = entity.getId();
	}
	
	
}
