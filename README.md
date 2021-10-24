# assignment1.2

Code overview:

4 Classes:

    Main Class:  the Command Line Interface (UI / CLI)
    Salesman Class: basic class for Salesman-objects (see collections below)
    PerformanceRecord Class: basic class for PerformanceRecord-objects (see collections below)
    ManagePersonalController Class: class to manage CRUD actions for salesmen and PerformanceRecords
1 Interface:

    ManagePersonal Interface: contains the methods implemented in the ManagePersonalController.
1 TestClass:

    ManagePersonalControllerTest: round-tripping tests for Salesman/PerformanceRecord CRUD

Collections:

    SalesMen
        [
            {
                _id: int, (unique)
                name: String,
                department: String
            },
            ...
        ]

    PerformanceRecords
        [
            {
                _id: int, (unique)
                sId: int,  (salesManId)
                description: String,
                targetValue: int,
                actualValue: int,
                year: int
            },
            ...
        ]
