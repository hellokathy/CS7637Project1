package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Releationship {
	public ArrayList<Map<String, RavensAttribute>> similaritesList;
	public ArrayList<Map<String, RavensAttribute>> differencesList;
	public ArrayList<RavensObject> shapesDeleted;
	public ArrayList<RavensObject> shapesAdded;
	public int totalSidesA;
	public int totalSidesB;
	
	
	public Releationship() {
		// TODO Auto-generated constructor stub
		similaritesList = new ArrayList<Map<String, RavensAttribute>>();
		differencesList = new ArrayList<Map<String, RavensAttribute>>();
		shapesDeleted = new ArrayList<RavensObject>();
		shapesAdded = new ArrayList<RavensObject>();
		
	}

	public void addSimilarity(Map<String, RavensAttribute> map) {
		similaritesList.add(map);
	}

	public void addDifference(Map<String, RavensAttribute>  map) {
		similaritesList.add(map);
	}
}
