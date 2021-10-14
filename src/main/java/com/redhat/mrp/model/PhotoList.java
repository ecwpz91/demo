package com.redhat.mrp.model;

import java.util.Arrays;

public class PhotoList {

  private Photo[] photos;

  public Photo[] getPhotos() {
    return photos;
  }

  public void setPhotos(Photo[] photos) {
    this.photos = photos;
  }

  @Override
  public String toString() {
    return "{photos=" + Arrays.toString(photos) + "}";
  }
}