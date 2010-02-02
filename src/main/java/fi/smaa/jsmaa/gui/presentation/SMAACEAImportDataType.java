/**
 * 
 */
package fi.smaa.jsmaa.gui.presentation;

public enum SMAACEAImportDataType {
	PATIENT_ID("Pat. ID"),
	TREATMENT_ID("Treatm. ID"),
	COST("Cost"),
	EFFICACY("Effic."),
	COST_CENSORING("Cost cens."),
	EFFICACY_CENCORING("Effic. cens.");
	
	private String title;
	
	SMAACEAImportDataType(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}
}