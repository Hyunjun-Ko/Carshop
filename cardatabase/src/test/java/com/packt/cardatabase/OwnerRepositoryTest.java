package com.packt.cardatabase;

import com.packt.cardatabase.domain.Owner;
import com.packt.cardatabase.domain.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// 소유자 리포지터리의 CRUD 작업을 테스트 할 단위 테스트
// @SpringBootTest 대신 @DataJpaTest 어노테이션을 이용하면
// H2 하이버네이트 데이터베이스의 스프링 데이터가 자동으로 테스트에
// 맞게 구성된다. SQL 로깅도 활성화 된다.
@DataJpaTest
public class OwnerRepositoryTest {
    @Autowired
    private OwnerRepository repository;

    @Test
    void saveOwner() {
        repository.save(new Owner("Lucy", "Smith"));
        assertThat(repository.findByFirstname("Lucy").isPresent()).isTrue();
    }

    @Test
    void deleteOwners() {
        repository.save(new Owner("Lisa", "Morrison"));
        repository.deleteAll();
        assertThat(repository.count()).isEqualTo(0);
    }
}
