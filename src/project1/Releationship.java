package project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Releationship {
	// its all about the meta.........
	public ArrayList<String> similaritiesList;
	public ArrayList<String> differencesList;
	public ArrayList<RavensObject> shapesDeleted;
	public ArrayList<RavensObject> shapesAdded;
	public RavensFigure objA;
	public RavensFigure objB;
	public int totalSidesA;
	public int totalSidesB;

	public Releationship(RavensFigure objA, RavensFigure objB) {

		this.similaritiesList = new ArrayList<String>();
		this.differencesList = new ArrayList<String>();
		this.objA = objA;
		this.objB = objB;

		buildReleationship();
		logRelationship();

	}

	public Releationship buildReleationship() {

		buildSimAndDiffLists();

		//removes differences that are in the similarities list
		ArrayList<String> differences = new ArrayList<String>();
		differences.addAll(differencesList);
		for (String A : differences) {
			if (similaritiesList.contains(A)) {
				differencesList.remove(A);
			}
		}

		differencesList = removeDupes(differencesList);
		similaritiesList = removeDupes(similaritiesList);

		this.totalSidesA = getSides(objA);
		this.totalSidesB = getSides(objB);
		this.shapesDeleted = getShapesDeleted(objA, objB);
		this.shapesAdded = getShapesAdded(objA, objB);

		return this;
	}

	public void buildSimAndDiffLists() {
		
		for (RavensObject A : objA.getObjects()) {
			for (RavensObject B : objB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					for (RavensAttribute aAttribute : A.getAttributes()) {
						for (RavensAttribute bAttribute : B.getAttributes()) {
							if (aAttribute.getName().equals(
									bAttribute.getName())
									&& aAttribute.getValue().equals(
											bAttribute.getValue())) {
								similaritiesList.add(String.format("%s:%s:%s",
										A.getName(), aAttribute.getName(),
										aAttribute.getValue()));
							} else {
								String diff = String.format("%s:%s:%s",
										A.getName(), bAttribute.getName(),
										bAttribute.getValue());
								if (aAttribute.getName().equals(bAttribute.getName())) {
									differencesList.add(diff);
								}
							}
						}
					}
				}
			}
		}
	}

	public ArrayList<String> removeDupes(ArrayList<String> list) {

		// add elements to al, including duplicates
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);
		return list;

	}

	public ArrayList<RavensObject> getShapesDeleted(RavensFigure figA, RavensFigure figB) {

		// gets shapes deleted between x and y
		ArrayList<RavensObject> list = new ArrayList<RavensObject>();
		for (RavensObject A : figA.getObjects()) {
			list.add(A);
			for (RavensObject B : figB.getObjects()) {
				if (A.getName().equals(B.getName())) {
					list.remove(A);
					break;
				}
			}
		}
		return list;
	}

	public ArrayList<RavensObject> getShapesAdded(RavensFigure figA, RavensFigure figB) {

		// get shapes added between x and y
		ArrayList<RavensObject> list = new ArrayList<RavensObject>();
		for (RavensObject A : figB.getObjects()) {
			list.add(A);
			for (RavensObject B : figA.getObjects()) {
				if (A.getName().equals(B.getName())) {
					list.remove(A);
					break;
				}
			}
		}
		return list;
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

		System.out.println("Total Objs fig." + objA.getName() + " "
				+ objA.getObjects().size());
		System.out.println("Total Objs fig." + objB.getName() + " "
				+ objB.getObjects().size());
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

		double matchStrength = 0;
		matchStrength += compareSides(aReleationship, bReleationship);
		matchStrength += compareSimilarities(aReleationship, bReleationship);
		matchStrength += compareDifferences(aReleationship, bReleationship);
		matchStrength += compareShapesAdded(aReleationship, bReleationship);
		matchStrength += compareShapesDeleted(aReleationship, bReleationship);
		matchStrength += compareObjects(aReleationship, bReleationship);

		// return strength of comparison
		return matchStrength;

	}

	public static double compareSides(Releationship aReleationship,
			Releationship bReleationship) {
		double strength = 0;

		// If shapes combined
		if (aReleationship.shapesDeleted.size() == 1
				&& aReleationship.totalSidesB == aReleationship.totalSidesA) {
			if (bReleationship.shapesDeleted.size() == 1
					&& bReleationship.totalSidesB == bReleationship.totalSidesA) {
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

	public static double compareObjects(Releationship aReleationship,
			Releationship bReleationship) {
		double strength = 0;
		// if obj counts are the same in AB then we expect same in Cn
		if (aReleationship.objA.getObjects().size() == aReleationship.objB
				.getObjects().size()) {
			;
			if (bReleationship.objA.getObjects().size() == bReleationship.objB
					.getObjects().size()) {
				strength++;
			}
		}
		return strength;
	}

	public static double compareDifferences(Releationship aReleationship,
			Releationship bReleationship) {

		double strength = 0;

		for (String A : aReleationship.differencesList) {
			for (String B : bReleationship.differencesList) {
				if (A.equals(B)) {
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
		if (aReleationship.similaritiesList.size() == bReleationship.similaritiesList.size()) {
			strength++;
		}

		for (String stringa : aReleationship.similaritiesList) {
			String[] A = stringa.split(":");
			for (String stringb : bReleationship.similaritiesList) {
				String[] B = stringb.split(":");
				if ((A[0] + A[1]).equals(B[0] + B[1])) {
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
