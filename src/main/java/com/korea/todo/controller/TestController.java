package com.korea.todo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.TestRequestBodyDTO;

@RestController
//데이터를 반환하는 컨트롤러로 사용.
//JSON이나 XML형식의 데이터를 반환한다.
//@Controller, @ResponseBody 두개의 어노테이션의 결합이다
//@ResponseBody는 메서드의 반환값을 HTTP ResponseBody로 직렬화해 클라이언트에게 전달한다.
@RequestMapping("test")//test주소료 요청이 들어왔을 때 현재 컨트롤러로 들어올 수 있게 해준다.
public class TestController {
	
	//localhost:10000/test/tsetgetmapping
	@GetMapping("/testgetmapping")//GET으로 요청이 들어왔을 때 요청을 받아서 아래 메서드를 실행해준다.
	public String testController() {
		return "Hello worldss";
	}
	
	//localhost:10000/test2
	@GetMapping("/test2")
	public String testController2() {
		return "Hello worldss";
	}
	
	//localhost:10000/test/userss/1
	@GetMapping("/users/{id}")
	public String getUserById2(@PathVariable(name="id",required = false) int id) {
		return "User ID : "+ id;
	}
	
	//배열을 통해 여러개의 요청 URI를 받을 수 있다.
	@GetMapping("/users/{userId}/orders/{orderId}")
	public String getOrderByUserAndOrderId(@PathVariable("userId")Long id,
								@PathVariable("orderId")Long orderId) {
		return "User Id : "+id + " Order ID : " + orderId;
	}
	
	//정규식을 써서 변수 형식을 제한할 수있다.
	@GetMapping("/users/{userId:[0-9]{3}}")
	public String getUserId2(@PathVariable("userId")Long id,
								@PathVariable("orderId")Long orderId) {
		return "User Id : "+id + " Order ID : " + orderId;
	}
	
//================================================================================
	//localhost:10000/test/users?id=111
	@GetMapping("/users")
	//public String getUserById(RequestParam Long id)
		//쿼리스트링의 key와 매개변수의 변수명이 일치한다면 value값을 안줘도 된다.
	//public String getUserById(@RequestParam(reqired=false Long userId)
		//값을 필수로 넣지 않아도 에러가 나지는 않는다.
	//public String getUSerById(@RequestParam(defaultValue="0") Long  id)
		//값지 넘어오지 않았을때의 기본값을 설정할 수 있다.
	public String getUserById(@RequestParam(value = "id") Long userId) {
		return "User ID : " + userId;
	}
	
	@GetMapping("/search")
	public String search(@RequestParam("query") String query,
							@RequestParam("page") int page) {
		return "Search query : " + query + ", page : "+ page;
	}

//========================================================================
	
	@PostMapping("/submitForm")
	public String submitForm(@RequestParam("name")String name,
								@RequestParam("email")String email) {
		return "Form submitted : Name = " + name + ", Email : " + email;
	}
	
//========================================================================
	
	@GetMapping("/testRequestBody")
	//JSON 형식으로 전달되는 데이터를 TestRequestBodyDTO 형식으로 만들어서 넣어준다
	public String testRequestBody(@RequestBody TestRequestBodyDTO dto) {
		return "ID : "+dto.getId() + ", MESSAGE : " + dto.getMessage();
	}
	
	@GetMapping("/testRequestBody1")
	//[TEST] Dto 대신 Map 으로 해도 아주 잘 된다.
	public String testRequestBody1(@RequestBody Map<String, Object> dto) {
		return "ID : "+dto.get("id") + ", MESSAGE : " + dto.get("message");
	}
	
	@GetMapping("/testResonseBody")
	public ResponseDTO<String> testResponseBody(){
		List<String> list = Arrays.asList("하나","둘","셋");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//String error, List<String> list가 담겨있는 
		//ResponseDTO 객체를 반환
		return response;
	}
	
//=======================================================================
	@GetMapping("/testResponseEntity")
	//? = Object
	public ResponseEntity<?> test(){
		List<String> list = new ArrayList<String>();
		list.add("Hello World! I`m ResponseEntity. And you got 400");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//400 강제로 발생시키기
		return ResponseEntity.badRequest().body(response);
	}
	//ResponseDTO를 반환하는것과 비교했을 때 큰 차이는 없지만
	//단지 헤더와 HTTPStatus 를 조작할 수 있다는 점이 다르다.
	
	
	
}
