package edu.metrostate.danielGarces.pa3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The StationData class stores the information of one weather station.
 * 
 * @author Daniel Garces
 *
 */
public class StationData {
	String id;
	float latitude;
	float longitude;
	float elevation;
	String state;
	String name;
	String thisLine;

	public StationData() throws IOException {
	}

	/**
	 * The processStationData method processes the station data information of the
	 * ghcnd-stations.txt file and returns an ArrayList containing the information
	 * for each weather station.
	 * 
	 * @return all weather stations in the file
	 * @throws IOException
	 */
	public ArrayList<StationData> processStationData() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("weather_data/ghcnd-stations.txt")); // read file
		ArrayList<StationData> list = new ArrayList<StationData>();
		while ((thisLine = in.readLine()) != null) {
			StationData sd = new StationData();
			sd.id = thisLine.substring(0, 11);
			sd.latitude = Float.valueOf(thisLine.substring(12, 20).trim());
			sd.longitude = Float.valueOf(thisLine.substring(21, 30).trim());
			sd.elevation = Float.valueOf(thisLine.substring(31, 37).trim());
			sd.state = thisLine.substring(38, 40);
			sd.name = thisLine.substring(41, 71);
			list.add(sd);
		}
		return list;
	}

	@Override
	public String toString() {
		return "id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", elevation=" + elevation
				+ ", state=" + state + ", name=" + name;
	}

}
