package com.fish.shareplan;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class JpaTest {

    @Autowired
    RoomRepository roomRepository;


    @Test
    public void testEntityCreation() {
        // 특정 엔티티가 올바르게 테이블로 생성되었는지 테스트
        List<Room> all = roomRepository.findAll();
        System.out.println("all = " + all);
    }
}
