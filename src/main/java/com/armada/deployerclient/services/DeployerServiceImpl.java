package com.armada.deployerclient.services;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.stereotype.Service;

/**
 * Created by jodavidson on 11/15/14.
 */

/**
 * TODO: either fill this out to provide a layer between API and the web service
 * TODO: OR incorporate this into LocalDockerService for a single service rather than component
 */
@Service("deployerService")
public class DeployerServiceImpl {

	public String sayHello() {
		return "Hello from the deployerService (DeployerServiceImpl)";
	}

}
