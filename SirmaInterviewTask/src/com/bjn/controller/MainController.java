package com.bjn.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.bjn.data.EmployeeCouple;
import com.bjn.data.RawData;

public class MainController {
	private Map<Integer, List<RawData>> rawDataMap; // key: projectId, data:
													// list of initial data

	private Map<Integer, List<EmployeeCouple>> couplesMap; // key : projectId,
															// data: list of
															// Employee couples

	public void createRawDataMap(Collection<RawData> data) {
		rawDataMap = new HashMap<Integer, List<RawData>> ();
		for (RawData row : data) {
			List<RawData> projList = rawDataMap.get (row.getProjectId ());
			if (projList == null) {
				projList = new LinkedList<RawData> ();
				rawDataMap.put (row.getProjectId (), projList);
			}
			projList.add (row);
		}
	}

	private LocalDate laterDate(LocalDate date1, LocalDate date2) {
		return (date1.compareTo (date2) > 0) ? date1 : date2;
	}

	private LocalDate earlyerDate(LocalDate date1, LocalDate date2) {
		return (date1.compareTo (date2) < 0) ? date1 : date2;
	}

	public void createCouplesMap() {
		couplesMap = new HashMap<Integer, List<EmployeeCouple>> ();
		for (Integer projectId : rawDataMap.keySet ()) {
			couplesMap.put (projectId, createCouplesListForProject (rawDataMap.get (projectId)));
		}
	}

	private int daysBetween(RawData current, RawData next) {
		LocalDate laterStartDate = null;
		LocalDate earlyerEndDate = null;
		laterStartDate = laterDate (current.getStartDate (), next.getStartDate ());
		earlyerEndDate = earlyerDate (current.getEndDate (), next.getEndDate ());
		int days = (int) ChronoUnit.DAYS.between (laterStartDate, earlyerEndDate);
		return days;
	}

	private List<EmployeeCouple> createCouplesListForProject(List<RawData> employees) {
		List<EmployeeCouple> couples = new LinkedList<EmployeeCouple> ();
		Collections.sort (employees, (e1, e2) -> e1.getEmployeeId () - e2.getEmployeeId ());

		for (int i = 0; i < employees.size (); i++) {
			RawData current = employees.get (i);
			for (int j = i + 1; j < employees.size (); j++) {
				RawData next = employees.get (j);
				int days = daysBetween (current, next);
				if (days > 0) {
					EmployeeCouple employeeCouple = checkIfEmployeeCoupleExists (couples, current, next);
					if (employeeCouple == null) {
						employeeCouple = createEmployeeCouple (current, next);
					}
					employeeCouple.addEngagemant (days);
					couples.add (employeeCouple);
				}
			}
		}
		return couples;
	}

	private EmployeeCouple checkIfEmployeeCoupleExists(List<EmployeeCouple> list, RawData emp1, RawData emp2) {
		for (EmployeeCouple emp : list) {
			if (emp.getEmployee1Id () == emp1.getEmployeeId () && emp.getEmployee2Id () == emp2.getEmployeeId ()) {
				return emp;
			}
		}
		return null;
	}

	private EmployeeCouple createEmployeeCouple(RawData current, RawData next) {
		EmployeeCouple employeeCouple = new EmployeeCouple ();
		employeeCouple.setEmployee1Id (current.getEmployeeId ());
		employeeCouple.setEmployee2Id (next.getEmployeeId ());
		employeeCouple.setProjectId (current.getProjectId ());
		return employeeCouple;
	}

	public Collection<EmployeeCouple> getCouples() {
		Collection<EmployeeCouple> result = new LinkedList<EmployeeCouple> ();
		createCouplesMap ();
		for (Integer i : couplesMap.keySet ()) {
			List<EmployeeCouple> res = couplesMap.get (i);
			int max = 0;
			EmployeeCouple emp = null;
			for (EmployeeCouple c : res) {
				if (c.calculateTotalEngagemant () > max) {
					max = c.calculateTotalEngagemant ();
					emp = c;
				}
			}
			if (emp != null) {
				result.add (emp);
			}
		}
		return result;
	}
}
