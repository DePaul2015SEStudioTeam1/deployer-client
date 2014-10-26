package com.armada.deployerclient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by john davidson on 10/24/14.
 */
@Component
public class LocalDockerService {

	public static final Logger LOG = LoggerFactory
			.getLogger(LocalDockerService.class);

	private DockerClient dockerClient;

	public String connectToLocalDocker() {

	    DockerClientConfig.DockerClientConfigBuilder b = DockerClientConfig.createDefaultConfigBuilder();
		LOG.info(b.toString());
		dockerClient = DockerClientBuilder.getInstance(b.build()).build();

		return "connected to: " + dockerClient.toString();
	}

	public String searchDockerRepository(String imageName) {
		List<SearchItem> dockerSearch = dockerClient.searchImagesCmd(imageName).exec();
		return "Search returned" + dockerSearch.toString();
	}

	public String pullDockerImage(String imageName) {
		return "";
	}

	public String createDockerContainer(String imageName) {
		return "";
	}

	public String startDockerContainer(String imageName) {
		return "";
	}

	public String stopDockerContainer(String imageName) {
		return "";
	}

	public String cleanUp() {
		try {
			dockerClient.close();
		} catch (IOException ioe) {
			LOG.warn(ioe.toString());
		}
		return "cleaned up.";
	}

}