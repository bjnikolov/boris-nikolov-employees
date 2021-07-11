package com.bjn.data;

import java.util.Collection;
import java.util.LinkedList;

public class EmployeeCouple {

	private int employee1Id;

	private int employee2Id;

	Collection<Integer> engagemant = new LinkedList<Integer> ();

	private int projectId;

	public int getEmployee1Id() {
		return this.employee1Id;
	}

	public void setEmployee1Id(int employee1Id) {
		this.employee1Id = employee1Id;
	}

	public int getEmployee2Id() {
		return this.employee2Id;
	}

	public void setEmployee2Id(int employee2Id) {
		this.employee2Id = employee2Id;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;

	}

	public void addEngagemant(int days) {
		engagemant.add (days);
	}

	public int calculateTotalEngagemant() {
		int totalTime = 0;
		for (Integer i : engagemant) {
			totalTime += i;
		}

		return totalTime;
	}

}
