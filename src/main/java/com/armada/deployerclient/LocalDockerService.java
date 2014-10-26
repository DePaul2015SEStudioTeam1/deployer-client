package com.armada.deployerclient;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
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

	public String connectToLocalDocker() {

	    DockerClientConfig.DockerClientConfigBuilder b = DockerClientConfig.createDefaultConfigBuilder();
		LOG.info(b.toString());
		dockerClient = DockerClientBuilder.getInstance(b.build()).build();

		return "connected to: " + dockerClient.toString();
	}

	public String getLocalDockerInfo() {
		Info info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());
		return info.toString();
	}

	public String searchDockerRepository(String imageName) {
		List<SearchItem> dockerSearch = dockerClient.searchImagesCmd(imageName).exec();
		return "Search returned " + dockerSearch.toString();
	}

	public void removeDockerImage(String imageName) {
		LOG.info("Removing image: {}", imageName);
		try {
			dockerClient.removeImageCmd(imageName).exec();
		} catch (NotFoundException e) {
			// just ignore if not exist
			LOG.warn("Image " + imageName + " does not exist locally.");
		}
	}

	public void pullDockerImage(String imageName) {
		Info info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());

		int imgCount = info.getImages();
		LOG.info("imgCount1: {}", imgCount);

		//If the image already exists, remove it first
		removeDockerImage(imageName);

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

	public String createDockerContainer(String imageName) {
		return "";
	}

	public String startDockerContainer(String imageName) {
		return "";
	}

	public String stopDockerContainer(String imageName) {
		return "";
	}

	protected String responseAsString(InputStream response)  {

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + (itr.hasNext() ? "\n" : ""));
				//LOG.info("line: "+line);
			}
			return logwriter.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(response);
		}
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