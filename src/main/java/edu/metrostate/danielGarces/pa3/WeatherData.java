package edu.metrostate.danielGarces.pa3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * The WeatherData class stores the weather information of one particular day.
 * 
 * @author Daniel Garces
 *
 */
public class WeatherData implements Comparable<WeatherData> {
	String id;
	int year;
	int month;
	int day;
	String element;
	int value;
	String qflag;
	BigDecimal decimalValue;
	String thisLine;

	public WeatherData() throws IOException {
	}

	/**
	 * The maxTemperatures method calculates the top 5 days with the maximum
	 * temperatures of one file and returns them.
	 * 
	 * @param file
	 * @param startDate
	 * @param endDate
	 * @return top 5 maximum temperatures
	 * @throws IOException
	 */
	public ArrayList<WeatherData> maxTemperatures(File file, Date startDate, Date endDate) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file)); // read the file
		ArrayList<WeatherData> list = new ArrayList<WeatherData>();
		while ((thisLine = in.readLine()) != null) {
			int days = (thisLine.length() - 21) / 8; // Calculate the number of days in the line
			for (int i = 0; i < days; i++) { // Process each day in the line.
				Integer value = Integer.valueOf(thisLine.substring(21 + 8 * i, 26 + 8 * i).trim());
				String qflag = thisLine.substring(27 + 8 * i, 28 + 8 * i);
				if (qflag.equals(" ") && value != -9999) {
					WeatherData wd = new WeatherData();
					wd.day = i + 1;
					wd.id = thisLine.substring(0, 11);
					wd.year = Integer.valueOf(thisLine.substring(11, 15).trim());
					wd.month = Integer.valueOf(thisLine.substring(15, 17).trim());
					wd.element = thisLine.substring(17, 21);
					wd.value = value;
					wd.qflag = qflag;
					BigDecimal decimal = new BigDecimal(wd.value);
					wd.decimalValue = decimal.movePointLeft(1);
					Date date = new Date(wd.year, wd.month, wd.day);
					if (isDateValid(startDate, endDate, date) && wd.element.equals("TMAX")) { // check if in date range
						if (list.size() < 5) {
							list.add(wd);
						} else {
							for (int j = 0; j < 5; j++) {
								if (list.get(j).value < wd.value) {
									list.set(j, wd);
									break;
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * The minTemperatures method calculates the top 5 days with the minimum
	 * temperatures of one file and returns them.
	 * 
	 * @param file
	 * @param startDate
	 * @param endDate
	 * @return top 5 minimum temperatures
	 * @throws IOException
	 */
	public ArrayList<WeatherData> minTemperatures(File file, Date startDate, Date endDate) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file)); // read the file
		ArrayList<WeatherData> list = new ArrayList<WeatherData>();
		while ((thisLine = in.readLine()) != null) {
			int days = (thisLine.length() - 21) / 8; // Calculate the number of days in the line
			for (int i = 0; i < days; i++) { // Process each day in the line.
				Integer value = Integer.valueOf(thisLine.substring(21 + 8 * i, 26 + 8 * i).trim());
				String qflag = thisLine.substring(27 + 8 * i, 28 + 8 * i);
				if (qflag.equals(" ") && value != -9999) {
					WeatherData wd = new WeatherData();
					wd.day = i + 1;
					wd.id = thisLine.substring(0, 11);
					wd.year = Integer.valueOf(thisLine.substring(11, 15).trim());
					wd.month = Integer.valueOf(thisLine.substring(15, 17).trim());
					wd.element = thisLine.substring(17, 21);
					wd.value = value;
					wd.qflag = qflag;
					BigDecimal decimal = new BigDecimal(wd.value);
					wd.decimalValue = decimal.movePointLeft(1);
					Date date = new Date(wd.year, wd.month, wd.day);
					if (isDateValid(startDate, endDate, date) && wd.element.equals("TMIN")) { // check if in date range
						if (list.size() < 5) {
							list.add(wd);
						} else {
							for (int j = 0; j < 5; j++) {
								if (list.get(j).value > wd.value) {
									list.set(j, wd);
									break;
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * The isDateValid method compares the date to the start date and end date to
	 * check if it is in the valid range.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param date
	 * @return true if date is in the range, false otherwise
	 */
	public static boolean isDateValid(Date startDate, Date endDate, Date date) {
		return !(date.before(startDate) || date.after(endDate));
	}

	@Override
	public String toString() {
		return "id=" + id + ", year=" + year + ", month=" + month + ", day=" + day + ", element=" + element + ", value="
				+ decimalValue + "C" + ", qflag=" + qflag;
	}

	@Override
	public int compareTo(WeatherData o) {
		if (this.value == o.value)
			return 0;
		else if (this.value > o.value)
			return -1;
		else
			return 1;
	}
}
