import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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

		ManagePersonalController managePersonalController = new ManagePersonalController(salesmenCollection,
				performanceRecordsCollection);

		// salesmenCollection.deleteMany(new Document()); // delete all documents to
		// prevent duplicate key error

		label: while (true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("\nChoose an option:\n"
					+ "[0-EXIT]\n [1-CREATE salesman] [2-READ SalesMan] [3-GET MANY salesmen] [4-DELETE salesman] [5-UPDATE salesman] \n"
					+ "[6-CREATE Performance record for a salesman] [7-READ Performance record for a salesman]\n[8-DELETE Performance record for a salesman] [9-Update Performance record for a salesman]");
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
						if (!key.toUpperCase().equals("name") && !!key.toUpperCase().equals("department")) {
							throw new InputMismatchException();
						}
						System.out.println("Enter the VALUE of the field you want to update:");
						String value = scanner.nextLine();
						SalesMan updated = managePersonalController.updateSalesMan(sid, key, value);
						if (updated != null)
							System.out.println(updated + " was successfully updated");
						valid = true;
					} catch (InputMismatchException e) {
						System.out.println("choose a valid Key Name Or department!");
						scanner = new Scanner(System.in);
						break;
					}
				}
				break;

			case "6": // create Performance record
				String desc;
				int year = 0;

				MongoCursor<Document> cursorSalesman;
				System.out.println("Enter the salesman's ID: ");
				try {
					sid = scanner.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid ID");
					scanner = new Scanner(System.in);
					break;
				}
				try {
					SalesMan s1 = managePersonalController.readSalesMan(sid);
					if (s1 == null) {
						throw new NullPointerException();
					}
				} catch (NullPointerException e) {
					scanner = new Scanner(System.in);
					break;
				}
				try {
					System.out.println("Enter the Year of the Performance: ");
					year = scanner.nextInt();
					cursorSalesman = performanceRecordsCollection.find(Filters.eq("sid", sid)).iterator();
					while (cursorSalesman.hasNext()) {
						Document cursorDoc = cursorSalesman.next();
						if ((int) cursorDoc.get("year") == year) {
							throw new InputMismatchException("exist already a perfor of this ID in the same year");

						}
					}

				} catch (InputMismatchException e) {
					System.out.println("this ID have already a Performance record for this Year");
					scanner = new Scanner(System.in);
					break;
				}
				try {
					System.out.println("Enter the description: ");
					desc = scanner.next();
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid description");
					scanner = new Scanner(System.in);
					break;
				}
				try {
					System.out.println("Enter the targetValue: ");
					int target = scanner.nextInt();
					System.out.println("Enter the actualValue: ");
					int actualValue = scanner.nextInt();
					PerformanceRecord per = new PerformanceRecord(sid, desc, target, actualValue, year);
					managePersonalController.createPerformanceRecord(per);
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid Values");
					scanner = new Scanner(System.in);
					break;

				}

				break;
			case "7": // Read PerformanceRecords
				String yearInString;
				try {
					System.out.println("Enter the ID Of the Salesman ID");
					sid = scanner.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid ID");
					scanner = new Scanner(System.in);
					break;
				}
				try {
					SalesMan s1 = managePersonalController.readSalesMan(sid);
					if (s1 == null) {
						throw new NullPointerException();
					}
				} catch (NullPointerException e) {
					System.out.print(" in the Performance List");
					scanner = new Scanner(System.in);
					break;
				}
				System.out.println("Enter the Year of the PerformanceRecord or Enter * to query all for this ID");

				yearInString = scanner.next();
				if (!yearInString.equals("*")) {
					try {
						int istEinZahl = Integer.parseInt(yearInString);
					} catch (NumberFormatException n) {
						System.out.println("Please Enter the Year or choose * for All ");
						scanner = new Scanner(System.in);
						break;
					}
				}

				List<PerformanceRecord> listPer = managePersonalController.readPerformanceRecords(sid, yearInString);

				if (listPer.size() == 0) {
					System.out.println("No PerformanceRecords was found! Please change the criteria");
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
					scanner = new Scanner(System.in);
					break;
				}
				try {
					SalesMan s1 = managePersonalController.readSalesMan(sid);
					if (s1 == null) {
						throw new NullPointerException();
					}
				} catch (NullPointerException e) {
					System.out.print(" in the Performance List");
					scanner = new Scanner(System.in);
					break;
				}

				System.out.println("Enter the year of the Performance or write * to delete alle Per for this ID");
				String yearPer = scanner.next();
				if (!yearPer.equals("*")) {
					try {
						int istEinZahl = Integer.parseInt(yearPer);
					} catch (NumberFormatException n) {
						System.out.println("Please Enter the Year or Write * for ALL ");
						scanner = new Scanner(System.in);
						break;
					}
				}
				List<PerformanceRecord> deletedListe = managePersonalController.deletePerformanceRecord(sid, yearPer);
				try {
					PerformanceRecord test = deletedListe.get(0);
				} catch (IndexOutOfBoundsException f) {
					System.out.println("This ID:" + sid + " has NO Performance in this Year: " + yearPer);
					scanner = new Scanner(System.in);
					break;
				}
				for (PerformanceRecord per : deletedListe) {
					System.out.println(per);
					System.out.println(" was successfully removed from The Database");
				}

				break;
			case "9": // UPDATE Performance
				int yearUpdate = 0;
				try {
					System.out.println("Enter the ID of the Salesmen:");
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
					scanner = new Scanner(System.in);
				}

				try { // test ob die Performance bereitgestellt
					PerformanceRecord test = managePersonalController
							.readPerformanceRecords(sid, String.valueOf(yearUpdate)).get(0);
				}

				catch (IndexOutOfBoundsException e) {
					System.out.println("This ID:" + sid + " has No Performance Record in this year " + yearUpdate);
					scanner = new Scanner(System.in);
					break;
				}
				try {
					System.out.println("Enter the KEY of the field you want to update:");
					scanner = new Scanner(System.in);
					String key = scanner.nextLine();
					if (!key.equals("sId") && !key.equals("description") && !key.equals("targetValue")
							&& !key.equals("actualValue") && !key.equals("year")) {
						throw new InputMismatchException();
					}

					System.out.println("Enter the VALUE of the field you want to update:");
					String value = scanner.nextLine();
					PerformanceRecord updated = managePersonalController.updatePerformanceRecord(sid,String.valueOf(yearUpdate), key, value);
					if (updated != null)
						System.out.println(updated + " was successfully updated");
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid Key or Value!");
					scanner = new Scanner(System.in);
					break;

				}
				break;

			default:
				System.out.println("invalid option!");
			}
		}
		System.out.println("Thanks for using our software :)");
	}
}
