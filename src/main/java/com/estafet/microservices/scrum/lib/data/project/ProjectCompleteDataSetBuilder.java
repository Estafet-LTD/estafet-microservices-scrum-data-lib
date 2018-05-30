package com.estafet.microservices.scrum.lib.data.project;

import java.util.List;

import com.estafet.microservices.scrum.lib.data.story.Story;
import com.estafet.microservices.scrum.lib.data.story.StoryBuilder;
import com.estafet.microservices.scrum.lib.data.task.TaskBuilder;

public class ProjectCompleteDataSetBuilder {

	private List<List<String>> data;
	private String projectTitle;
	
	public ProjectCompleteDataSetBuilder setData(List<List<String>> data) {
		this.data = data;
		return this;
	}

	public ProjectCompleteDataSetBuilder setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
		return this;
	}
	
	public Project build() {
		Project project = Project.getProjectByTitle(projectTitle);
		for (int i = 1; i < data.size(); i++) {
			String storyTitle = data.get(i).get(0);
			Integer storypoints = Integer.parseInt(data.get(i).get(1));
			Story story = new StoryBuilder()
							.setProjectId(project.getId())
							.setTitle(storyTitle)
							.setStorypoints(storypoints)
							.build();
			new TaskBuilder()
				.setStoryId(story.getId())
				.build();
		}
		String previousSprint = project.getActiveSprint().getName();
		for (int i = 1; i < data.size(); i++) {
			String sprint = data.get(i).get(2);
			String storyTitle = data.get(i).get(0);
			if (!sprint.equals(previousSprint)) {
				project.getSprint(previousSprint).complete();
				project.getStory(storyTitle).addToSprint(project.getSprint(sprint).getId());
			} else {
				project.getStory(storyTitle).addToSprint(project.getSprint(previousSprint).getId());
			}
			previousSprint = sprint;
		}
		project.getSprint(previousSprint).complete();
		return project;
	}
	
}
