import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();

        MongoDatabase database = mongoClient.getDatabase("mongoDB");

        MongoCollection<Document> salesmenCollection = database.getCollection("salesmen");
        MongoCollection<Document> performanceRecordsCollection = database.getCollection("performanceRecords");

        ManagePersonalController managePersonalController = new ManagePersonalController(salesmenCollection,
                performanceRecordsCollection);

        label:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("""

                    Choose an option:
                    [0-EXIT]
                    [1-CREATE salesman] [2-READ SalesMan] [3-GET MANY salesmen] [4-DELETE salesman] [5-UPDATE salesman]\s
                    [6-CREATE Performance record for a salesman] [7-READ Performance record for a salesman]
                    [8-DELETE Performance record for a salesman] [9-Update Performance record for a salesman]""");
            String option = scanner.nextLine();
            int sid = 0;
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
                            if(result != null) {
                                System.out.println(result);
                            }
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

                    if (list.size() == 0) {
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
                    break;

                case "6": // create Performance record
                    String desc;
                    int year;

                    System.out.println("Enter the salesman's ID: ");
                    try {
                        sid = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("ID is not valid");
                        break;
                    }

                    SalesMan s1 = managePersonalController.readSalesMan(sid);
                    if (s1 == null) {
                        break;
                    }
                    System.out.println("Enter the Year of the Performance: ");
                    year = scanner.nextInt();
                    Document p = performanceRecordsCollection
                            .find(
                                    Filters.and(
                                            Filters.eq("sid", sid),
                                            Filters.eq("year", year)
                                    )
                            )
                            .first();
                    if (p != null) {
                        System.out.println("A record exists already for this year and ID ");
                        break;
                    }
                    scanner = new Scanner(System.in);
                    System.out.println("Enter the description:");
                    desc = scanner.nextLine();

                    try {
                        System.out.println("Enter the targetValue: ");
                        int targetValue = scanner.nextInt();

                        System.out.println("Enter the actualValue: ");
                        int actualValue = scanner.nextInt();

                        PerformanceRecord per = new PerformanceRecord(sid, desc, targetValue, actualValue, year);
                        managePersonalController.createPerformanceRecord(per);

                    } catch (InputMismatchException e) {
                        System.out.println("Enter a valid Values");
                        break;
                    }

                    break;

                case "7": // Read PerformanceRecords

                    try {
                        System.out.println("Enter the ID Of the Salesman ID");
                        sid = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Enter a valid ID");
                        break;
                    }
                    s = managePersonalController.readSalesMan(sid); //Salesman
                    if (s == null) {
                        break;
                    }

                    System.out.println("Enter the Year of the PerformanceRecord or Enter 0 to query all for this ID");

                    try {
                        year = scanner.nextInt();
                    } catch (NumberFormatException n) {
                        System.out.println("Enter a valid number");
                        break;
                    }


                    List<PerformanceRecord> listPer = managePersonalController.readPerformanceRecords(sid, year);

                    if (listPer.size() == 0) {
                        System.out.println("No PerformanceRecords were found! Please change the criteria");
                    } else {
                        System.out.println(listPer.size() + " found:");
                        for (PerformanceRecord per : listPer) {
                            System.out.println(per);
                        }
                    }
                    break;

                case "8":// delete Performance
                    System.out.println("Enter the ID of the Salesman:");

                    try {
                        sid = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Enter a valid ID Of the Salesman!");
                        break;
                    }

                    s1 = managePersonalController.readSalesMan(sid);
                    if (s1 == null) {
                        break;
                    }


                    System.out.println("Enter the year of the Performance");

                    try {
                        year = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Enter a valid year");
                        break;
                    }

                    PerformanceRecord deleted = managePersonalController.deletePerformanceRecord(sid, year);
                    if (deleted != null) {
                        System.out.println(deleted + " was successfully deleted");
                    } else {
                        System.out.println("There is no performance record for the given criteria");
                    }

                    break;
                case "9": // UPDATE Performance
                    int yearUpdate;
                    System.out.println("Enter the ID of the Salesmen:");
                    try {
                        sid = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Enter a valid ID!");
                        scanner = new Scanner(System.in);
                    }

                    try {
                        System.out.println("Enter the Year of the Performance:");
                        yearUpdate = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Enter a valid Year!");
                        break;
                    }

                    if (managePersonalController.readPerformanceRecords(sid, yearUpdate).size() == 0) {
                        System.out.println("This ID:" + sid + " has No Performance Record in this year " + yearUpdate);
                        break;
                    }


                    System.out.println("Enter the KEY of the field you want to update:");
                    scanner = new Scanner(System.in);
                    String key = scanner.nextLine();

                    System.out.println("Enter the VALUE of the field you want to update:");
                    String value = scanner.nextLine();

                    PerformanceRecord updated = managePersonalController.updatePerformanceRecord(sid, yearUpdate, key, value);
                    if (updated != null)
                        System.out.println(updated + " was successfully updated");

                    break;

                default:
                    System.out.println("invalid option!");
            }
        }
        System.out.println("Thanks for using our software :)");
    }
}
