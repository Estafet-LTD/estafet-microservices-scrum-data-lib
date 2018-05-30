package com.estafet.microservices.scrum.lib.data.story;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.scrum.lib.commons.wait.WaitUntil;
import com.estafet.microservices.scrum.lib.data.db.ServiceDatabases;
import com.estafet.microservices.scrum.lib.data.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

	private int id;

	private String title;

	private String description;

	private Integer storypoints;

	private String status;

	private Integer sprintId;

	private Integer projectId;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStorypoints() {
		return storypoints;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public String getStatus() {
		return status;
	}

	public static Story getStory(Integer storyId) {
		return new RestTemplate().getForObject(System.getenv("STORY_API_SERVICE_URI") + "/story/{id}", Story.class,
				storyId);
	}

	@SuppressWarnings("rawtypes")
	public List<Task> getTasks() {
		List objects = new RestTemplate().getForObject(System.getenv("TASK_API_SERVICE_URI") + "/story/{storyId}/tasks",
				List.class, id);
		List<Task> tasks = new ArrayList<Task>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Task task = mapper.convertValue(object, new TypeReference<Task>() {
			});
			task.setSprintId(sprintId);
			tasks.add(task);
		}
		return tasks;
	}

	public void addToSprint(Integer sprintId) {
		this.sprintId = sprintId;
		new RestTemplate().postForObject(System.getenv("STORY_API_SERVICE_URI") + "/add-story-to-sprint",
				new AddSprintStory().setSprintId(sprintId).setStoryId(id), Story.class);
		new WaitUntil() {
			public boolean success() {
				return ServiceDatabases.exists("sprint-api", "story", "story_id", id);
			}
		}.start();
	}

	public void complete() {
		for (Task task : getTasks()) {
			task.claim();
			task.complete();
		}
		new WaitUntil() {
			public boolean success() {
				return Story.getStory(id).getStatus().equals("Completed");
			}
		}.start();
	}

	Story setTitle(String title) {
		this.title = title;
		return this;
	}

	Story setDescription(String description) {
		this.description = description;
		return this;
	}

	Story setStorypoints(Integer storypoints) {
		this.storypoints = storypoints;
		return this;
	}

	Story setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
		return this;
	}

	Story setProjectId(Integer projectId) {
		this.projectId = projectId;
		return this;
	}

}
