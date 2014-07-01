package localization;

import java.util.ArrayList;
import java.util.List;

public class TestLoc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<PointId> pointIds = new ArrayList<PointId>();
		pointIds.add(new PointId(new Point(-1,1), "0"));
		pointIds.add(new PointId(new Point(1,1), "1"));
		pointIds.add(new PointId(new Point(1,-1), "2"));
		pointIds.add(new PointId(new Point(-1,-1), "3"));		
		MultiplePointsLocalization loc = new MultiplePointsLocalization(pointIds);
		
		List<PointIdDistance> pointsIdDistance = new ArrayList<PointIdDistance>();
		pointsIdDistance.add(new PointIdDistance(Math.sqrt(2.5), "0"));
		pointsIdDistance.add(new PointIdDistance(1, "1"));
		pointsIdDistance.add(new PointIdDistance(1, "2"));
		pointsIdDistance.add(new PointIdDistance(Math.sqrt(2.5), "3"));		
		try {
			Point p = loc.getLocation(pointsIdDistance);
			System.out.println("x : " + p.x + "    y : " + p.y);
		} catch (LocalizationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
