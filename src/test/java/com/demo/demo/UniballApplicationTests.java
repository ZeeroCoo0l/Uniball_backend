package com.demo.demo;
import com.demo.demo.entities.Team;
import com.demo.demo.entities.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
//@WithMockUser(roles = "ADMIN")
class UniballApplicationTests {

	UserClient p1 = new UserClient(UUID.randomUUID().toString(),"Zack", "<EMAIL>", "0731234567", UserClient.Position.FORWARD,"","");
	UserClient p2 = new UserClient(UUID.randomUUID().toString(),"Oskar", "<EMAIL>", "0731234567", UserClient.Position.MIDFIELDER,"","");
	UserClient p3 = new UserClient(UUID.randomUUID().toString(),"Sebastian", "<EMAIL>", "0731234567", UserClient.Position.DEFENDER,"","");
	UserClient p4 = new UserClient(UUID.randomUUID().toString(),"Anna", "<EMAIL>", "0731234567", UserClient.Position.GOALKEEPER,"","");
	Team t1 = new Team("Uniballers");

	@Test
	void contextLoads() {
	}
	@Test
	void test() {
		t1.addPlayer(p1);
		t1.addPlayer(p2);
		t1.addPlayer(p3);
		t1.addPlayer(p4);
//		assertEquals(4, t1.getPlayers().size());
	}
}
