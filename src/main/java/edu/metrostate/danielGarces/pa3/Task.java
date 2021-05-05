package edu.metrostate.danielGarces.pa3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The Task class implements the Callable interface. It executes the futures
 * created to iterate over the list of results and return the top 5 results.
 * 
 * @author Daniel Garces
 *
 */
public class Task implements Callable {
	List list = new ArrayList();
	String maxOrMin;

	public Task(List list, String maxOrMin) {
		this.list = list;
		this.maxOrMin = maxOrMin;
	}

	@Override
	public List call() throws Exception {
		List top5 = new ArrayList();
		if (maxOrMin.equalsIgnoreCase("max") || maxOrMin.equalsIgnoreCase("maximum")) {
			Collections.sort(list); // sort the list
			for (int i = 0; i < 5; i++)
				top5.add(list.get(i));
			return top5; // return the top 5 max temperatures
		} else {
			Collections.sort(list); // sort the list
			Collections.reverse(list); // reverse the list
			for (int i = 0; i < 5; i++)
				top5.add(list.get(i));
			return top5; // return the top 5 min temperatures
		}
	}
}
