package localization;

public class LocalizationException extends Exception {
	public LocalizationException(LocalizationExceptionDescription desc) {
		super(desc.toString());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
