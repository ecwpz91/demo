package com.redhat.mrp.model;

import java.util.Arrays;

public class RoverList {

	private Rover[] rovers;

	public Rover[] getRovers() {
		return rovers;
	}

	public void setRovers(Rover[] rovers) {
		this.rovers = rovers;
	}

	@Override
	public String toString() {
		return Arrays.toString(rovers);
	}

}
