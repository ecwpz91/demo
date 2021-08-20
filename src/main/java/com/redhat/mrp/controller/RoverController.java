package com.redhat.mrp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.mrp.dto.Rover;
import com.redhat.mrp.model.RoverList;

@Controller
@RequestMapping("/")
public class RoverController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String URI = "https://api.nasa.gov/mars-photos/api/v1/rovers";
	private static final Logger LOGGER = LoggerFactory.getLogger(RoverController.class);

	@Value("${api.key}")
	private String apiKey;

	private HttpEntity<String> entity;
	private UriComponentsBuilder builder;
	private String uriString;
	private RoverList rovers;
	private List<Rover> roverList;

	private static <T> List<T> convertArrayToList(T array[]) {
		List<T> list = new ArrayList<>();

		for (T t : array) {
			list.add(t);
		}

		return list;
	}

	@GetMapping(value = "/rovers")
	public String findAllRovers(ModelMap model) {
		builder = UriComponentsBuilder.fromHttpUrl(URI).queryParam("api_key", apiKey);
		uriString = builder.toUriString();
		LOGGER.debug("Fetching rovers from URI :: {}", uriString);		
		ResponseEntity<String> result = restTemplate.exchange(uriString, HttpMethod.GET, entity, String.class);

		if (result != null) {
			LOGGER.debug("Response body :: {}", result.getBody());
			try {
				rovers = objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
						.readValue(result.getBody(), RoverList.class);
				LOGGER.debug("RoverArray :: {}", rovers);
				roverList = convertArrayToList(rovers.getRovers());
				LOGGER.debug("RoverList :: {}", roverList);
			} catch (Exception e) {
				LOGGER.debug("Exception :: {}", e);
			}
		}

		model.put("rovers", roverList);
		return "rovers";
	}

}
