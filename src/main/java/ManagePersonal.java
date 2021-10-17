import java.util.List;

public interface ManagePersonal {
    public void createSalesMan( SalesMan record );

    public void addPerformanceReord( EvaluationRecord record , int sid );

    public SalesMan readSalesMan( int sid );

    public List<SalesMan> querySalesMan(String attribute , String key );

    public EvaluationRecord readEvaluationRecords( int sid );

    public SalesMan deleteSalesMan(int sid); // new

    public SalesMan updateSalesMan(int sid, String key, String value); // new
}
