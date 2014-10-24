package com.armada.deployerclient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.jaxrs.DockerClientBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by john davidson on 10/24/14.
 */
@Component
public class LocalDockerService {

	private DockerClient dockerClient;

	public String connectToLocalDocker() {
		dockerClient = DockerClientBuilder.getInstance("http://localhost:2375").build();
		return "connected to: " + dockerClient.toString();
	}

	public String searchDockerRepository(String imageName) {
		List<SearchItem> dockerSearch = dockerClient.searchImagesCmd(imageName).exec();
		return "Search returned" + dockerSearch.toString();
	}

	public String pullDockerImage(String imageName) {
		return "";
	}

}