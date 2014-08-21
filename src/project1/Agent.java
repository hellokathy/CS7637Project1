package project1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures: public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
	/**
	 * The default constructor for your Agent. Make sure to execute any
	 * processing necessary before your Agent starts solving problems here.
	 * 
	 * Do not add any variables to this signature; they will not be used by
	 * main().
	 * 
	 */
	//used to test an assumptions strength 
	//i.e. if a transformation has a strong similarity weight than the
	//AI can make tolerance levels to arrive at the best answer
	public HashMap<String, Integer> Weights;
	
	public Agent() {
		Weights = new HashMap<String, Integer>();
		Weights.put("Unchanged", 5);
		Weights.put("Reflected", 4);
		Weights.put("Scaled", 2);
		Weights.put("Deleted", 1);
		Weights.put("ShapeChanged", 0);
	}

	/**
	 * The primary method for solving incoming Raven's Progressive Matrices. For
	 * each problem, your Agent's Solve() method will be called. At the
	 * conclusion of Solve(), your Agent should return a String representing its
	 * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
	 * are also the Names of the individual RavensFigures, obtained through
	 * RavensFigure.getName().
	 * 
	 * In addition to returning your answer at the end of the method, your Agent
	 * may also call problem.checkAnswer(String givenAnswer). The parameter
	 * passed to checkAnswer should be your Agent's current guess for the
	 * problem; checkAnswer will return the correct answer to the problem. This
	 * allows your Agent to check its answer. Note, however, that after your
	 * agent has called checkAnswer, it will *not* be able to change its answer.
	 * checkAnswer is used to allow your Agent to learn from its incorrect
	 * answers; however, your Agent cannot change the answer to a question it
	 * has already answered.
	 * 
	 * If your Agent calls checkAnswer during execution of Solve, the answer it
	 * returns will be ignored; otherwise, the answer returned at the end of
	 * Solve will be taken as your Agent's answer to this problem.
	 * 
	 * @param problem
	 *            the RavensProblem your agent should solve
	 * @return your Agent's answer to this problem
	 */
	public String Solve(RavensProblem problem) {
		
		//seperate problems from possible choices
		HashMap<String, RavensFigure> figure = problem.getFigures();
		HashMap<String, RavensFigure> choices = getChoices(problem.getFigures());
		HashMap<String, RavensFigure> problems = getProblems(problem.getFigures());
		RavensFigure three = figure.get("3");
		ArrayList<RavensObject> objects = three.getObjects();
		for (RavensObject ravensObject : three.getObjects()) {
			ArrayList<RavensAttribute> atrbArrayList = ravensObject
					.getAttributes();
			RavensAttribute attribute = atrbArrayList.get(0);

		}
		return "1";
	}

	public HashMap<String, RavensFigure> getChoices(
			HashMap<String, RavensFigure> figures) {
		
		HashMap<String, RavensFigure> list = new HashMap<String, RavensFigure>();
		Iterator<Entry<String, RavensFigure>> it = figures.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			if (pairs.getKey().toString().matches("-?\\d+(\\.\\d+)?")) {
				list.put(pairs.getKey().toString(), (RavensFigure) pairs.getValue());
			}
			//it.remove(); // avoids a ConcurrentModificationException
		}
		return list;
	}
	
	
	public HashMap<String, RavensFigure> getProblems(
			HashMap<String, RavensFigure> figures) {
		
		HashMap<String, RavensFigure> list = new HashMap<String, RavensFigure>();
		Iterator<Entry<String, RavensFigure>> it = figures.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, RavensFigure> pairs = (Map.Entry<String, RavensFigure>) it
					.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			if (!pairs.getKey().toString().matches("-?\\d+(\\.\\d+)?")) {
				list.put(pairs.getKey().toString(), (RavensFigure) pairs.getValue());
			}
			//it.remove(); // avoids a ConcurrentModificationException
		}
		return list;
	}
	public int getTranformStrength(RavensObject a, RavensObject b) {
		for (RavensAttribute attribute : a.getAttributes()) {
			
		}
		int strength = 0;
		return strength;
		
	}
	public boolean isRotation() {
		return false;
	}
	public boolean isDeletion() {
		return false;
	}
	public boolean isReflection() {
		return false;
	}
	public boolean isUnchanged() {
		return false;
	}
	public boolean isScaled() {
		return false;	
	}
	public boolean isShapeChanged() {
		return false;
	}

//	public int getOrderByStrength(String studentName) {
//		List<String> studentsList = new ArrayList<String>();
//		List<Entry<Student, Double>> list = new ArrayList<Entry<Student, Double>>(
//				getAllGrades().entrySet());
//		Collections.sort(list, new Comparator<Map.Entry<Student, Double>>() {
//			public int compare(Map.Entry<Student, Double> o1,
//					Map.Entry<Student, Double> o2) {
//				return (o2.getValue()).compareTo(o1.getValue());
//			}
//		});
//		for (Map.Entry<Student, Double> entry : list) {
//			studentsList.add(entry.getKey().getName());
//		}
//		return studentsList.indexOf(studentName) + 1;
//	}
	
	public int getSides(String shape) {
		switch (shape) {
		case "triangle":
			return 3;
		case "square":
			return 4;
		case "pentagon":
			return 5;
		case "hexagon":
			return 6;
		case "heptagon":
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
