package com.korea.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.korea.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	
	//비밀키(변하지 않는 상수) 512
	private static final String SECRET_KEY = "60f2609f589e1f3e2d63fe29e76426fcf2f0d20633c37673dbbc82"
			+ "429f65d4d0f0fa87c24a8ff36b515bee2ba454347b95b43109581d152c5575ad2c09caf89fe880dbdc13d06"
			+ "9674372958bce9afbcd59812c257d777b89300f264fca32b8204d2b51127f016ae0266393575187cc99485b78"
			+ "83ff8373a61a95a91348f26ced12bae3306d111c146c120b179066754ea8912047b0ee0d12d4c08aafa0dac391"
			+ "448b08f55f399ce4be1d8b74090e954ea89656c06391207ad5c3e955e0d18a721bb47da75c673b770e48275455079"
			+ "32165f1a3344ea5fc7831342e6ee3b84db764491b706320f808bb71c179169a67b6c9cb62ca0421401bdc439118be94"
			+ "8e9908e5b1e234997b4350f09d660b6527256a626c4650bf30f5cc5cbbc1fefd9496799c17f414ba22dac6a919be333eae"
			+ "91e722b47bd6257d3be96a2adfbfa68d596a7bcd3833d8e1849e4aa9c6cf36d2ad384dab770afddf775bda0a6724997cd35a"
			+ "bf701a2986695d12697af4bab58dda457170680e7df17e62eaa4ecc3817e059d9a577e7a73f6874550a775b64dcb51fa00593"
			+ "920957fd5cc5d739629a50a966e0074482c5b0372278a6d1f9dddb177a34aad9fa6e2d1b13ea5e6443199b90e1e9e298f9090675"
			+ "e8768fdf70ebf12e2f6e0b8fcc4a76b7be4e29690163b33c8ce430f8af08d35afec4332b6a9cd6855018ee04b0e25a49e7db5256501cf6eec";
	
	//토큰을 만드는 메서드
	public String create(UserEntity entity) {
		//토큰 만료시간을 설정.
		//현재 시각 + 1일
		//Instant클래스 : 타임스탬프로 찍는다.
		//plus() : 첫번재 인자는 더할 양, 시간단위.
		//ChronoUnit 열거형의 DAYS 일 단위를 의미한다.
		//Date -> 날짜 객체
			//expriryDate -> 변수의 이름(만료일)
				//Date.from() -> from() = Instant 객체를 Date 로 변환(신버전 -> 구버전)
					//Instant.now() -> 오늘
						//.plus() -> 더하겠다.
							//(1,ChronoUnit.DAYS) -> 1을 더하겠다. 타입은 일(하루를 더하겠다)
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		
		
		/*
		 	<JWT 토큰 내부>
			header
			{
				"alg" : "HS512"
				"typ":"JWT"
			}.
			{
			"sub" : "id 아무거나",
			"iss" : "todo app",
			"iat" : "1595733657",
			"exp" : "1596597657" 
			}.
			서명
		*/		
		
		//JWT 토큰을 빌더 형태로 생성
		return Jwts.builder()
				//header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512,SECRET_KEY) //헤더+서명 알고리즘 설정
				.setSubject(entity.getId())//sub 클레임 : 사용자 고유 ID
				.setIssuer("todo app") //iss 클레임 : 토큰 발급자
				.setIssuedAt(new Date()) //iat 클레임 : 발급 시각
				.setExpiration(expiryDate) //exp 클레임 : 만료시각
				.compact(); //최종 직렬화된 토큰 문자열 반환.
	}
	
	
	
	//토큰 검사(vaildate) 메서드
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY) //서명 검증용 키 설정
									.parseClaimsJws(token) //토큰 파싱 및 서명 검증
									.getBody(); //내부 페이로드(Claims)획득 페이로드(sub,iss,iat,exp...)
		
		return claims.getSubject(); //sub 클레임(사용자 ID) 반환.
	}
	
	
	
	

}//end class
