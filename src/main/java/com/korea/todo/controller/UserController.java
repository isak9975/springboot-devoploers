package com.korea.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.UserDTO;
import com.korea.todo.model.UserEntity;
import com.korea.todo.security.TokenProvider;
import com.korea.todo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

	
	//userService 생성자 주입하기.
	@Autowired
	private final UserService service;
	
	//TokenProvider 클래스 주입하기
	private final TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	//회원가입(유저추가)
	//로그인을 해야 토큰을 주는거지, 회원가입을 했다고 토큰을 주는게 아니다.
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO dto){
		//요청본문에 포함된 UserDTO 객체를 수신하여 처리한다.
		try {
			//UserDTO기반으로 UserEntity객체 생성하기
			UserEntity entity = UserEntity.builder().
					username(dto.getUsername())
					//비밀번호를 그냥 저장하지 않고 암호화.
					.password(passwordEncoder.encode(dto.getPassword()))
					.build();
			//UserEntity 객체를 service 로 보내서 데이터베이스에 추가하기.
			UserEntity result = service.create(entity);
			
			//등록된 UserEntity 정보를 UserDTO로 변환하여 응답에 사용한다.
			UserDTO response = UserDTO.builder().username(result.getUsername())
					.password(result.getPassword()).build();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			//예외가 발생한 경우, 에러 메시지를 포함한 ResponseDTO 객체를 만들어 응답에 보낸다.
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity
					.badRequest() //HTTP 400 응답을 생성한다.
						.body(responseDTO); //에러 메시지를 포함한 응답 본문을 반환한다.
		}
			
	}
	
	
	//로그인
	//Get으로 만들면 브라우저의 주소창에 아디이와 비밀번호가 노출될 수 있다.
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO dto){
		//요청 본문으로 전달된 UserDTO의 username과 password을 기반으로 유저를 조회
			//+ 비밀번호 암호화 객체도 같이 넘김.
		UserEntity user = service.getByCredential(dto.getUsername(), 
									dto.getPassword(),
									passwordEncoder);
		
		//조회된 user 가 있으면
		if(user != null) {
			//토큰을 만든다
			final String token = tokenProvider.create(user);
			
			//인증에 성공한 경우, 유저 정보를 UserDTO로 변환하여 응답에 사용한다.
			final UserDTO userDTO = UserDTO
					.builder()
						.id(user.getId())
							.password(user.getPassword())
								.token(token)
									.build();
			return ResponseEntity.ok().body(userDTO);
		}else {
			//조회된 유저가 없거나, 인증이 실패한 경우 에러 메시지를 포함한 ResponseDTO를 반환한다.
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Login falied")
						.build();
			return ResponseEntity.badRequest().body(responseDTO);
			
		}		
		
	}
	
}
