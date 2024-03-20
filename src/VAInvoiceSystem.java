import java.sql.*;
import java.util.*;
import java.sql.Date;

class Client {
    private int id;
    private String name;

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class ClientDAO {
    private Connection connection;

    public ClientDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/VAInvoSys", "root", "Gaston7214mySQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String name) {
        try {
            String query = "INSERT INTO clients (name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            System.out.println("Client added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listClients() {
        try {
            String query = "SELECT * FROM clients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getTotalBilledAmount(int clientId) {
        double totalAmount = 0.0;
        try {
            String query = "SELECT SUM(amount) AS totalAmount FROM invoices WHERE client_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalAmount = resultSet.getDouble("totalAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
}

class Service {
    private int id;
    private String name;
    private double ratePerHour;

    public Service(int id, String name, double ratePerHour) {
        this.id = id;
        this.name = name;
        this.ratePerHour = ratePerHour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }
}

class ServiceDAO {
    private Connection connection;

    public ServiceDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/VAInvoSys", "root", "Gaston7214mySQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addService(String name, double ratePerHour) {
        try {
            String query = "INSERT INTO services (name, rate_per_hour) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, ratePerHour);
            preparedStatement.executeUpdate();
            System.out.println("Service added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listServices() {
        try {
            String query = "SELECT * FROM services";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double ratePerHour = resultSet.getDouble("rate_per_hour");
                System.out.println("ID: " + id + ", Name: " + name + ", Rate per Hour: " + ratePerHour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getTotalBilledHours(int serviceId) {
        double totalHours = 0.0;
        try {
            String query = "SELECT SUM(hours) AS totalHours FROM invoice_services WHERE service_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, serviceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalHours = resultSet.getDouble("totalHours");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalHours;
    }
}

class Invoice {
    private int id;
    private int clientId;
    private Date invoiceDate;

    public Invoice(int id, int clientId, Date invoiceDate) {
        this.id = id;
        this.clientId = clientId;
        this.invoiceDate = invoiceDate;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }
}

class InvoiceDAO {
    private Connection connection;

    public InvoiceDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/VAInvoSys", "root", "Gaston7214mySQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createInvoice(int clientId, Date invoiceDate) {
        try {
            String query = "INSERT INTO invoices (client_id, invoice_date) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setDate(2, invoiceDate);
            preparedStatement.executeUpdate();
            System.out.println("Invoice created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addServiceToInvoice(int invoiceId, int serviceId, double hours) {
        try {
            String query = "INSERT INTO invoice_services (invoice_id, service_id, hours) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, invoiceId);
            preparedStatement.setInt(2, serviceId);
            preparedStatement.setDouble(3, hours);
            preparedStatement.executeUpdate();
            System.out.println("Service added to invoice successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInvoice(int invoiceId) {
        try {
            String query = "DELETE FROM invoices WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, invoiceId);
            preparedStatement.executeUpdate();
            System.out.println("Invoice deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listInvoicesForClient(int clientId) {
        try {
            String query = "SELECT * FROM invoices WHERE client_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date invoiceDate = resultSet.getDate("invoice_date");
                System.out.println("ID: " + id + ", Invoice Date: " + invoiceDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getTotalAmountForInvoice(int invoiceId) {
        double totalAmount = 0.0;
        try {
            String query = "SELECT SUM(s.rate_per_hour * i.hours) AS totalAmount " +
                    "FROM invoice_services i " +
                    "INNER JOIN services s ON i.service_id = s.id " +
                    "WHERE i.invoice_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, invoiceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalAmount = resultSet.getDouble("totalAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
}

class AnalyticsService {
    private Connection connection;

    public AnalyticsService() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/VAInvoSys", "root", "Gaston7214mySQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getTotalIncomeForPeriod(Date startDate, Date endDate) {
        double totalIncome = 0.0;
        try {
            String query = "SELECT SUM(s.rate_per_hour * i.hours) AS totalIncome " +
                    "FROM invoice_services i " +
                    "INNER JOIN services s ON i.service_id = s.id " +
                    "INNER JOIN invoices inv ON i.invoice_id = inv.id " +
                    "WHERE inv.invoice_date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble("totalIncome");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalIncome;
    }

    public String getMostPopularServiceForPeriod(Date startDate, Date endDate) {
        String mostPopularService = "";
        try {
            String query = "SELECT s.name AS serviceName, SUM(i.hours) AS totalHours " +
                    "FROM invoice_services i " +
                    "INNER JOIN services s ON i.service_id = s.id " +
                    "INNER JOIN invoices inv ON i.invoice_id = inv.id " +
                    "WHERE inv.invoice_date BETWEEN ? AND ? " +
                    "GROUP BY i.service_id " +
                    "ORDER BY totalHours DESC " +
                    "LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                mostPopularService = resultSet.getString("serviceName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostPopularService;
    }

    public String getTopClientForPeriod(Date startDate, Date endDate) {
        String topClient = "";
        try {
            String query = "SELECT c.name AS clientName, SUM(s.rate_per_hour * i.hours) AS totalAmount " +
                    "FROM invoice_services i " +
                    "INNER JOIN services s ON i.service_id = s.id " +
                    "INNER JOIN invoices inv ON i.invoice_id = inv.id " +
                    "INNER JOIN clients c ON inv.client_id = c.id " +
                    "WHERE inv.invoice_date BETWEEN ? AND ? " +
                    "GROUP BY inv.client_id " +
                    "ORDER BY totalAmount DESC " +
                    "LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                topClient = resultSet.getString("clientName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topClient;
    }
}

public class VAInvoiceSystem {
    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        AnalyticsService analyticsService = new AnalyticsService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to Virtual Assistant System!");
            System.out.println("1. Client Management");
            System.out.println("2. Service Management");
            System.out.println("3. Invoice Management");
            System.out.println("4. Analytics");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    clientManagement(clientDAO, scanner);
                    break;
                case 2:
                    serviceManagement(serviceDAO, scanner);
                    break;
                case 3:
                    invoiceManagement(clientDAO, serviceDAO, invoiceDAO, scanner);
                    break;
                case 4:
                    analyticsManagement(analyticsService, scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void clientManagement(ClientDAO clientDAO, Scanner scanner) {
        while (true) {
            System.out.println("\nClient Management:");
            System.out.println("1. Add Client");
            System.out.println("2. View Clients");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter client name: ");
                    String clientName = scanner.nextLine();
                    clientDAO.addClient(clientName);
                    break;
                case 2:
                    System.out.println("List of Clients:");
                    clientDAO.listClients();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void serviceManagement(ServiceDAO serviceDAO, Scanner scanner) {
        while (true) {
            System.out.println("\nService Management:");
            System.out.println("1. Add Service");
            System.out.println("2. View Services");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter service name: ");
                    String serviceName = scanner.nextLine();
                    System.out.print("Enter rate per hour: ");
                    double ratePerHour = scanner.nextDouble();
                    serviceDAO.addService(serviceName, ratePerHour);
                    break;
                case 2:
                    System.out.println("List of Services:");
                    serviceDAO.listServices();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void invoiceManagement(ClientDAO clientDAO, ServiceDAO serviceDAO, InvoiceDAO invoiceDAO, Scanner scanner) {
        while (true) {
            System.out.println("\nInvoice Management:");
            System.out.println("1. Create Invoice");
            System.out.println("2. Add Service to Invoice");
            System.out.println("3. Delete Invoice");
            System.out.println("4. View Invoices for a Client");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter client ID: ");
                    int clientId = scanner.nextInt();
                    System.out.print("Enter invoice date (yyyy-MM-dd): ");
                    String dateString = scanner.next();
                    Date invoiceDate = Date.valueOf(dateString);
                    invoiceDAO.createInvoice(clientId, invoiceDate);
                    break;
                case 2:
                    System.out.print("Enter invoice ID: ");
                    int invoiceId = scanner.nextInt();
                    System.out.print("Enter service ID: ");
                    int serviceId = scanner.nextInt();
                    System.out.print("Enter hours: ");
                    double hours = scanner.nextDouble();
                    invoiceDAO.addServiceToInvoice(invoiceId, serviceId, hours);
                    break;
                case 3:
                    System.out.print("Enter invoice ID to delete: ");
                    int invoiceIdToDelete = scanner.nextInt();
                    invoiceDAO.deleteInvoice(invoiceIdToDelete);
                    break;
                case 4:
                    System.out.print("Enter client ID: ");
                    int clientIdForInvoices = scanner.nextInt();
                    System.out.println("Invoices for Client:");
                    invoiceDAO.listInvoicesForClient(clientIdForInvoices);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void analyticsManagement(AnalyticsService analyticsService, Scanner scanner) {
        System.out.println("\nAnalytics:");
        System.out.println("Enter date range for analytics (start date and end date in yyyy-MM-dd format):");
        System.out.print("Start Date: ");
        String startDateString = scanner.next();
        System.out.print("End Date: ");
        String endDateString = scanner.next();
        Date startDate = Date.valueOf(startDateString);
        Date endDate = Date.valueOf(endDateString);

        double totalIncome = analyticsService.getTotalIncomeForPeriod(startDate, endDate);
        System.out.println("Total Income for the Period: " + totalIncome);

        String mostPopularService = analyticsService.getMostPopularServiceForPeriod(startDate, endDate);
        System.out.println("Most Popular Service for the Period: " + mostPopularService);

        String topClient = analyticsService.getTopClientForPeriod(startDate, endDate);
        System.out.println("Top Client for the Period: " + topClient);
    }
}