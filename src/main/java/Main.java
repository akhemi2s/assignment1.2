import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Document> salesmenCollection = database.getCollection("salesmen");
        MongoCollection<Document> performanceRecordsCollection = database.getCollection("performanceRecords");

        ManagePersonalController managePersonalController =
                new ManagePersonalController(
                        salesmenCollection,
                        performanceRecordsCollection
                );

        // salesmenCollection.deleteMany(new Document()); // delete all documents to prevent duplicate key error

        label:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose an option:\n" +
                    "[0-EXIT] [1-CREATE salesman] [2-READ SalesMan] [3-GET MANY salesmen] [4-DELETE salesman] [5-UPDATE salesman]"
            );
            String option = scanner.nextLine();

            int sid;
            boolean valid = false;

            switch (option) {
                case "0": // Exit
                    break label;

                case "1": // create salesman
                    System.out.println("Enter the salesman's Name: ");
                    String name = scanner.nextLine();
                    System.out.println("Enter the salesman's Department: ");
                    String dep = scanner.nextLine();
                    SalesMan s = new SalesMan(name, dep);
                    managePersonalController.createSalesMan(s);
                    break;

                case "2": // Read salesman
                    System.out.println("Enter the salesman's ID: ");
                    while (!valid) {
                        try {
                            sid = scanner.nextInt();
                            SalesMan result = managePersonalController.readSalesMan(sid);
                            System.out.println(result);
                            valid = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Enter a valid ID!");
                            scanner = new Scanner(System.in);
                        }
                    }
                    break;

                case "3":
                    System.out.println("Enter the criteria: Ex: name=Thomas / * to query all");
                    String[] criteria = scanner.nextLine().split("=");
                    while (!criteria[0].equals("*") && criteria.length != 2) {
                        System.out.println("Enter a valid criteria!");
                        criteria = scanner.nextLine().split("=");
                    }
                    List<SalesMan> list;
                    if (criteria[0].equals("*")) {
                        list = managePersonalController.querySalesMan("*", "*");
                    } else {
                        list = managePersonalController.querySalesMan(criteria[0], criteria[1]);
                    }

                    if(list.size() == 0) {
                        System.out.println("No Salesman was found! Please change the criteria");
                    } else {
                        System.out.println(list.size() + " found:");
                        for (SalesMan salesMan : list) {
                            System.out.println(salesMan);
                        }
                    }

                    break;

                case "4":
                    System.out.println("Enter the ID of the Salesman:");
                    while (!valid) {
                        try {
                            sid = scanner.nextInt();
                            SalesMan deleted = managePersonalController.deleteSalesMan(sid);
                            if (deleted != null)
                                System.out.println(deleted + " was successfully removed from The Database");
                            valid = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Enter a valid ID!");
                            scanner = new Scanner(System.in);
                        }
                    }
                    break;
                case "5":
                    System.out.println("Enter the ID of the Salesman:");
                    while (!valid) {
                        try {
                            sid = scanner.nextInt();
                            System.out.println("Enter the KEY of the field you want to update:");
                            scanner = new Scanner(System.in);
                            String key = scanner.nextLine();
                            System.out.println("Enter the VALUE of the field you want to update:");
                            String value = scanner.nextLine();
                            SalesMan updated = managePersonalController.updateSalesMan(sid, key, value);
                            if (updated != null)
                                System.out.println(updated + " was successfully updated");
                            valid = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Enter a valid ID!");
                            scanner = new Scanner(System.in);
                        }
                    }

                default:
                    System.out.println("invalid option!");
            }
        }
        System.out.println("Thanks for using our software :)");
    }
}
