package localization;

/**
 * 
 * @author Sulzyckim
 * Odleglosc od punktu identyfikowanego identyfikatorem
 */
public class PointIdDistance implements Comparable<PointIdDistance> {
	public String ID;
	public double distance;
	
	@Override
	public int compareTo(PointIdDistance another) {
		if(distance > another.distance) return 1;
		if(distance < another.distance) return -1;
		return 0;
	}
	
	public PointIdDistance(){
	}
	
	public PointIdDistance(double distance, String ID){
		this.distance = distance;
		this.ID = ID;
	}
}
