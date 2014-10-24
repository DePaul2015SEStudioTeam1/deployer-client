package com.armada.deployerclient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DeployerClient {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        LocalDockerService dockerService = context.getBean(LocalDockerService.class);
        System.out.println(dockerService.connectToLocalDocker());
    }
}
