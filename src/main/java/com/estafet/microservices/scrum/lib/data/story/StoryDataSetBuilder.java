package com.estafet.microservices.scrum.lib.data.story;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.estafet.microservices.scrum.lib.data.task.TaskBuilder;

public class StoryDataSetBuilder {

	private List<List<String>> data;
	
	private Integer projectId;
	
	public StoryDataSetBuilder setProjectId(Integer projectId) {
		this.projectId = projectId;
		return this;
	}

	public StoryDataSetBuilder setData(List<List<String>> data) {
		this.data = data;
		return this;
	}

	public List<Story> build() {
		List<Story> stories = new ArrayList<Story>();
		Pattern p = Pattern.compile("(Task\\#\\d+)(\\s+)(\\[)(\\d+)(\\s+hours\\])");
		for (int i = 1; i < data.size(); i++) {
			String storyTitle = data.get(i).get(0);
			Integer storypoints = Integer.parseInt(data.get(i).get(1));
			Story story = new StoryBuilder()
							.setProjectId(projectId)
							.setTitle(storyTitle)
							.setStorypoints(storypoints)
							.build();
			for (String task : data.get(i).get(2).split(",")) {
				Matcher m = p.matcher(task.trim());
				if (m.find()) {
					new TaskBuilder()
						.setInitialHours(Integer.parseInt(m.group(4)))
						.setTitle(m.group(1))
						.setStoryId(story.getId())
						.build();		
				}
			}
			stories.add(story);
		}
		return stories;
	}
	
}
