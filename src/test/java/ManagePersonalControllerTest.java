import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ManagePersonalControllerTest {
    MongoClient mongoClient;
    MongoDatabase database;
    ManagePersonalController managePersonalController;
    MongoCollection<Document> salesmenCollection;
    MongoCollection<Document> performanceRecordsCollection;

    @BeforeEach
    void setUp() {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("test");

        database.drop();

        salesmenCollection = database.getCollection("salesmen");
        performanceRecordsCollection = database.getCollection("performanceRecords");

        managePersonalController = new ManagePersonalController(salesmenCollection, performanceRecordsCollection);
    }

    @AfterEach
    void tearDown() {
        database.drop();
    }

    @Test
    void crudSalesmanTest() {
        // test create
        Assertions.assertEquals(0, salesmenCollection.count()); // assert that salesmen collection in the test database is empty
        managePersonalController.createSalesMan(SalesMan.generateSalesMan());
        managePersonalController.createSalesMan(SalesMan.generateSalesMan());
        managePersonalController.createSalesMan(SalesMan.generateSalesMan());
        Assertions.assertEquals(3, salesmenCollection.count());

        // test read with valid ID
        SalesMan validSalesman = managePersonalController.readSalesMan(1);
        Assertions.assertNotNull(validSalesman);
        Assertions.assertEquals("default", validSalesman.getName());
        Assertions.assertEquals("default", validSalesman.getDepartment());

        // test read with non valid ID
        SalesMan notValidSalesman = managePersonalController.readSalesMan(4);
        Assertions.assertNull(notValidSalesman);

        // test read many Salesmen
        List<SalesMan> listSalesmen = managePersonalController.querySalesMan("*", "*");
        Assertions.assertEquals(3, listSalesmen.size());

        // test update salesman with valid ID
        SalesMan updatedSalesman = managePersonalController.updateSalesMan(1, "name", "defaultUpdated");
        Assertions.assertEquals("defaultUpdated", updatedSalesman.getName());

        // test update salesman with non valid ID
        SalesMan notValidUpdatedSalesman = managePersonalController.updateSalesMan(6, "name", "defaultUpdated");
        Assertions.assertNull(notValidUpdatedSalesman);

        //test Delete with valid ID
        SalesMan deleletedSalesman = managePersonalController.deleteSalesMan(1);
        Assertions.assertNotNull(deleletedSalesman);  // assert that the method deleted something and returned it back
        Assertions.assertEquals(2, salesmenCollection.count());
        Assertions.assertNull(managePersonalController.readSalesMan(1));

        //test Delete with non valid ID
        deleletedSalesman = managePersonalController.deleteSalesMan(7);
        Assertions.assertNull(deleletedSalesman); // assert that the method returned null
        Assertions.assertEquals(2, salesmenCollection.count());
    }

    @Test
    void crudPerformanceRecordTest() {
        /* test create */
        Assertions.assertEquals(0, performanceRecordsCollection.count());
        Assertions.assertEquals(0, salesmenCollection.count());

        // trying to create a PerformanceRecord for a non existent Salesman
        managePersonalController.createPerformanceRecord(PerformanceRecord.generatePerformanceRecord());
        Assertions.assertEquals(0, performanceRecordsCollection.count());

        // create Salesman with ID 1 then try again
        managePersonalController.createSalesMan(SalesMan.generateSalesMan());
        managePersonalController.createPerformanceRecord(PerformanceRecord.generatePerformanceRecord());
        Assertions.assertEquals(1, performanceRecordsCollection.count());


        /* test read */
        List<PerformanceRecord> list = managePersonalController.readPerformanceRecords(1,0);
        Assertions.assertEquals(1, list.size());

        /* test update */
        PerformanceRecord updatedPr = managePersonalController.updatePerformanceRecord(1,1, "description", "newDesc");
        Assertions.assertEquals("newDesc", updatedPr.getDescription());

        /* test delete */
        PerformanceRecord deletedPr = managePersonalController.deletePerformanceRecord(1,1);
        Assertions.assertNotNull(deletedPr);
        Assertions.assertEquals(0, performanceRecordsCollection.count());
    }
}