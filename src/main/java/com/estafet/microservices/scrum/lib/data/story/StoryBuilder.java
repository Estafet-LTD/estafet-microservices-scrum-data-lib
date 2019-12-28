package com.estafet.microservices.scrum.lib.data.story;

import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.estafet.microservices.scrum.lib.commons.wait.WaitUntil;
import com.estafet.microservices.scrum.lib.data.db.ServiceDatabases;

public class StoryBuilder {

	private String title= "my title";

	private String description = "my description";

	private Integer storypoints;

	private Integer projectId;

	public StoryBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public StoryBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public StoryBuilder setStorypoints(Integer storypoints) {
		this.storypoints = storypoints;
		return this;
	}

	public StoryBuilder setProjectId(Integer projectId) {
		this.projectId = projectId;
		return this;
	}

	public Story build() {
		Story story = new RestTemplate().postForObject(PropertyUtils.instance().getProperty("STORY_API_SERVICE_URI") + "/project/{id}/story",
				new Story().setDescription(description).setTitle(title).setStorypoints(storypoints),
				Story.class, projectId);
		new WaitUntil() {
			public boolean success() {
				return ServiceDatabases.exists("task-api", "story", "story_id", story.getId());
			}
		}.start();
		return story;		
	}

}
