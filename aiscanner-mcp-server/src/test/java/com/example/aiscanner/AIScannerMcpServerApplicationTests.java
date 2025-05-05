package com.example.aiscanner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"sapling.ai.api.key=NQT0OS6F50VXOGXQNDH50QZKRM2RB92P"
})
class AIScannerMcpServerApplicationTests {

	@Test
	void contextLoads() {
		// This will test that the application context loads successfully
	}
}
