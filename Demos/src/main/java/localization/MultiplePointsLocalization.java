package localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class MultiplePointsLocalization implements MultiplePointsLocalizationInterface {

	private List<PointId> pointIds;
	
	public MultiplePointsLocalization(List<PointId> pointIds) {
		this.pointIds = pointIds;
	}



	
	
	public List<PointId> sortByDistance(List<PointIdDistance> pointsIdDistance) throws LocalizationException{
		List<PointIdDistance> pointsIdDistanceSorted = new ArrayList<PointIdDistance>(); 
		Collections.copy(pointsIdDistanceSorted, pointsIdDistance);
		Collections.sort(pointsIdDistanceSorted);
		List<PointId> pointIds = new ArrayList<PointId>();
		for(int i = 0; i < 3; i++){
			pointIds.add(findPointById(pointsIdDistanceSorted.get(i).ID));
		}
		return pointIds;
	}

	
//	metoda Linear Least Squares - bierze pod uwagê wszystkie punkty bazowe
//  http://inside.mines.edu/~whereman/papers/Murphy-Hereman-Trilateration-MCS-07-1995.pdf
//  http://commons.apache.org/proper/commons-math/userguide/linear.html#a3.4_Solving_linear_systems
	
	public Point getLocation(List<PointIdDistance> pointsIdDistance) throws LocalizationException {
		
		Point ref = findPointById(pointsIdDistance.get(0).ID).point;
		double rRef = pointsIdDistance.get(0).distance;
		int rowCount = pointsIdDistance.size() - 1;
		double[][] A = new double[rowCount][2];
		double[] b = new double[rowCount];
		for(int i = 0; i < rowCount; i++){
			Point p = findPointById(pointsIdDistance.get(i + 1).ID).point;
			A[i][0] = p.x - ref.x;
			A[i][1] = p.y - ref.y;
			double d = (ref.x - p.x) * (ref.x - p.x) + (ref.y - p.y) * (ref.y - p.y);
			double r = pointsIdDistance.get(i + 1).distance;
			b[i] = 0.5 * (rRef * rRef - r * r + d);
		}
		
		
		 
		RealMatrix coefficients =  new Array2DRowRealMatrix(A, false);
		DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
		RealVector constants = new ArrayRealVector(b, false);
		RealVector solution = solver.solve(constants);
		Point ret = new Point();
		ret.x = solution.getEntry(0) + ref.x;
		ret.y = solution.getEntry(1) + ref.y;
		return ret;
	}
	
//	
//	/**
//	 * Wyznaczenie polozenia punktu metoda pol trojkatow
//	 */
//		@Override
//	public Point getLocation(List<PointIdDistance> pointsIdDistance) throws LocalizationException {
//			if(pointsIdDistance.size() < 3) throw new LocalizationException(LocalizationExceptionDescription.WRONG_POINTS_COUNT);
//			List<PointIdDistance> pointsIdDistanceSorted = new ArrayList<PointIdDistance>(); 
//			Collections.copy(pointsIdDistanceSorted, pointsIdDistance);
//			Collections.sort(pointsIdDistanceSorted);
//			List<PointId> pointIds = new ArrayList<PointId>();
//			for(int i = 0; i < 3; i++){
//				pointIds.add(findPointById(pointsIdDistanceSorted.get(i).ID));
//			}
//			double s = calculateTriangleAreaUsingPoints(pointIds);
//			double s01 = calculateSideTriangleArea(pointsIdDistanceSorted, 0 , 1);
//			double s12 = calculateSideTriangleArea(pointsIdDistanceSorted, 1 , 2);
//			double s20 = calculateSideTriangleArea(pointsIdDistanceSorted, 2 , 0);
//			
//			return null;
//		}	
	
//	private double calculateSideTriangleArea(List<PointIdDistance> pointsIdDistanceSorted, int a, int b) throws LocalizationException{
//		List<Double> sides = new ArrayList<Double>();
//		List<PointId> sidesPoints = new ArrayList<PointId>();		
//		sides.add(pointsIdDistanceSorted.get(a).distance);
//		sides.add(pointsIdDistanceSorted.get(b).distance);
//		sidesPoints.add(findPointById(pointsIdDistanceSorted.get(a).ID));
//		sidesPoints.add(findPointById(pointsIdDistanceSorted.get(b).ID));
//		sides.add(calculateDistance(sidesPoints));
//		return calculateTriangleAreaUsingSides(sides);		
//	}
//	
//	private double calculateTriangleAreaUsingPoints(List<PointId> p) throws LocalizationException{
//		if(p.size() != 3) throw new LocalizationException(LocalizationExceptionDescription.WRONG_POINTS_COUNT);
//		double xa = p.get(0).point.x;
//		double ya = p.get(0).point.y;
//		double xb = p.get(1).point.x;
//		double yb = p.get(1).point.y;
//		double xc = p.get(2).point.x;
//		double yc = p.get(2).point.y;
//		return (double)0.5 * Math.abs((xb - xa) * (yc - ya) - (yb - ya) * (xc - xa));
//	}
//
//	
//	private double calculateTriangleAreaUsingSides(List<Double> sides) throws LocalizationException{
//		if(sides.size() != 3) throw new LocalizationException(LocalizationExceptionDescription.WRONG_SIDES_COUNT);
//		double a = sides.get(0);
//		double b = sides.get(1);
//		double c = sides.get(2);
//		double s = (a + b + c) / (double)2.0;
//		return (double)0.25 * Math.sqrt(s * (s - a) * (s - b) * (s - c));
//	}
//	
//	
	private PointId findPointById(String ID) throws LocalizationException{
		for(PointId pointId : pointIds){
			if(pointId.ID == ID) return pointId;
		}
		throw new LocalizationException(LocalizationExceptionDescription.POINT_ID_NOT_FOUND);
	}
//
//	private double calculateDistance(List<PointId> p) throws LocalizationException{
//		if(p.size() != 2) throw new LocalizationException(LocalizationExceptionDescription.WRONG_POINTS_COUNT);
//		double xa = p.get(0).point.x;
//		double ya = p.get(0).point.y;
//		double xb = p.get(1).point.x;
//		double yb = p.get(1).point.y;
//		return Math.sqrt((xb - xa) * (xb - xa) + (yb - ya) * (yb - ya));
//	}
	
	
}
