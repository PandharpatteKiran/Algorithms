package com.project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * @author Pandharpatte Kiran CS610 PP 4006
 */
public class hits4006 {

	Scanner scan = null;
	static int iterations;
	static double initialValue;
	static String fname;
	static ArrayList<Integer> vertices = new ArrayList<Integer>();
	static Map<Integer, Integer> vertexDeg = new HashMap<Integer, Integer>();
	static Map<Integer, List<Integer>> myPointers = new HashMap<Integer, List<Integer>>();
	static Map<Integer, List<Integer>> adjList = new HashMap<Integer, List<Integer>>();
	static Map<Integer, Double> authority = new HashMap<Integer, Double>();
	static Map<Integer, Double> hub = new HashMap<Integer, Double>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = null;
		String filename = "C:/Kiran/Practice/samplegraph.txt";
		ArrayList<Integer> file = new ArrayList<Integer>();
		ArrayList<Integer> source = new ArrayList<Integer>();
		ArrayList<Integer> destination = new ArrayList<Integer>();
		SortedSet<Integer> vert = new TreeSet<Integer>();

		try {

			iterations = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
			//fname = args[2];
			//String filename = fname;
			scan = new Scanner(new FileReader(filename));
			while (scan.hasNext()) {
				int val = scan.nextInt();
				file.add(val);
			}

			// Unique vertices stored in a set
			for (int i = 2; i < file.size(); i++) {
				vert.add(file.get(i));
			}

			// since vertices are in a set - I converted it to a list
			vertices.addAll(vert);

			if (initialValue == -1) {
				initialValue = (double) 1 / vert.size();
			} else if (initialValue == -2) {
				initialValue = (double) 1 / (Math.sqrt(vert.size()));
			}

			// Source and Destination vertices
			for (int i = 2; i < file.size(); i++) {
				source.add(file.get(i));
				i++;
				destination.add(file.get(i));
			}

			// Degree of a vertex {source = degree}
			for (int i = 0; i < source.size(); i++) {
				int degree = Collections.frequency(source, source.get(i));
				vertexDeg.put(source.get(i), degree);
			}

			// Adjacency List of Given Graph
			Iterator<Integer> itr = vertices.iterator();
			while (itr.hasNext()) {
				int src = itr.next();
				ArrayList<Integer> destVert = new ArrayList<Integer>();
				for (int i = 0; i < source.size(); i++) {
					if (source.get(i).equals(src)) {
						destVert.add(destination.get(i));
					}
				}
				adjList.put(src, destVert);
			}

			// Find who's pointing to who
			for (int i = 0; i < vertices.size(); i++) {
				int a = vertices.get(i);
				ArrayList<Integer> pointers = new ArrayList<>();
				for (int j = 0; j < destination.size(); j++) {
					int b = destination.get(j);
					if (b == a) {
						if (pointers.isEmpty()) {
							pointers.add(source.get(j));
						} else if (!pointers.contains(source.get(j))) {
							pointers.add(source.get(j));
						}
					}
				}
				myPointers.put(vertices.get(i), pointers);
			}

			String pattern = "#.######";
			DecimalFormat df = new DecimalFormat(pattern);
			String formatInitHub = df.format(initialValue);
			// Initialize ranks to initialValue
			for (int k = 0; k < vertices.size(); k++) {
				hub.put(vertices.get(k), Double.parseDouble(formatInitHub));
			}
			System.out.println("Base Hub: Iter  0 " + hub);
			if (iterations == 0) {
				iterations = 1;
			}

			for (int i = 0; i < iterations; i++) {

				// Authority Calculation
				for (int j = 0; j < vertices.size(); j++) {
					double auth = 0;
					ArrayList<Integer> aList = (ArrayList<Integer>) myPointers.get(vertices.get(j));
					for (int k = 0; k < aList.size(); k++) {
						auth = auth + (hub.get(aList.get(k)));
					}
					authority.put(vertices.get(j), auth);
				}
				// Scale Authority Values
				double sqAuth = 0;
				double tempAuth = 0;
				double scaleAuth = 0;
				for (int p = 0; p < vertices.size(); p++) {
					tempAuth = authority.get(vertices.get(p));
					sqAuth = sqAuth + (tempAuth * tempAuth);
				}
				for (int q = 0; q < vertices.size(); q++) {
					scaleAuth = (double)(authority.get(vertices.get(q)) / Math.sqrt(sqAuth));
					
					//String formatAuth = (String) df.format(scaleAuth);
					authority.put(vertices.get(q), scaleAuth);
					//System.out.println(authority);									
				}

				// Hub Calculation
				for (int m = 0; m < vertices.size(); m++) {
					double hubval = 0;
					ArrayList<Integer> aList1 = (ArrayList<Integer>) adjList.get(vertices.get(m));
					for (int l = 0; l < aList1.size(); l++) {
						hubval = hubval + (double) (authority.get(aList1.get(l)));
					}
					hub.put(vertices.get(m), hubval);
					//System.out.println(hub);
				}
				// Scale Hub values
				double sqHub = 0.0;
				double tempHub = 0.0;
				double scaleHub = 0.0;				
				for (int p = 0; p < vertices.size(); p++) {
					tempHub = hub.get(vertices.get(p));
					sqHub = sqHub + (tempHub * tempHub);
				}
				for (int q = 0; q < vertices.size(); q++) {
					scaleHub = (double)(hub.get(vertices.get(q)) / Math.sqrt(sqHub));
					//String formatHub = (String) df.format(scaleHub);
					
					hub.put(vertices.get(q), scaleHub);
					//System.out.println(hub);
				}
				if (vertices.size() < 10) {
					display4006(authority, hub, i);
				} else {
					if (i == (iterations - 1)) {
						System.out.println("Iter " + (i + 1));
						for (int j = 0; j < vertices.size(); j++) {
							System.out.println("A/H : " + "[" + j + "]" + " " + authority.get(j) + "/" + hub.get(j) + " ");
							//System.out.println("A/H : " + "[" + j + "]" + " " + df.format(authority.get(j)) + "/" + df.format(hub.get(j)) + " ");
						}
						// System.out.println();
					}
					continue;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scan = null;
		}
	}

	private static void display4006(Map<Integer, Double> authority2, Map<Integer, Double> hub2, int i2) {
		// TODO Auto-generated method stub
		ArrayList<Double> a = new ArrayList<Double>();
		ArrayList<Double> b = new ArrayList<Double>();
		for (int i = 0; i < authority2.size(); i++) {
			a.add(authority2.get(i));
			b.add(hub2.get(i));
		}
		String pattern = "#.######";
		DecimalFormat df = new DecimalFormat(pattern);		
		System.out.print("Iter " + (i2 + 1) + " A/H : ");
		for (int j = 0; j < vertices.size(); j++) {
			System.out.print("[" + j + "]" + " " + a.get(j) + "/" + b.get(j) + " ");
			//System.out.print("[" + j + "]" + " " + df.format(a.get(j)) + "/" + df.format(b.get(j)) + " ");
		}
		System.out.println();
	}

}
