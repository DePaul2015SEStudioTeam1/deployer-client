package com.armada.deployerclient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DeployerClient {

    public static void main(String[] args) {
	    System.out.println("Running the DeployerClient...");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        LocalDockerService dockerService = context.getBean(LocalDockerService.class);
	    dockerService.connectToLocalDocker();
	    System.out.println("Connected to dockerService: " + dockerService.toString());
	    System.out.println("Local docker info:\n" + dockerService.getLocalDockerInfo().toString());

	    System.out.println("Searching for the agent to deploy:");
	    dockerService.searchDockerRepository("agent-prod");

	    System.out.println("Pull down image from remote registry:");
	    dockerService.pullImage("armadaproject/agent-prod");

	    String[] agentOneCommand = { "java","-jar", "/opt/lib/agent/agent.jar", "deployer-johntestagent"};
	    String[] agentTwoCommand = { "java","-jar", "/opt/lib/agent/agent.jar", "deployer-classtest-2"};
	    String[] agentThreeCommand = { "java","-jar", "/opt/lib/agent/agent.jar", "deployer-classtest-3"};

	    String containerOneId = dockerService.createContainer(
				"armadaproject/agent-prod",
				"deployer-classtest-1", agentOneCommand);
	    String containerTwoId = dockerService.createContainer(
			    "armadaproject/agent-prod",
			    "deployer-classtest-2", agentTwoCommand);
	    String containerThreeId = dockerService.createContainer(
			    "armadaproject/agent-prod",
			    "deployer-classtest-3", agentThreeCommand);

	    System.out.println("about to start agents in containers...");
	    dockerService.startContainer(containerOneId);
	    dockerService.startContainer(containerTwoId);
	    dockerService.startContainer(containerThreeId);
    }
}
