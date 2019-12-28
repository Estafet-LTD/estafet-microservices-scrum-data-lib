package com.estafet.microservices.scrum.lib.data.sprint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.estafet.microservices.scrum.lib.commons.wait.WaitUntil;
import com.estafet.microservices.scrum.lib.data.project.Project;
import com.estafet.microservices.scrum.lib.data.story.Story;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprint {

	private Integer id;

	private String startDate;

	private String endDate;

	private Integer number;

	private String status;

	private Integer projectId;

	private Integer noDays;

	public Integer getId() {
		return id;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Integer getNumber() {
		return number;
	}

	public String getStatus() {
		return status;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getNoDays() {
		return noDays;
	}
	
	@JsonIgnore
	public String getName() {
		return "Sprint#" + id;
	}
	
	public static Sprint getSprint(Integer sprintId) {
		return new RestTemplate().getForObject(PropertyUtils.instance().getProperty("SPRINT_API_SERVICE_URI") + "/sprint/{id}",
				Sprint.class, sprintId);
	}
	
	public Story getStory(int storyId) {
		for (Story story : getStories()) {
			if (story.getId() == storyId) {
				return story;
			}
		}
		return null;
	}
	
	@JsonIgnore
	public SprintBurndown getSprintBurndown() {
		SprintBurndown burndown = new RestTemplate().getForObject(PropertyUtils.instance().getProperty("SPRINT_BURNDOWN_SERVICE_URI") + "/sprint/{id}/burndown",
				SprintBurndown.class, id);
		return burndown;
	}
	
	@JsonIgnore
	public List<Story> getStories() {
		List<Story> stories = new ArrayList<Story>();
		for (Story story : Project.getProjectById(projectId).getStories()) {
			if (story.getSprintId() != null && story.getSprintId().equals(id)) {
				stories.add(story);
			}
		}
		return stories;
	}
	
	public void complete() {
		for (Story story : getStories()) {
			story.complete();
		}
		new WaitUntil() {
			public boolean success() {
				return Sprint.getSprint(id).getStatus().equals("Completed") && Sprint.getSprint(id+1) != null;
			}
		}.start();
	}

}
