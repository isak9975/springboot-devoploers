package com.korea.todo.controller;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.TodoDTO;
import com.korea.todo.model.TodoEntity;
import com.korea.todo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	//실행할 때 service 객체가 필드로 직접 주입된다.,
	@Autowired
	TodoService service;
	
	//주입받은 객체로 메서드를 실행하면 된다.
//	@GetMapping("/test")
//	public ResponseEntity<?> testTodo(){
//		//service클래스에 있는 메서드를 호출
//		String str = service.testService();
//		List<String> list = new ArrayList<>();
//		list.add(str);
//		ResponseDTO<String> response = ResponseDTO
//		.<String>builder().data(list).build();
//		return ResponseEntity.ok().body(response);
//	}
	
//===========================================================================
	//@AuthenticationPrincipal
	//스프링 시큐리티에서 사용자의 Principal 객체를 컨트롤러의 메서드의 인자로
	//주입하기 위해 사용하는 어노티에션
	
	//Principal : 시큐리티에서 현재 인증된 사용자를 나타내는 객체
	//주로 SecurityContextHolder에 저장된 Authentication 객체를 통해 접근할 수 있다.
	//Authentication 객체의 getPrincipal()메서드는 인증된 사용자의 정보를 담고있으며,
	//이 정보는 @AuthenticationPrincipal 어노테이션을 통해 컨트롤러 메서드에 자동으로 주입된다.
	
	//요청을 통해서 넘어오는 정보는 요청본문에 담겨서 온다.
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {			
			//TodoDTO 객체를 TOdoEntity객체로 변환한다.
			TodoEntity entity = dto.toEntity(dto);
			
			//id 값을 명시적으로 null 로 설정하여, 엔티티가 새로운 데이터임을 보장한다.			
			entity.setId(null); //혹시라도 아무값이나 들어있으면 자동으로 생성되지 않기 때문.
			
			//유저 아이디를 @AuthenticationPrincipal을 통해 Principal 에서 꺼내와서 적용.
			entity.setUserId(userId);
			
			//서비스 계층에 있는 create메서드를 호출하여, TodoEntity를 데이터베이스에 저장하는 작업ㅇ르 수행한다.
			//이 메서드는 추가만 하는것이 아니라 저저장된 TodoEntity 객체들을 저장한 리스트에 반환한다.
			List<TodoEntity> entityes = service.create(entity);
			
			
			//자바스트림을 이용해 반환된 엔티티 리스트를 TOdoDTO리스트로 변환한다.
			//TodoDTO::new -> TodoDTO 생성자의 호출
			List<TodoDTO> dtos = entityes.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//변환된 TodoDTO""리스트를 이용해 responseDTO를 초기화한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			//혹시나 예외가 발생하는 경우 dto대신 error에 메시지를 넣어서 반환
			String error = e.getMessage();;
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	@GetMapping
	public ResponseEntity<?> retriveTodolist(@AuthenticationPrincipal String userId) {
		//아이디에 해당되는 엔티티를 Service 의 함수를 이용하여 얻어오기.
		List<TodoEntity> entities = service.retrive(userId);
		 
		//생성자를 이용하여 Entity -> Dto 를 구현하였으므로
		//
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//보낼 데이터들을 ResponseDTO에 담기.(RespopnseDTO로 만들기)
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		//dto를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		//로그인 되어있는 유저 아이디 설정
		entity.setUserId(userId);
		
		//서비스레이어의 update메서드를 이용해 entity를 업데이트한다.
 		List<TodoEntity> entites =  service.update(entity);
 		
 		//자바스트림을 이용해 반환된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
 			//.map 으로 돌려서 TodoDTO의 객체로 만들고, .collect 을 사용해 List 타입으로 변환한다.
 		List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
 		
 		//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
 			//ResponseDTO의 data가 제네릭이기 때문에 제네릭 제네릭 선언해줘야한다.
 		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
 		
 		return ResponseEntity.ok().body(response);
		
	}
	
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		//로그인 되어있는 유저 아이디 설정
		dto.setId(userId);

		//TodoDTO 객체를 TodoEntity객체로 변환한다.
		TodoEntity entity = dto.toEntity(dto);
		
		List<TodoEntity> entites =  service.delete(entity);
		
		//스르임을 이용해서 List 안에 있는 Entity 객체를 DTO로 바꾼다.
		List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	

	
}//end class
