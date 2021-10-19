package com.redhat.mrp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.mrp.model.Photo;
import com.redhat.mrp.model.PhotoList;
import com.redhat.mrp.model.Rover;
import com.redhat.mrp.model.RoverList;

@Controller
@RequestMapping("/")
public class RoverController {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	private static final String URI = "https://api.nasa.gov/mars-photos/api/v1/rovers";
	private static final Logger LOGGER = LoggerFactory.getLogger(RoverController.class);
	private static final String FHAZ = "FHAZ";
	// private static final String DATE = "2015-6-3";
	private static final Random RANDOM = new Random();

	private HttpEntity<String> entity;
	private PhotoList photoList;

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
		List<Rover> rovers = result.map(RoverList::getRovers).map(List::of).orElseGet(ArrayList::new);

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

	@GetMapping("/photo/{name}")
	public String getRandomPhoto(ModelMap model, @PathVariable String name, @RequestParam(value = "landingDate", required = false) String landingDate, @RequestParam(value = "maxDate", required = false) String maxDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
		LocalDate from = LocalDate.parse(landingDate, formatter);
		LocalDate to = LocalDate.parse(maxDate, formatter);
		long days = from.until(to, ChronoUnit.DAYS);
		long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
		LocalDate randomDate = from.plusDays(randomDays);

		photoList = getAllPhotos(randomDate.toString(), name);
		Photo randomPhoto = null;

		if (photoList != null && photoList.getPhotos() != null) {
			Photo[] photos = photoList.getPhotos();
			if (photos.length == 0) {
				throw new IllegalArgumentException(
						"No photos available for the requested date " + randomDate.toString() + ". You may want to try other dates.");
			}
			randomPhoto = photos[RANDOM.nextInt(photos.length)];
			LOGGER.debug("Random photo picked :: {}", randomPhoto);
		}

		model.put("photo", randomPhoto);
		return "photo";
	}

	public PhotoList getAllPhotos(String date, String name) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URI + "/" + name + "/photos")
				.queryParam("earth_date", date).queryParam("camera", FHAZ).queryParam("api_key", apiKey);
		String uriString = builder.toUriString();
		LOGGER.debug("Fetching photos from URI :: {}", uriString);
		ResponseEntity<String> result = restTemplate.exchange(uriString, HttpMethod.GET, entity, String.class);

		if (result != null) {
			LOGGER.debug("Response body :: {}", result.getBody());
			try {
				photoList = objectMapper.readValue(result.getBody(), PhotoList.class);
				LOGGER.debug("PhotoList :: {}", photoList);
			} catch (Exception e) {
				LOGGER.debug("Exception :: {}", e);
			}
		}

		return photoList;
	}

}
