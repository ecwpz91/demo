package com.redhat.mrp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.mrp.model.Rover;
import com.redhat.mrp.model.RoverList;

@Controller
@RequestMapping("/")
public class RoverController {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	private static final String URI = "https://api.nasa.gov/mars-photos/api/v1/rovers";
	private static final Logger LOGGER = LoggerFactory.getLogger(RoverController.class);

	@Value("${api.key}")
	private String apiKey;

	public RoverController(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	@GetMapping("/rovers")
	public String findAllRovers(ModelMap model) {
		String uriString = UriComponentsBuilder.fromHttpUrl(URI).queryParam("api_key", this.apiKey).toUriString();
		Optional<RoverList> result = Optional.ofNullable(this.restTemplate.getForObject(uriString, RoverList.class));
		List<Rover> rovers = result
			.map(RoverList::getRovers)
			.map(List::of)
			.orElseGet(ArrayList::new);

		model.put("rovers", rovers);
		return "rovers";
	}

	@GetMapping("/rover/{name}")
	public String findRoverByName(ModelMap model, @PathVariable String name) {
		String uriString = UriComponentsBuilder.fromHttpUrl(URI + "/" + name).queryParam("api_key", apiKey).toUriString();
		Rover result = this.restTemplate.getForObject(uriString, Rover.class);

		if (result != null) {
			LOGGER.debug("Response body :: {}", result);
			try {
				LOGGER.debug("Rover :: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
			} catch (Exception e) {
				LOGGER.debug("Exception :: {}", e);
			}
		}

		model.put("rover", result);
		return "rover";
	}

}
