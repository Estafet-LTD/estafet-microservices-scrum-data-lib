package com.estafet.microservices.scrum.lib.data.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.estafet.microservices.scrum.lib.commons.wait.WaitUntil;
import com.estafet.microservices.scrum.lib.data.db.ServiceDatabases;
import com.estafet.microservices.scrum.lib.data.sprint.Sprint;
import com.estafet.microservices.scrum.lib.data.story.Story;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

	private Integer id;

	private String title;

	private Integer noSprints;

	private Integer sprintLengthDays;

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Integer getNoSprints() {
		return noSprints;
	}

	public Integer getSprintLengthDays() {
		return sprintLengthDays;
	}
	
	public static Project getProjectById(Integer projectId) {
		for (Project project : getProjects()) {
			if (project.getId().equals(projectId)) {
				return project;
			}
		}
		return null;
	}
	
	public static Project getProjectByTitle(String title) {
		for (Project project : getProjects()) {
			if (project.getTitle().equals(title)) {
				return project;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Project> getProjects() {
		List objects = new RestTemplate().getForObject(PropertyUtils.instance().getProperty("PROJECT_API_SERVICE_URI") + "/projects",
				List.class);
		List<Project> projects = new ArrayList<Project>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Project project = mapper.convertValue(object, new TypeReference<Project>() {
			});
			projects.add(project);
		}
		return projects;
	}

	@JsonIgnore
	public ProjectBurndown getBurndown() {
		ProjectBurndown burndown = new RestTemplate().getForObject(
				PropertyUtils.instance().getProperty("PROJECT_BURNDOWN_SERVICE_URI") + "/project/{id}/burndown", ProjectBurndown.class,
				id);
		return burndown;
	}
	
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public List<Sprint> getSprints() {
		List objects = new RestTemplate().getForObject(PropertyUtils.instance().getProperty("SPRINT_API_SERVICE_URI") + "/project/{id}/sprints",
				List.class, id);
		List<Sprint> sprints = new ArrayList<Sprint>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Sprint sprint = mapper.convertValue(object, new TypeReference<Sprint>() {
			});
			sprints.add(sprint);
		}
		return sprints;
	}
	
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public List<Story> getStories() {
		List objects = new RestTemplate().getForObject(PropertyUtils.instance().getProperty("STORY_API_SERVICE_URI") + "/project/{id}/stories",
				List.class, id);
		List<Story> stories = new ArrayList<Story>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Story story = mapper.convertValue(object, new TypeReference<Story>() {
			});
			stories.add(story);
		}
		return stories;
	}
	
	public Story getStory(String storyTitle) {
		for (Story story : getStories()) {
			if (story.getTitle().equals(storyTitle)) {
				return story;
			}
		}
		return null;
	}
	
	@JsonIgnore
	public Sprint getActiveSprint() {
		for (Sprint sprint : getSprints()) {
			if (sprint.getStatus().equals("Active")) {
				return sprint;
			}
		}
		return null;
	}
		
	public Sprint getSprint(String name) {
		for (Sprint sprint : getSprints()) {
			if (sprint.getName().equals(name)) {
				return sprint;
			}
		}
		return null;
	}
	
	public void newProjectWait() {
		
		new WaitUntil() {
			public boolean success() {
				List<Sprint> sprints = getSprints();
				return !sprints.isEmpty() && sprints.get(0).getStatus().equals("Active");
			}
		}.start();
		
		new WaitUntil() {
			public boolean success() {
				return ServiceDatabases.exists("project-burndown", "project_burndown", "project_burndown_id", id);
			}
		}.start();
		
		Integer sprintId = getSprints().get(0).getId();
		
		new WaitUntil() {
			public boolean success() {
				return ServiceDatabases.exists("sprint-burndown", "sprint", "sprint_id", sprintId);
			}
		}.start();
	}

	Project setId(Integer id) {
		this.id = id;
		return this;
	}

	Project setTitle(String title) {
		this.title = title;
		return this;
	}

	Project setNoSprints(Integer noSprints) {
		this.noSprints = noSprints;
		return this;
	}

	Project setSprintLengthDays(Integer sprintLengthDays) {
		this.sprintLengthDays = sprintLengthDays;
		return this;
	}

}
