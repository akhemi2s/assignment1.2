public class EvaluationRecord {
    private int id;
    private String title;
    private int targetValue;
    private int actualValue;

    public EvaluationRecord(String title, int targetValue, int actualValue, int bonus) {
        this.title = title;
        this.targetValue = targetValue;
        this.actualValue = actualValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public int getActualValue() {
        return actualValue;
    }

    public void setActualValue(int actualValue) {
        this.actualValue = actualValue;
    }


    @Override
    public String toString() {
        return "EvaluationRecord{" +
                "title='" + title + '\'' +
                ", targetValue=" + targetValue +
                ", actualValue=" + actualValue +
                '}';
    }
}
