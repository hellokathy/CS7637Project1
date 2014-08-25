package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Releationship {
	//its all about the meta......... 
	public ArrayList<Map<String, RavensAttribute>> similaritesList;
	public ArrayList<Map<String, RavensAttribute>> differencesList;
	public ArrayList<RavensObject> shapesDeleted;
	public ArrayList<RavensObject> shapesAdded;
	public RavensFigure objA;
	public RavensFigure objB;
	public int totalSidesA;
	public int totalSidesB;
	
	public Releationship(RavensFigure objA, RavensFigure objB) {

		similaritesList = new ArrayList<Map<String, RavensAttribute>>();
		differencesList = new ArrayList<Map<String, RavensAttribute>>();
		shapesDeleted = new ArrayList<RavensObject>();
		shapesAdded = new ArrayList<RavensObject>();
		
		this.objA = objA;
		this.objB = objB;
		
		buildReleationship();
		logRelationship();
		
	}

	public void addSimilarity(Map<String, RavensAttribute> map) {
		similaritesList.add(map);
	}

	public void addDifference(Map<String, RavensAttribute>  map) {
		similaritesList.add(map);
	}
	public Releationship buildReleationship() {
		//Releationship releationship = getReleationship(objA.getName(), objB.getName());
		
		for (RavensObject A: objA.getObjects()) {
			for (RavensObject B: objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					for (RavensAttribute aAttribute : A.getAttributes()) {
						for (RavensAttribute bAttribute : B.getAttributes()) {
							Map<String, RavensAttribute> map = new HashMap<String, RavensAttribute>();
							if (aAttribute.getName().equals(bAttribute.getName()) &&
									aAttribute.getValue().equals(bAttribute.getValue())) {
									map.put(A.getName(), aAttribute);
									this.addSimilarity(map);								
							}
							else {
								if (aAttribute.getName().equals(bAttribute.getName())) {
									map.put(A.getName(), bAttribute);
									this.differencesList.add(map);		
								}
							}
						}
					}
				}
			}
		}	
			
		//get shapes deleted between x and y
		for (RavensObject A: objA.getObjects()) {
			this.shapesDeleted.add(A);
			for (RavensObject B: objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					this.shapesDeleted.remove(A);
					break;
				}
			}	
		}
		//get shapes added between x and y
		for (RavensObject A: objB.getObjects()) {
			this.shapesAdded.add(A);
			for (RavensObject B: objA.getObjects()) {
				if (A.getName().equals(B.getName())) {
					this.shapesAdded.remove(A);
					break;
				}
			}	
		}
		return this;	
	}

	public int getSides(RavensFigure obj) {
		int i = 0;
		for (RavensObject A: obj.getObjects()) {
			for (RavensAttribute aAttribute : A.getAttributes()) {
				if (aAttribute.getName().equals("shape")) {
					i += getSides(aAttribute.getValue()) ;
				}	
			}	
		}
		return i;
	}
	
	private void logRelationship() {
		System.out.println("Total Sides fig."+ objA.getName() + " " + getSides(objA));
		System.out.println("Total Sides fig." + objB.getName() + " " + getSides(objB));
		System.out.println();
		System.out.println("Similarities:");
		for (Map<String, RavensAttribute>  sim : this.similaritesList) {
			Iterator<Entry<String, RavensAttribute>> it = sim.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, RavensAttribute> pairs = (Map.Entry<String, RavensAttribute>) it
						.next();
				System.out.println((pairs.getKey() + " " + pairs.getValue().getName() + " " + pairs.getValue().getValue()));
			}
			
		}
	
		System.out.println();
		System.out.println("Differences:");
		for (Map<String, RavensAttribute>  diff : this.differencesList) {
			Iterator<Entry<String, RavensAttribute>> it = diff.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, RavensAttribute> pairs = (Map.Entry<String, RavensAttribute>) it
						.next();
				System.out.println((pairs.getKey() + " " + pairs.getValue().getName() + " " + pairs.getValue().getValue()));
			}
		}
		System.out.println();
		System.out.println("ShapesAdded:");
		for (RavensObject obj : this.shapesAdded) {
			System.out.println(obj.getName());
		}
		System.out.println();
		System.out.println("ShapesDeleted:");
		for (RavensObject obj : this.shapesDeleted) {
			System.out.println(obj.getName());
		}
		System.out.println();
	
	}
	public int getSides(String shape) {

		switch (shape) {
		case "triangle":
			return 3;
		case "square":
			return 4;
		case "diamond":
			return 4;
		case "pentagon":
			return 5;
		case "half-arrow":
			return 5;
		case "hexagon":
			return 6;
		case "heptagon":
			return 7;
		case "arrow":
			return 7;
		case "octagon":
			return 8;
		case "nonagon":
			return 9;
		case "decagon":
			return 10;
		default:
			break;
		}
		return 0;

	}

}
