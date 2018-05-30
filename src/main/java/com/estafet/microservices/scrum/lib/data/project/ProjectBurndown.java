package com.estafet.microservices.scrum.lib.data.project;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectBurndown {

	private int id;

	private String title;

	private List<ProjectBurndownSprint> sprints = new ArrayList<ProjectBurndownSprint>();

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

	public List<ProjectBurndownSprint> getSprints() {
		return sprints;
	}
	
	
}
