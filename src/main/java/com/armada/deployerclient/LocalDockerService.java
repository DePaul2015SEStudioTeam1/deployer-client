package com.armada.deployerclient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by john davidson on 10/24/14.
 */
@Component
public class LocalDockerService {

	public static final Logger LOG = LoggerFactory
			.getLogger(LocalDockerService.class);
	private DockerClient dockerClient;

	/**
	 * Connect to the docker daemon running locally. As a prerequisite, docker will already be running locally AND
	 * configured to listen on the port specified by docker.io.properties.
	 */
	public void connectToLocalDocker() {
		DockerClientConfig.DockerClientConfigBuilder b = DockerClientConfig.createDefaultConfigBuilder();
		LOG.info(b.toString());
		dockerClient = DockerClientBuilder.getInstance(b.build()).build();
		LOG.info("connected to: " + dockerClient.toString());
	}

	/**
	 * Gets the information object about the dockerClient
	 */
	public Info getLocalDockerInfo() {
		Info info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());
		return info;
	}

	/**
	 * Search the docker repository for all images that fit the name given. By default, this is hub.docker.com,
	 * credentials provided in the docker.io.properties file.
	 */
	public void searchDockerRepository(String imageName) {
		List<SearchItem> dockerSearch = dockerClient.searchImagesCmd(imageName).exec();
		LOG.info("Search returned " + dockerSearch.toString());
	}

	/**
	 * Remove an image if it exists locally
	 */
	public void removeImage(String imageName) {
		LOG.info("Removing image: {}", imageName);
		try {
			dockerClient.removeImageCmd(imageName).exec();
		}
		catch (NotFoundException e) {
			// just ignore if not exist
			LOG.warn("Image " + imageName + " does not exist locally.");
		}
	}

	/**
	 * Pull an image from the remote docker registry Credentials to pull are give in the docker.io.properties file
	 */
	public void pullImage(String imageName) {
		Info info = getLocalDockerInfo();

		int imgCount = info.getImages();
		LOG.info("imgCount1: {}", imgCount);

		//If the image already exists, remove it first
		removeImage(imageName);

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());
		imgCount = info.getImages();
		LOG.info("imgCount2: {}", imgCount);
		LOG.info("Pulling image: {}", imageName);

		InputStream response = dockerClient.pullImageCmd(imageName).exec();
		System.out.println(responseAsString(response));

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info after pull, {}", info.toString());

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(imageName).exec();
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
	}

	/**
	 * Create a container from an image that's locally available.
	 * Returns the string representing the Container ID.
	 */
	public String createContainer(String imageName, String containerName, String[] command) {
		CreateContainerResponse container = dockerClient
				.createContainerCmd(imageName).withCmd(command)
				.withName(containerName).exec();

		LOG.info("Created container {}", container.toString());
		return container.getId();
	}

	/**
	 * Start a container that is built locally. When started, the container will run
	 * whatever it's CMD is configured to be.
	 */
	public void startContainer(String containerId) {
		dockerClient.startContainerCmd(containerId).exec();
		InspectContainerResponse containerResponse = inspectContainer(containerId);
		LOG.info("Container Inspect: {}", containerResponse.toString());
	}

	/**
	 * Stop a running container, given by containerID
	 */
	public void stopContainer(String containerId) {
//		dockerClient.waitContainerCmd(containerId).exec();
		dockerClient.stopContainerCmd(containerId).exec();
	}

	/**
	 * Inspect a current container, returning a InspectContainerResponse object (docker-java api)
	 */
	public InspectContainerResponse inspectContainer(String containerId) {
		return dockerClient.inspectContainerCmd(containerId).exec();
	}

	/**
	 * Remove a container by id
	 */
	public void removeContainer(String containerId) {
		dockerClient.removeContainerCmd(containerId).exec();
	}


	/**
	 * Format an inputstream response
	 */
	protected String responseAsString(InputStream response) {
		StringWriter logwriter = new StringWriter();
		try {
			LineIterator itr = IOUtils.lineIterator(
					response, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + (itr.hasNext() ? "\n" : ""));
				LOG.info("line: " + line);
			}
			return logwriter.toString();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.closeQuietly(response);
		}
	}

	/**
	 * clean up the docker client
	 */
	public void cleanUp() {
		try {
			dockerClient.close();
			LOG.info("dockerClient closed");
		}
		catch (IOException ioe) {
			LOG.warn(ioe.toString());
		}
	}
}