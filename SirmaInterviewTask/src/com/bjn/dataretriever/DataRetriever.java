package com.bjn.dataretriever;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.LinkedList;

import com.bjn.data.RawData;

public class DataRetriever {
	private Collection<DateTimeFormatter> formats = new LinkedList<DateTimeFormatter> ();;

	public void createFormats() {
		DateTimeFormatter ddMMyyyy = DateTimeFormatter.ofPattern ("dd-MM-yyyy");
		DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern ("yyyy-MM-dd");
		DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern ("MM-dd-yyyy");
		formats.add (ddMMyyyy);		
		formats.add (yyyyMMdd);
		formats.add (MMddyyyy);
	}

	public Collection<RawData> retrieveData(File file) {
		BufferedReader reader = null;
		Collection<RawData> data = new LinkedList<RawData> ();
		try {
			reader = new BufferedReader (new FileReader (file));
			String line = null;
			reader.readLine (); // skip header line
			while ( (line = reader.readLine ()) != null) {
				line = line.trim ();
				if (!line.isEmpty ()) {
					RawData rawData = convertToRawData (line);
					data.add (rawData);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace ();
		} catch (IOException e) {
			e.printStackTrace ();
		} finally {
			try {
				reader.close ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
		return data;
	}

	private LocalDate formatDate(String parse) {
		LocalDate date = null;
		for (DateTimeFormatter format : formats) {
			try {
				date = LocalDate.parse (parse, format);
				break;
			} catch (DateTimeParseException e) {
				// this format does not fit
			}
		}
		return date;
	}

	private RawData convertToRawData(String line) {
		RawData data = new RawData ();
		String[] words = line.split (",");
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].trim ();
		}
		int employeeId = Integer.parseInt (words[0]);
		int projectId = Integer.parseInt (words[1]);
		LocalDate startDate = formatDate (words[2]);
		LocalDate endDate;
		if (words[3].equals ("NULL") || words[3].equals ("null")) {
			endDate = LocalDate.now ();
		} else {
			endDate = formatDate (words[3]);
		}
		data.setEmployeeId (employeeId);
		data.setProjectId (projectId);
		data.setStartDate (startDate);
		data.setEndDate (endDate);
		return data;
	}
}
