import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ManagePersonalController implements ManagePersonal {

	MongoCollection<Document> salesManCollection;
	MongoCollection<Document> performanceRecordCollection;

	public ManagePersonalController(MongoCollection<Document> collection,
			MongoCollection<Document> performanceRecordCollection) {
		this.salesManCollection = collection;
		this.performanceRecordCollection = performanceRecordCollection;
	}

	@Override
    public void createSalesMan(SalesMan record) {
        MongoCursor<Document> cursor;
        int letztID = 0;
        cursor = salesManCollection.find().iterator();
        while (cursor.hasNext()) {Document cursorDoc = cursor.next();
        letztID=(int) cursorDoc.get("_id");
        }

    	Document doc = new Document("_id", letztID + 1)
                .append("name", record.getName())
                .append("department", record.getDepartment());
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
			System.out.println("SalesMan not found");
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
				SalesMan tmpS = new SalesMan(((Long) cursorDoc.get("_id")).intValue(), (String) cursorDoc.get("name"),
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

	}

	@Override
	public List<PerformanceRecord> readPerformanceRecords(int sid) {
		return null;
	}

	@Override
	public PerformanceRecord deletePerformanceRecord(int id) {
		return null;
	}

	@Override
	public PerformanceRecord updatePerformanceRecord(int id, String key, String value) {
		return null;
	}
}
