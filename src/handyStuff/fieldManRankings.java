package handyStuff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class fieldManRankings {

	private String database;
	private String password;
	private ArrayList<executionSum> graphMongo = new ArrayList<executionSum>();
	private ArrayList<executionSum> restMongo = new ArrayList<executionSum>();
	private ArrayList<executionSum> soapMongo = new ArrayList<executionSum>();
	private ArrayList<executionSum> graphMysql = new ArrayList<executionSum>();
	private ArrayList<executionSum> restMysql = new ArrayList<executionSum>();
	private ArrayList<executionSum> soapMysql = new ArrayList<executionSum>();

	public fieldManRankings(String database, String password) {
		this.database = database;
		this.password = password;
		getData();
	}

	private void getData() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", password);

			for (int testcase = 1; testcase <= 18; testcase++) {
				for (int execution = 1; execution <= 10; execution++) {
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(
							"SELECT SUM(totalTime) FROM resultdatalog WHERE testCase = 'TestCase" + testcase
									+ "' AND executionNO = " + execution + " AND API = 'GRAPHQL' AND dbName = 'Mongo'");
					rs.next();
					graphMongo.add(new executionSum(rs.getInt(1), "GraphQL-Mongo"));

					rs = stmt.executeQuery("SELECT SUM(totalTime) FROM resultdatalog WHERE testCase = 'TestCase"
							+ testcase + "' AND executionNO = " + execution + " AND API = 'REST' AND dbName = 'Mongo'");
					rs.next();
					restMongo.add(new executionSum(rs.getInt(1), "REST-Mongo"));

					rs = stmt.executeQuery("SELECT SUM(totalTime) FROM resultdatalog WHERE testCase = 'TestCase"
							+ testcase + "' AND executionNO = " + execution + " AND API = 'SOAP' AND dbName = 'Mongo'");
					rs.next();
					soapMongo.add(new executionSum(rs.getInt(1), "SOAP-Mongo"));

					rs = stmt.executeQuery(
							"SELECT SUM(totalTime) FROM resultdatalog WHERE testCase = 'TestCase" + testcase
									+ "' AND executionNO = " + execution + " AND API = 'GRAPHQL' AND dbName = 'MySql'");
					rs.next();
					graphMysql.add(new executionSum(rs.getInt(1), "GraphQL-MySQL"));

					rs = stmt.executeQuery("SELECT SUM(totalTime) FROM resultdatalog WHERE testCase = 'TestCase"
							+ testcase + "' AND executionNO = " + execution + " AND API = 'REST' AND dbName = 'MySql'");
					rs.next();
					restMysql.add(new executionSum(rs.getInt(1), "REST-MySQL"));

					rs = stmt.executeQuery("SELECT SUM(totalTime) FROM resultdatalog WHERE testCase = 'TestCase"
							+ testcase + "' AND executionNO = " + execution + " AND API = 'SOAP' AND dbName = 'MySql'");
					rs.next();
					soapMysql.add(new executionSum(rs.getInt(1), "SOAP-MySQL"));

				}
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getRankings() {

		System.out.println(soapMysql.size());

		for (int i = 0; i < 180; i++) {
			// for (int execution = 1; execution <= 10; execution++) {
			// System.out.println("new iteration! *********************************");
			ArrayList<executionSum> tempSort = new ArrayList<executionSum>();
			// System.out.println("testcase*execution: "+testcase*execution+" and the get is
			// "+(testcase*execution-1));
			tempSort.add(graphMongo.get(i));
			tempSort.add(restMongo.get(i));
			tempSort.add(soapMongo.get(i));
			tempSort.add(graphMysql.get(i));
			tempSort.add(restMysql.get(i));
			tempSort.add(soapMysql.get(i));

			Collections.sort(tempSort);

			// rank them.
			for (int j = 0; j < 6; j++) {
				executionSum current = tempSort.get(j);

				if (current.value.equals(15806L)) {
					System.out.println("hit!");
				}

				if (j < 5) {
					// is current not the same as next?
					if (!current.value.equals(tempSort.get(j + 1).value)) {
						// System.out.println(i+" is not equals to the next one");
						current.rank = j + 1;
					} else {

						// the next is the same as current.
						int it = j + 1;
						// System.out.println("i: "+i);
						// find out how many in the list are equal.
						while (current.value.equals(tempSort.get(it).value)) {
							it++;
							// System.out.println(it);
						}
						// remove one from it, otherwise we go over and into one that is not equals.
						it--;

						// give them a rank.
						int rank = (j + 1 + it + 1) / 2;
						for (int x = i; x <= it; x++) {
							tempSort.get(x).rank = rank;
						}
						// up i to the next unranked value.
						j = it;
					}
				} else {
					// System.out.println("last one @: "+i);
					current.rank = j + 1;
				}
			}
			// printList(tempSort);
			// }
		}
		printCSV();
	}

	private void printCSV() {
		/*
		 * for (int i = 0; i < 18; i++) { for(int j = 0; j < 10; j++) {
		 * System.out.println(i+1+","+graphMongo.get((i+1*j)).value+","+graphMongo.get((
		 * i+1*j)).rank+","+
		 * restMongo.get((i+1*j)).value+","+restMongo.get((i+1*j)).rank+","+
		 * soapMongo.get((i+1*j)).value+","+soapMongo.get((i+1*j)).rank+","+
		 * graphMysql.get((i+1*j)).value+","+graphMysql.get((i+1*j)).rank+","+
		 * restMysql.get((i+1*j)).value+","+restMysql.get((i+1*j)).rank+","+
		 * soapMysql.get((i+1*j)).value+","+soapMysql.get((i+1*j)).rank); } }
		 */

		for (int i = 0; i < 180; i++) {
			System.out.println(i + "," + graphMongo.get((i)).value + "," + graphMongo.get((i)).rank + ","
					+ restMongo.get((i)).value + "," + restMongo.get((i)).rank + "," + soapMongo.get((i)).value + ","
					+ soapMongo.get((i)).rank + "," + graphMysql.get((i)).value + "," + graphMysql.get((i)).rank + ","
					+ restMysql.get((i)).value + "," + restMysql.get((i)).rank + "," + soapMysql.get((i)).value + ","
					+ soapMysql.get((i)).rank);
		}

	}

	private void printList(ArrayList<executionSum> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

}
