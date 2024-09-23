package com.packt.cardatabase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// RESTful 웹 서비스의 인증 기능 테스트
// 컨트롤러나 다른 노출된 엔드 포인트를 테스트하려면 MockMvc 객체를 이용할 수 있다.
// MockMvc 객체를 이용하면 서버가 시작되지는 않지만 스프링이 HTTP 요청을 처리하는
// 계층에서 실제 상황을 모의 테스트할 수 있다. MockMvc는 이러한 요청을 보내는 perform
// 메서드를 제공한다. 인증을 테스트하라면 요청 본문에 자격증명을 추가해야 한다. 마지막으로
// 응답상태가 OK인지 확인한다.
@SpringBootTest
@AutoConfigureMockMvc
public class CarRestTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthentication() throws Exception {
        // 올바른 자격 증명으로 인증 테스트
        this.mockMvc.
                perform(post("/login").
                content("{\"username\":\"admin\",\"password\":\"admin\"}").
                header(HttpHeaders.CONTENT_TYPE, "application/json")).
                andDo(print()).andExpect(status().isOk());
    }
}
