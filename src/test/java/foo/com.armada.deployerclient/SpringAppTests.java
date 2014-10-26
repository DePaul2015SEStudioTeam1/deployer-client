package com.armada.deployerclient;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SpringAppTests {

	@Autowired
	private LocalDockerService dockerService;

	@Before
	public void setUp() {
		System.out.println(dockerService.connectToLocalDocker());
	}

	@After
	public void tearDown() { System.out.println(dockerService.cleanUp()); }

	@Test
	public void testSearchImage() {
		System.out.println(dockerService.searchDockerRepository("helloworldtest"));
	}
}
