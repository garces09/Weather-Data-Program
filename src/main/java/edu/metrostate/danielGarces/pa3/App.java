package edu.metrostate.danielGarces.pa3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The App class contains the main method of the program. It prints the top 5
 * minimum or maximum temperatures in the range provided by the user input.
 * 
 * @author Daniel Garces
 *
 */
public class App {
	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Starting Year: ");
		int startYear = scanner.nextInt();
		System.out.println("Ending Year: ");
		int endYear = scanner.nextInt();
		System.out.println("Starting Month: ");
		int startMonth = scanner.nextInt();
		System.out.println("Ending Month: ");
		int endMonth = scanner.nextInt();
		System.out.println("Maximum or Minimum: ");
		String maxOrMin = scanner.next();
		System.out.println("\nCalculating the top five results...\n");
		Date startDate = new Date(startYear, startMonth, 1);
		Date endDate = new Date(endYear, endMonth, 32);
		File folder = new File("weather_data/ghcnd_hcn/"); // reads folder containing all the weather files
		File[] files = folder.listFiles(); // creates an array of all the weather files
		ExecutorService service = Executors.newFixedThreadPool(10); // creates a thread pool of 10 threads
		ConcurrentLinkedQueue<Future<ArrayList<WeatherData>>> futures = new ConcurrentLinkedQueue<Future<ArrayList<WeatherData>>>();
		for (int i = 0; i < files.length; i++) {
			// creates a new MyCallable object for each file
			Callable<WeatherData> callable = new MyCallable(maxOrMin, files[i], startDate, endDate);
			Future future = service.submit(callable); // submits callable for execution
			futures.add(future); // adds future to the queue
		}
		ArrayList list = new ArrayList();
		for (Future fut : futures) {
			try {
				list.add(fut.get()); // get the future's results and add them to the list
			} catch (ExecutionException e) {
				((Throwable) e).printStackTrace();
			}
		}
		ArrayList<WeatherData> wdList = new ArrayList<WeatherData>();
		for (int i = 0; i < list.size(); i++) {
			ArrayList<WeatherData> tempList = new ArrayList<WeatherData>();
			tempList = (ArrayList<WeatherData>) list.get(i);
			if (tempList.size() == 5) {
				for (int j = 0; j < 5; j++) {
					wdList.add(tempList.get(j)); // extract each WeatherData object and add it to the least
				}
			}
		}
		// divide list in 4
		List dividedLists = new ArrayList();
		List dividedList1 = new ArrayList();
		int listSize = wdList.size();
		dividedList1 = wdList.subList(0, listSize / 4); // first 1/4
		dividedLists.add(dividedList1);
		List dividedList2 = new ArrayList();
		dividedList2 = wdList.subList((listSize / 4) + 1, listSize / 2); // second 1/4
		dividedLists.add(dividedList2);
		List dividedList3 = new ArrayList();
		dividedList3 = wdList.subList((listSize / 2) + 1, listSize - (listSize / 4)); // third 1/4
		dividedLists.add(dividedList3);
		List dividedList4 = new ArrayList();
		dividedList4 = wdList.subList(listSize - (listSize / 4) + 1, listSize - 1); // fourth 1/4
		dividedLists.add(dividedList4);
		ConcurrentLinkedQueue<Future<ArrayList<WeatherData>>> futures2 = new ConcurrentLinkedQueue<Future<ArrayList<WeatherData>>>();
		for (int i = 0; i < 4; i++) {
			// creates a new Task object for each 1/4 of the list
			Callable<WeatherData> callable = new Task((List) dividedLists.get(i), maxOrMin);
			Future future = service.submit(callable); // submits callable for execution
			futures2.add(future); // add future to the queue
		}
		ArrayList finalList = new ArrayList();
		for (Future fut : futures2) {
			try {
				finalList.add(fut.get()); // get future's results and add them to the list
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		ArrayList<WeatherData> top20 = new ArrayList<WeatherData>();
		ArrayList<WeatherData> top5 = new ArrayList<WeatherData>();
		for (int i = 0; i < finalList.size(); i++) {
			ArrayList<WeatherData> tempList = new ArrayList<WeatherData>();
			tempList = (ArrayList<WeatherData>) finalList.get(i);
			for (int j = 0; j < 5; j++) {
				top20.add(tempList.get(j)); // extract each WeatherData object and add it to the list
			}
		}
		if (maxOrMin.equalsIgnoreCase("max") || maxOrMin.equalsIgnoreCase("maximum")) {
			Collections.sort(top20); // sort the top 20 results
			for (int i = 0; i < 5; i++)
				top5.add(top20.get(i)); // add the top 5 max results to the list
		} else {
			Collections.sort(top20); // sort the top 20 results
			Collections.reverse(top20); // reverse the top 20 results
			for (int i = 0; i < 5; i++)
				top5.add(top20.get(i)); // add the top 5 min results to the list
		}
		StationData sd = new StationData();
		ArrayList<StationData> sdList = new ArrayList<StationData>();
		sdList = sd.processStationData(); // process the station data file and return an ArrayList of weather stations
		for (int i = 0; i < top5.size(); i++) {
			System.out.println(top5.get(i));
			for (int j = 0; j < sdList.size(); j++) {
				if (top5.get(i).id.equals(sdList.get(j).id)) { // if id of WeatherData matches station's id
					System.out.println(sdList.get(j));
					break;
				}
			}
		}
		service.shutdown();
	}
}
