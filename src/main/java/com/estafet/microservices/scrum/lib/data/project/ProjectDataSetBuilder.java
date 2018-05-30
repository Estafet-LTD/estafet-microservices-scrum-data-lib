package com.estafet.microservices.scrum.lib.data.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectDataSetBuilder {

	private List<List<String>> data;
	
	public ProjectDataSetBuilder setData(List<List<String>> data) {
		this.data = data;
		return this;
	}

	public List<Project> build() {
		List<Project> projects = new ArrayList<Project>();
		for (int i = 1; i < data.size(); i++) {
			List<String> row = data.get(i);
			Project project = new ProjectBuilder()
							.setTitle(row.get(0))
							.setNoSprints(Integer.parseInt(row.get(1)))
							.setSprintLengthDays(Integer.parseInt(row.get(2)))
							.build();
			projects.add(project);
		}
		return projects;
	}
	
}
