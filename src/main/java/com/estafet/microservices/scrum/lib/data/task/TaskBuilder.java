package com.estafet.microservices.scrum.lib.data.task;

import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.estafet.microservices.scrum.lib.commons.wait.WaitUntil;
import com.estafet.microservices.scrum.lib.data.story.Story;

public class TaskBuilder {

	private String title = "my title";

	private String description = "my description";

	private Integer initialHours = 13;

	private Integer storyId;

	public Task build() {
		Task task = new RestTemplate().postForObject(PropertyUtils.instance().getProperty("TASK_API_SERVICE_URI") + "/story/{id}/task",
				new Task().setTitle(title).setDescription(description).setInitialHours(initialHours), Task.class,
				storyId);
		new WaitUntil() {
			public boolean success() {
				Story story = Story.getStory(storyId);
				return story.getStatus().equals("In Progress") || story.getStatus().equals("Planning");
			}
		}.start();
		return task;
	}

	public TaskBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public TaskBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public TaskBuilder setInitialHours(Integer initialHours) {
		this.initialHours = initialHours;
		return this;
	}

	public TaskBuilder setStoryId(Integer storyId) {
		this.storyId = storyId;
		return this;
	}

}
