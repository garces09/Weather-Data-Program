package edu.metrostate.danielGarces.pa3;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * The MyCallable class implements the Callable interface. It executes the
 * future created for each weather data file.
 * 
 * @author Daniel Garces
 *
 */
public class MyCallable implements Callable {
	String maxOrMin;
	File file;
	Date startDate, endDate;

	public MyCallable(String maxOrMin, File files, Date startDate, Date endDate) {
		this.maxOrMin = maxOrMin;
		this.file = files;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public ArrayList<WeatherData> call() throws Exception {
		WeatherData wd = new WeatherData();
		if (maxOrMin.equalsIgnoreCase("max") || maxOrMin.equalsIgnoreCase("maximum"))
			return wd.maxTemperatures(file, startDate, endDate); // return top 5 max temperatures in the file
		else
			return wd.minTemperatures(file, startDate, endDate); // else return top 5 min temperatures in the file
	}

}
