package handyStuff;

public class executionSum implements Comparable<executionSum>{
	public double rank;
	public Long value;
	public String apiDB;
	public executionSum(long value, String apiDB) {
		super();
		this.rank = 0;
		this.value = value;
		this.apiDB = apiDB;
	}
	@Override
	public String toString() {
		return "executionSum [rank=" + rank + ", value=" + value + ", apiDB=" + apiDB + "]";
	}
	@Override
	public int compareTo(executionSum o) {
		return this.value.compareTo(o.value);
	}
	
	
	
}
