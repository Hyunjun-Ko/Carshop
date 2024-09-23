package com.packt.cardatabase;

import com.packt.cardatabase.web.CarController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// JUnit을 단위 테스트에 이용한다. @SpringBootTest 어노테이션은 해당 클래스가
// 스프링 부트 기반 테스트를 실행하는 일반 테스트 클래스임을 지정한다. 메서드 앞의
// @Test 어노테이션은 해당 메서드를 테스트 케이스로 실행할 수 있다고 JUnit에 알린다.
@SpringBootTest
class CardatabaseApplicationTests {
	@Autowired
	private CarController controller;

	// 테스트를 추가할 위치는 이 클래스의 contextLoads라는 테스트 메서드다.
	@Test
	@DisplayName("First example test case") // 어노테이션으로 테스트 케이스 이름 지정
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
