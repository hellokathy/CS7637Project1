package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Releationship {
	// its all about the meta.........
	public ArrayList<String> similaritiesList;
	public ArrayList<String> differencesList;
	public HashMap<String, Double> Weights;
	public ArrayList<RavensObject> shapesDeleted;
	public ArrayList<RavensObject> shapesAdded;
	public RavensFigure objA;
	public RavensFigure objB;
	public int totalSidesA;
	public int totalSidesB;
	public int totalObjectsA;
	public int totalObjectsB;

	public Releationship(RavensFigure objA, RavensFigure objB) {

		this.similaritiesList = new ArrayList<String>();
		this.differencesList = new ArrayList<String>();
		this.objA = objA;
		this.objB = objB;

		buildReleationship();
		logRelationship();

		Weights = new HashMap<String, Double>();
		Weights.put("Unchanged", 5.00);
		Weights.put("Reflected", 4.00);
		Weights.put("Scaled", 2.00);
		Weights.put("Deleted", 1.00);
		Weights.put("ShapeChanged", 0.00);
	}

	public Releationship buildReleationship() {
		
		for (RavensObject A : objA.getObjects()) {
			for (RavensObject B : objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					for (RavensAttribute aAttribute : A.getAttributes()) {
						for (RavensAttribute bAttribute : B.getAttributes()) {
							if (aAttribute.getName().equals(bAttribute.getName())
									&& aAttribute.getValue().equals(bAttribute.getValue())) {
								similaritiesList.add(A.getName() +":"+ aAttribute.getName() + ":" +aAttribute.getValue());
							} else {
								if (aAttribute.getName().equals(bAttribute.getName())) {
									differencesList.add(A.getName()+":"+ bAttribute.getName() + ":" +bAttribute.getValue());
								}
							}
						}
					}
				}
			}
		}
		this.totalObjectsA= objA.getObjects().size();
		this.totalObjectsB = objB.getObjects().size();	
		this.totalSidesA = getSides(objA);
		this.totalSidesB = getSides(objB);
		this.shapesDeleted = getShapesDeleted();
		this.shapesAdded = getShapesAdded();

		return this;
	}


	public ArrayList<RavensObject> getShapesDeleted() {

		// gets shapes deleted between x and y
		ArrayList<RavensObject> list = new ArrayList<RavensObject>();
		for (RavensObject A : objA.getObjects()) {
			list.add(A);
			for (RavensObject B : objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					list.remove(A);
					break;
				}
			}
		}
		return list;
	}

	public ArrayList<RavensObject> getShapesAdded() {

		// get shapes added between x and y
		ArrayList<RavensObject> list = new ArrayList<RavensObject>();
		for (RavensObject A : objB.getObjects()) {
			list.add(A);
			for (RavensObject B : objA.getObjects()) {
				if (A.getName().equals(B.getName())) {
					list.remove(A);
					break;
				}
			}
		}
		return list;
	}

	public String getAttributeValue(RavensObject a, String key) {

		for (RavensAttribute obj : a.getAttributes()) {
			if (obj.getName().equals(key.toLowerCase())) {
				return obj.getValue();
			}
		}
		return null;
	}
	
	public int getSides(RavensFigure obj) {
		int sides = 0;
		for (RavensObject A : obj.getObjects()) {
			for (RavensAttribute aAttribute : A.getAttributes()) {
				if (aAttribute.getName().equals("shape")) {
					sides += getSides(aAttribute.getValue());
				}
			}
		}
		return sides;
	}

	private void logRelationship() {
		System.out.println("Total Sides fig." + objA.getName() + " "
				+ getSides(objA));
		System.out.println("Total Sides fig." + objB.getName() + " "
				+ getSides(objB));
		System.out.println();
		System.out.println("Similarities:");

		for (String sim : this.similaritiesList) {
			String[] sarray = sim.split(":");
			System.out.println((sarray[0] + " " + sarray[1] + " " + sarray[2]));
		}

		System.out.println();
		System.out.println("Differences:");
		for (String diff : this.differencesList) {
			String[] sarray = diff.split(":");
			System.out.println((sarray[0] + " " + sarray[1] + " " + sarray[2]));
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

	public static double getRelationshipStrength(Releationship aReleationship,
			Releationship bReleationship) {

		double totalAttributes = getTotalAttributes(aReleationship);
		double matchStrength = 0;

		matchStrength += compareSides(aReleationship, bReleationship);
		matchStrength += compareSimilarities(aReleationship, bReleationship);
		matchStrength += compareDifferences(aReleationship, bReleationship);
		matchStrength += compareShapesAdded(aReleationship, bReleationship);
		matchStrength += compareShapesDeleted(aReleationship, bReleationship);

		// return strength of comparison
		return (matchStrength / totalAttributes) * 100;

	}

	public static double getTotalAttributes(Releationship releationship) {

		double totalAttributes = 0;
		totalAttributes += releationship.similaritiesList.size();
		totalAttributes += releationship.differencesList.size();

		if (releationship.totalSidesA == releationship.totalSidesB) {
			totalAttributes++;
		}
		else {
			if (releationship.totalSidesA % releationship.totalSidesB == 0 || 
					releationship.totalSidesB % releationship.totalSidesA == 0 ||
					releationship.totalSidesA / releationship.totalSidesB == 2 || 
					releationship.totalSidesB / releationship.totalSidesA == 2) {
				totalAttributes++;
			}
		}

		if (releationship.shapesAdded.size() == 0) {
			totalAttributes++;
		} else {
			totalAttributes += releationship.shapesAdded.size();
		}

		if (releationship.shapesDeleted.size() == 0) {
			totalAttributes++;
		} else {
			totalAttributes += releationship.shapesDeleted.size();
		}

		return totalAttributes;
	}

	public static double compareSides(Releationship aReleationship,
			Releationship bReleationship) {
		double strength = 0;
		// sides
		if (aReleationship.totalSidesA == aReleationship.totalSidesB) {
			if (bReleationship.totalSidesA == aReleationship.totalSidesB) {
				strength++;
			}
		} else {
			if (aReleationship.totalSidesA % aReleationship.totalSidesB == 0
					|| aReleationship.totalSidesA % aReleationship.totalSidesB == 0
					|| aReleationship.totalSidesA / aReleationship.totalSidesB == 2
					|| aReleationship.totalSidesB / aReleationship.totalSidesB == 2) {
				strength++;
			}
		}

		return strength;
	}

	public static double compareShapesDeleted(Releationship aReleationship,
			Releationship bReleationship) {
		 double strength = 0;
		// shapes deleted
		if (aReleationship.shapesDeleted.size() == bReleationship.shapesDeleted
				.size()) {
			strength++;
		}
		return strength;
	}

	public static double compareShapesAdded(Releationship aReleationship,
			Releationship bReleationship) {
		 double strength = 0;
		// shapes added
		if (aReleationship.shapesAdded.size() == bReleationship.shapesAdded
				.size()) {
			strength++;
		}
		return strength;
	}

	public static double compareDifferences(Releationship aReleationship,
			Releationship bReleationship) {
		 double strength = 0;

		if (aReleationship.differencesList.size() == bReleationship.differencesList
				.size()) {
			strength++;
		}
		
		for (String stringa : aReleationship.differencesList) {
			for (String stringb : bReleationship.differencesList) {
				if (stringa.equals(stringb)) {
					strength++;
				}
			}
		}
		return strength;
	}

	public static double compareSimilarities(Releationship aReleationship,
			Releationship bReleationship) {
		 double strength = 0;
		 
		// similarities A to B vs C to n 
		if (aReleationship.similaritiesList.size() == bReleationship.similaritiesList
				.size()) {
			strength++;
		}		
		
		String aString;
		String bString;
		for (String stringa : aReleationship.similaritiesList) {
			String[] A = stringa.split(":");
			aString = A[0] + A[1];
			for (String stringb : bReleationship.similaritiesList) {
				String[] B = stringb.split(":");
				bString = B[0] +B[1];
				if (aString.equals(bString)) {
					strength++;
				}
			}
		}
	
		return strength;
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
