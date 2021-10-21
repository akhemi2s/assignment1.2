import java.util.List;

public interface ManagePersonal {

    //Salesman CRUD
    public void createSalesMan( SalesMan record );
    public SalesMan readSalesMan( int sid );
    public List<SalesMan> querySalesMan(String attribute , String key );
    public SalesMan deleteSalesMan(int sid); // new
    public SalesMan updateSalesMan(int sid, String key, String value); // new

    //PerformanceRecord CRUD
    public void createPerformanceRecord( PerformanceRecord record ); //refactored
    public List<PerformanceRecord> readPerformanceRecords( int sid ,String year ); //refactored
    public List<PerformanceRecord> deletePerformanceRecord(int id,String year); // new
    public PerformanceRecord updatePerformanceRecord(int id,String year, String key, String value); //new

}
 