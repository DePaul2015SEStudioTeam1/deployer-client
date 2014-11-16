package com.armada.deployerclient.endpoints;

import com.armada.deployerclient.LocalDockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import com.armada.deployerclient.services.DeployerServiceImpl;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by jodavidson on 11/15/14.
 */
@WebService(serviceName="DeployerService")
public class DeployerServiceEndpoint extends SpringBeanAutowiringSupport {

	@Autowired
	private DeployerServiceImpl deployerService;

	@Autowired
	private LocalDockerService localDockerService;

	@WebMethod
	public String sayHello() {
		return deployerService.sayHello();
	}

	@WebMethod
	public void connectToLocalDocker() {
		localDockerService.connectToLocalDocker();
	}

	@WebMethod
	public void searchDockerRepository(String search) {
		localDockerService.searchDockerRepository(search);
	}

	@WebMethod
	public void pullImage(String imageName){
		localDockerService.pullImage(imageName);
	}
	@WebMethod
	public void createContainer(String imageName, String containerName, String[] commands){
		localDockerService.createContainer(imageName, containerName, commands);
	}
	@WebMethod
	public void startContainer(String containerId){
		localDockerService.startContainer(containerId);
	}
	@WebMethod
	public void stopContainer(String containerId){
		localDockerService.stopContainer(containerId);
	}

}
