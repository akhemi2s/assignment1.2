import java.util.List;

public class PerformanceRecord {
    private int id;
    private int sId;
    private int year;
    private int bonus;
    private List<EvaluationRecord> evaluationRecords;

    public PerformanceRecord(int id, int sId, int year, int bonus, List<EvaluationRecord> evaluationRecords) {
        this.id = id;
        this.sId = sId;
        this.year = year;
        this.bonus = bonus;
        this.evaluationRecords = evaluationRecords;
    }

    public int getId() {
        return id;
    }

    public int getsId() {
        return sId;
    }

    public int getYear() {
        return year;
    }

    public List<EvaluationRecord> getEvaluationRecords() {
        return evaluationRecords;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEvaluationRecords(List<EvaluationRecord> evaluationRecords) {
        this.evaluationRecords = evaluationRecords;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
