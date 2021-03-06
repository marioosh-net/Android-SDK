package localization;

import java.util.List;

/**
 * 
 * @author Sulzyckim
 * Opis funkcjonalnosci obliczeń dotyczących lokalizacji  
 */

public interface MultiplePointsLocalizationInterface {
	/**
	 * 
	 * @param pointsIdDistance - Lista odleglosci od punktow stalych identyfikowana przez identyfikatory punktow stalych 
	 * @return Wspolrzedne punktu
	 */
	public Point getLocation(List<PointIdDistance> pointsIdDistance) throws LocalizationException;
}
