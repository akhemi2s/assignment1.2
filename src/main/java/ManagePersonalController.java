import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ManagePersonalController implements ManagePersonal {

    MongoCollection<Document> salesManCollection;
    MongoCollection<Document> performanceRecordsCollection;
    ;

    public ManagePersonalController(MongoCollection<Document> collection,
                                    MongoCollection<Document> performanceRecordCollection) {
        this.salesManCollection = collection;
        this.performanceRecordsCollection = performanceRecordCollection;
    }

    @Override
    public void createSalesMan(SalesMan record) {
        MongoCursor<Document> cursor;
        int letztID = 0;
        cursor = salesManCollection.find().iterator();
        while (cursor.hasNext()) {
            Document cursorDoc = cursor.next();
            letztID = (int) cursorDoc.get("_id");
        }

        Document doc = new Document("_id", letztID + 1).append("name", record.getName()).append("department",
                record.getDepartment());
        salesManCollection.insertOne(doc);

        System.out.println("Salesman created");
    }

    @Override
    public SalesMan readSalesMan(int sid) {
        Document tmpDoc = salesManCollection.find(Filters.eq("_id", sid)).first();
        SalesMan s = null;
        try {
            s = new SalesMan(sid, (String) tmpDoc.get("name"), (String) tmpDoc.get("department"));

        } catch (NullPointerException e) {
            System.out.print("SalesMan not found");
        }

        return s;
    }

    @Override
    public List<SalesMan> querySalesMan(String attribute, String key) {
        List<SalesMan> results = new ArrayList<>();
        MongoCursor<Document> cursor;
        if (attribute.equals("*") && key.equals("*")) {
            cursor = salesManCollection.find().iterator();
        } else {
            cursor = salesManCollection.find(Filters.eq(attribute, key)).iterator();
        }

        try {
            while (cursor.hasNext()) {
                Document cursorDoc = cursor.next();
                SalesMan tmpS = new SalesMan((int) cursorDoc.get("_id"), (String) cursorDoc.get("name"),
                        (String) cursorDoc.get("department"));
                results.add(tmpS);
            }
        } finally {
            cursor.close();
        }

        return results;
    }

    @Override
    public SalesMan deleteSalesMan(int sid) {
        SalesMan deltedS = readSalesMan(sid);
        if (deltedS != null) {
            salesManCollection.deleteOne(Filters.eq("_id", sid));
        }
        return deltedS;
    }

    @Override
    public SalesMan updateSalesMan(int sid, String key, String value) {
        SalesMan updatedS = readSalesMan(sid);
        if (updatedS != null) {
            Document fieldToUpdate = new Document(key, value);
            salesManCollection.updateOne(new Document("_id", sid), new Document("$set", fieldToUpdate));
            updatedS = readSalesMan(sid); // query the updated item
        }
        return updatedS;
    }

    @Override
    public void createPerformanceRecord(PerformanceRecord record) {
        MongoCursor<Document> cursor;
        int letztID = 0;
        cursor = performanceRecordsCollection.find().iterator();
        while (cursor.hasNext()) {
            Document cursorDoc = cursor.next();
            letztID = (int) cursorDoc.get("_id");
        }

        Document doc = new Document("_id", letztID + 1).append("sid", record.getsId())
                .append("description", record.getDescription()).append("targetValue", record.getTargetValue())
                .append("actualValue", record.getActualValue()).append("year", record.getYear());

        performanceRecordsCollection.insertOne(doc);
        System.out.println("Performance für die ID:" + record.getsId() + " is created");
    }

    @Override
    public List<PerformanceRecord> readPerformanceRecords(int sid, int year) {
        List<PerformanceRecord> results = new ArrayList<>();
        MongoCursor<Document> cursor;

        if (year == 0) {
            cursor = performanceRecordsCollection.find(Filters.eq("sid", sid)).iterator();
        } else {
            cursor = performanceRecordsCollection
                    .find(
                            Filters.and(
                                    Filters.eq("sid", sid),
                                    Filters.eq("year", year)
                            )
                    )
                    .iterator();
        }

        while (cursor.hasNext()) {
            Document cursorDoc = cursor.next();
            PerformanceRecord tmpP = new PerformanceRecord((int) cursorDoc.get("_id"), (int) cursorDoc.get("sid"),
                    (String) cursorDoc.get("description"), (int) cursorDoc.get("targetValue"),
                    (int) cursorDoc.get("actualValue"), (int) cursorDoc.get("year"));
            results.add(tmpP);
        }

        return results;
    }

    @Override
    public PerformanceRecord deletePerformanceRecord(int sid, int year) {
        List<PerformanceRecord> performanceRecords = readPerformanceRecords(sid, year);
        PerformanceRecord deltedPer = null;
        if (performanceRecords.size() > 0) {
            deltedPer = performanceRecords.get(0);
            performanceRecordsCollection.deleteOne(Filters.eq("_id", deltedPer.getId()));
        }
        return deltedPer;
    }

    @Override
    public PerformanceRecord updatePerformanceRecord(int sid, int year, String key, String value) {
        int val;
        Document fieldToUpdate;
        List<PerformanceRecord> updatedS = readPerformanceRecords(sid, year);
        if (updatedS.size() > 0) {
            if (key.equalsIgnoreCase("description")) {
                fieldToUpdate = new Document(key, value);
            } else {
                val = Integer.parseInt(value);
                fieldToUpdate = new Document(key, val);
            }

            performanceRecordsCollection.updateOne(new Document("_id", updatedS.get(0).getId()), new Document("$set", fieldToUpdate));
            updatedS = readPerformanceRecords(sid, year);

            return updatedS.get(0);
        }
        return null;
    }
}
