package handyStuff;

public class main {

	static  String database = "logdb";
	static String password = "";
	public static void main(String[] args) {
		
		fieldManRankings ranker = new fieldManRankings(database, password);
		ranker.getRankings();

	}

}
