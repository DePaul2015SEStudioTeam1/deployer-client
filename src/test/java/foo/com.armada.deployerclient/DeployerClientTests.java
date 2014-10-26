package com.armada.deployerclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class DeployerClientTests {

	@Autowired
	private LocalDockerService dockerService;

	@Before
	public void setUp() {
		dockerService.connectToLocalDocker();
	}

	@After
	public void tearDown() { dockerService.cleanUp(); }

	@Test
	public void testSearchImage() {
		dockerService.searchDockerRepository("helloworldtest");
	}

	@Test
	public void testPullImage() {
		dockerService.pullImage("armadaproject/helloworldtest");
	}

	@Test
	public void testRemoveImage() {
		dockerService.removeImage("armadaproject/helloworldtest");
	}

	@Test
	public void testCreateContainer() {
		String[] command = { "ping", "127.0.0.1"};
		String id = dockerService.createContainer(
				"armadaproject/helloworldtest",
				"helloworldcontainer-1", command);
	}

	@Test
	public void testStartContainer() {
//		dockerService.startContainer("7985de761dd9");
	}

	@Test
	public void testStopContainer() {
//		dockerService.stopContainer("7985de761dd9");
	}

}
