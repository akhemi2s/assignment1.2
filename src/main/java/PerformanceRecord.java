public class PerformanceRecord {
	private int id;
	private int sId;
	private String description;
	private int targetValue;
	private int actualValue;
	private int year;

	public PerformanceRecord(int id, int sId, String description, int targetValue, int actualValue, int year) {
		this.id = id;
		this.sId = sId;
		this.description = description;
		this.targetValue = targetValue;
		this.actualValue = actualValue;
		this.year = year;
	}

	public PerformanceRecord(int sId, String description, int targetValue, int actualValue, int year) {
		this.sId = sId;
		this.description = description;
		this.targetValue = targetValue;
		this.actualValue = actualValue;
		this.year = year;
	}

	public int getId() {
		return id;
	}

	public int getsId() {
		return sId;
	}

	public String getDescription() {
		return description;
	}

	public int getTargetValue() {
		return targetValue;
	}

	public int getActualValue() {
		return actualValue;
	}

	public int getYear() {
		return year;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setsId(int sId) {
		this.sId = sId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	public void setActualValue(int actualValue) {
		this.actualValue = actualValue;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "PerformanceRecord [id=" + id + ", sId=" + sId + ", description=" + description + ", targetValue="
				+ targetValue + ", actualValue=" + actualValue + ", year=" + year + "]";
	}
}
