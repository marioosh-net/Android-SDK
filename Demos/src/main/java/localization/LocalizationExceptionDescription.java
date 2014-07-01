package localization;

public enum LocalizationExceptionDescription {
	WRONG_POINTS_COUNT("Bledna ilosc punktow"),
	POINT_ID_NOT_FOUND("Brak punktu o podanym ID"),
	WRONG_SIDES_COUNT("Bledna ilosc bokow");
	
	String desc;
	
	LocalizationExceptionDescription(String desc){
		this.desc =desc;  
	}
}
