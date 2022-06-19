package personas;

public enum Seniority {
	JUNIOR ("Junior"),
	SEMI_SENIOR ("Semi Senior"),
	SENIOR ("Senior");
	
	private final String seniority;
	
	private Seniority(final String seniority) {
		this.seniority = seniority;
	}
	
	public String getSeniority() {
		return seniority;
	}
}
