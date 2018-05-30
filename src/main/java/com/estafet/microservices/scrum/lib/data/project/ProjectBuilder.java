package com.estafet.microservices.scrum.lib.data.project;

import org.springframework.web.client.RestTemplate;

public class ProjectBuilder {

	private String title;

	private Integer noSprints;

	private Integer sprintLengthDays;

	public ProjectBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public ProjectBuilder setNoSprints(Integer noSprints) {
		this.noSprints = noSprints;
		return this;
	}

	public ProjectBuilder setSprintLengthDays(Integer sprintLengthDays) {
		this.sprintLengthDays = sprintLengthDays;
		return this;
	}

	public Project build() {
		Project project =  new RestTemplate().postForObject(System.getenv("PROJECT_API_SERVICE_URI") + "/project",
				new Project().setTitle(title).setSprintLengthDays(sprintLengthDays).setNoSprints(noSprints),
				Project.class);
		project.newProjectWait();
		return project;
	}
	
	

}
