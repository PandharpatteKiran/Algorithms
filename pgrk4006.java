package com.project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Pandharpatte Kiran CS610 PP 4006
 */
public class pgrk4006 {
	Scanner scan = null;
	static int iterations;
	static double initialValue;
	static String fname;
	static final double ERRORRATE = ((double) 0.00001);
	static ArrayList<Integer> vertices = new ArrayList<Integer>();
	static Map<Integer, Integer> vertexDeg = new HashMap<Integer, Integer>();
	static Map<Integer, List<Integer>> myPointers = new HashMap<Integer, List<Integer>>();
	static Map<Integer, Double> pRank = new HashMap<Integer, Double>();
	static Map<Integer, Double> pRank1 = new HashMap<Integer, Double>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = null;
		String filename = "C:/Kiran/Practice/graphsproblems/actresses1.txt";
		ArrayList<Integer> file = new ArrayList<Integer>();
		ArrayList<Integer> source = new ArrayList<Integer>();
		ArrayList<Integer> destination = new ArrayList<Integer>();
		SortedSet<Integer> vert = new TreeSet<Integer>();
		// Map<Integer, List<Integer>> adjList = new HashMap<Integer,
		try {

			iterations = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
			//fname = args[2];
			//String filename = fname;

			// System.out.println(filename1);

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

			/*
			 * // Adjacency List of Given Graph Iterator<Integer> itr =
			 * vertices.iterator(); while (itr.hasNext()) { int src =
			 * itr.next(); ArrayList<Integer> destVert = new
			 * ArrayList<Integer>(); for (int i = 0; i < source.size(); i++) {
			 * if (source.get(i).equals(src)) {
			 * destVert.add(destination.get(i)); } } adjList.put(src, destVert);
			 * } System.out.println("Adjacency List"); System.out.println(
			 * "Source=[Destination List] : " + adjList);
			 */

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

			double d = 0.85;
			int n = vertices.size();
			double pr;
			String pattern = "#.######";
			DecimalFormat df = new DecimalFormat(pattern);
			String formatInitial = (String) df.format(initialValue);

			// Initialize ranks to initialValue
			for (int k = 0; k < vertices.size(); k++) {
				pRank.put(vertices.get(k), Double.parseDouble(formatInitial));
			}

			System.out.println("Base : 0 " + pRank);

			// ERRORRATE section
			int success = 0;
			if (iterations == 0) {
				iterations = 1;
				for (int i = 0; i < iterations; i++) {
					for (int j = 0; j < vertices.size(); j++) {
						ArrayList<Integer> aList = (ArrayList<Integer>) myPointers.get(vertices.get(j));
						double val = 0;
						for (int k = 0; k < aList.size(); k++) {
							val = val + (pRank.get(aList.get(k)) / vertexDeg.get(aList.get(k)));
						}
						pr = ((double) (1 - d) / n) + (d * val);
						String ss = (String) df.format(pr);
						pRank1.put(vertices.get(j), Double.parseDouble(ss));

					}
					for (int k = 0; k < vertices.size(); k++) {
						if ((pRank.get(vertices.get(k)) - pRank1.get(vertices.get(k))) < ERRORRATE) {
							if (k == (vertices.size() - 1)) {
								success = 1;
								if (vertices.size() <= 10) {
									System.out.println("Iter : " + (i + 1) + " " + pRank); // final
								}
								break;
							}
							continue;

						} else {
							pRank.putAll(pRank1);
							iterations++;
							break;
						}
					}
					if (success == 1) {
						break;
					}
					if (vertices.size() <= 10) {
						System.out.println("Iter : " + (i + 1) + " " + pRank);
					}
				}
				if (vertices.size() > 10) {
					System.out.println("Iter : " + iterations);
					for (int l = 0; l < vertices.size(); l++) {
						System.out.println(vertices.get(l) + "=" + pRank.get(vertices.get(l)));
					}
				}
			} // Till Here

			if (success == 0) {
				for (int i = 0; i < iterations; i++) {
					for (int j = 0; j < vertices.size(); j++) {
						ArrayList<Integer> aList = (ArrayList<Integer>) myPointers.get(vertices.get(j));
						double val = 0;
						for (int k = 0; k < aList.size(); k++) {
							val = val + ( pRank.get(aList.get(k)) / vertexDeg.get(aList.get(k)));
						}
						pr = ((double) (1 - d) / n) + (d * val);
						String ss = (String) df.format(pr);
						pRank.put(vertices.get(j), Double.parseDouble(ss));
				
					}
					if (vertices.size() <= 10) {
						System.out.println("Iter : " + (i + 1) + " " + pRank);
					} else {
						continue;
					}
				}
			}
			if (vertices.size() > 10 && success == 0) {
				System.out.println("Iter : " + iterations);
				for (int l = 0; l < vertices.size(); l++) {					
					System.out.println(vertices.get(l) + "=" + pRank.get(vertices.get(l)));
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scan = null;
		}
	}

}
