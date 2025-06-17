package com.korea.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	
	//AWS 로드밸런서는 루트 경로인 "/"에 http 요청을 보내서 
	//어플리케이션이 동작하는지 확인한다.
	//EB(ElasticBeanStalk)는 이를 기반으로 어플리케이션이 실행중인지, 주의가 필요한 상태인지 확인이 필요하다.
	@GetMapping("/")
	public String headlthCheck() {
		return "The service is up and running...";
	}

}
