package com.example.demo.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rover implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String name;

  @JsonProperty("landing_date")
  private String landingDate;

  @JsonProperty("launch_date")
  private String launchDate;

  private String status;

  @JsonProperty("max_sol")
  private String maxSol;

  @JsonProperty("max_date")
  private String maxDate;

  @JsonProperty("total_photos")
  private String totalPhotos;

  private Camera[] cameras;

  public Rover() {
    super();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLandingDate() {
    return landingDate;
  }

  public void setLandingDate(String landingDate) {
    this.landingDate = landingDate;
  }

  public String getLaunchDate() {
    return launchDate;
  }

  public void setLaunchDate(String launchDate) {
    this.launchDate = launchDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMaxSol() {
    return maxSol;
  }

  public void setMaxSol(String maxSol) {
    this.maxSol = maxSol;
  }

  public String getMaxDate() {
    return maxDate;
  }

  public void setMaxDate(String maxDate) {
    this.maxDate = maxDate;
  }

  public String getTotalPhotos() {
    return totalPhotos;
  }

  public void setTotalPhotos(String totalPhotos) {
    this.totalPhotos = totalPhotos;
  }

	public Camera[] getCameras() {
		return cameras;
	}

	public void setCameras(Camera[] cameras) {
		this.cameras = cameras;
	}

  @Override
	public String toString() {
		return "{id=" + id + ",name=" + name + ",landingDate=" + landingDate
				+ ",launchDate=" + launchDate + ",status=" + status + ",maxSol="
				+ maxSol + ",cameras=" + Arrays.toString(cameras) + "}";
	}

}
