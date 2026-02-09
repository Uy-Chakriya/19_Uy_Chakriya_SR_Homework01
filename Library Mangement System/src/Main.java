import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private static Scanner sc = new Scanner(System.in);
    private static Book[] library = new Book[100];
    private static int bookCount = 0;
    private static String libName;
    private static String libAddress;

    public static void main(String[] args) {

        setupLibrary();
        defaultBook();

        int choice = 0;
        while (choice != 6) {
            System.out.println("\n" + ANSI_BLUE + "================ "
                                    + libName + " , "
                                    + libAddress + " ================="
                                    + ANSI_RESET);
            System.out.println("1. Add Book (Auto-ID)");
            System.out.println("2. Show All Books");
            System.out.println("3. Show Available Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.println("=================================================\n");

            String input = validateInput(ANSI_YELLOW + "-> Enter your choice (1-6):"
                                            + ANSI_RESET, "^[1-6]$",
                                    "Choose one of the options.");
            choice = Integer.parseInt(input);

            switch (choice) {
                case 1: addBook();
                      break;
                case 2: showAllBooks(false);
                      break;
                case 3: showAvailableBooks(true);
                      break;
                case 4: borrowBook();
                      break;
                case 5: returnBook();
                       break;
                case 6: System.out.println(ANSI_YELLOW + "(^-^) Good Bye! (^-^)" + ANSI_RESET);
                      break;
            }
        }
    }

    private static void defaultBook() {
        library[bookCount++] = new Book("Kolab Pailin", new Author("Nhok Them", "1903-1974"), "1943");
        library[bookCount++] = new Book("Phka Sropoun", new Author("Nou Hach", "1916-1975"), "1949");
        library[bookCount++] = new Book("Sophat", new Author("Rim Kin", "1911-1959"), "1938");
        library[bookCount++] = new Book("Mealea Duong Chet", new Author("Nou Hach", "1916-1975"), "1972");
        library[bookCount++] = new Book("Reamker", new Author("Preah Reach Sambat", "1850-1920"), "1900");
    }

    private static void setupLibrary() {
        System.out.println(ANSI_BLUE + ">>>>>>>>>>>>>>>>>>>>>> Setup Library <<<<<<<<<<<<<<<<<<<<<<<<" + ANSI_RESET);
        libName = validateInput("Enter Library's Name: ", "^[a-zA-Z\\s]+$", "Allow input only letters");
        libAddress = validateInput("Enter Library's Address: ", "^[a-zA-Z\\s]+$", "Allow input only letters");
        System.out.println(ANSI_GREEN + "Library created successfully!\n" + ANSI_RESET);
    }

    private static String validateInput(String prompt, String regex, String eMessage) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (Pattern.matches(regex, input))
                return input;
            System.out.println(ANSI_RED + eMessage + ANSI_RESET);
        }
    }

    private static void addBook() {
        if (bookCount >= library.length) {
            System.out.println(ANSI_RED + "Library is full" + ANSI_RESET);
            return;
        }
        System.out.println("\n============ ADD BOOK INFO =============");
        String title = validateInput("Enter Book Title: ", "^[a-zA-Z\\s0-9]+$", "Invalid Title.");
        String authorName = validateInput("Enter Book Author Name: ", "^[a-zA-Z\\s.]+$", "Invalid Name.");

        int bornYear = 0;
        int deadYear = 0;
        String activeYear = "";
        
        while (true) {
            activeYear = validateInput(
                    "Enter Book Author Year Active: ",
                    "^[0-9]{4}-[0-9]{4}$",
                    "Please enter number in structure like this [1234-5678]."
            );

            String[] years = activeYear.split("-");
            bornYear = Integer.parseInt(years[0]);
            deadYear = Integer.parseInt(years[1]);

            if (bornYear > deadYear) {
                System.out.println(ANSI_RED + "You can not input the born year smaller than dead year." + ANSI_RESET);
            } else if ((deadYear - bornYear) < 5) { // Assuming "too close" means less than 5 years
                System.out.println(ANSI_RED + "Passed away year and born year are too close." + ANSI_RESET);
            } else if (bornYear > 2026 || deadYear > 2026) {
                System.out.println(ANSI_RED + "Years cannot be in the future (Current year is 2026)." + ANSI_RESET);
            } else {
                break;
            }
        }
        String pubYearStr;
        int pubYear;
        while (true) {
            pubYearStr = validateInput("Enter Book Publish Year: ", "^[0-9]{4}$",
                    ANSI_RED + "Invalid Year! Please follow this format YYYY and allow numbers only!" + ANSI_RESET);
            pubYear = Integer.parseInt(pubYearStr);

            if (pubYear < bornYear + 18) {
                System.out.println(ANSI_YELLOW + "This age of the Author is too young to publish the book!" + ANSI_RESET);
            } else if (pubYear > deadYear) {
                System.out.println(ANSI_RED + "Book cannot be published after the author has died." + ANSI_RESET);
            } else if (pubYear > 2026) {
                System.out.println(ANSI_RED + "Book cannot be published in the future." + ANSI_RESET);
            } else {
                break;
            }
        }

        library[bookCount] = new Book(title, new Author(authorName, activeYear), String.valueOf(pubYear));
        System.out.println(ANSI_GREEN + "Book ID: " + library[bookCount].getId() + " added successfully!" + ANSI_RESET);
        bookCount++;
    }

    private static void showAllBooks(boolean onlyAvailable) {
        if (bookCount == 0) {
            System.out.println(ANSI_YELLOW + "No books in system." + ANSI_RESET);
            return;
        }

        int rowsPerPage = 3;
        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) bookCount / rowsPerPage);

        while (true) {
            Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
            table.addCell("ID");
            table.addCell("TITLE");
            table.addCell("AUTHOR");
            table.addCell("PUBLISHED");
            table.addCell("STATUS");

            // Calculate start and end index for current page
            int startIndex = (currentPage - 1) * rowsPerPage;
            int endIndex = Math.min(startIndex + rowsPerPage, bookCount);

            for (int i = startIndex; i < endIndex; i++) {
                table.addCell(String.valueOf(library[i].getId()));
                table.addCell(library[i].getTitle());
                table.addCell(library[i].getAuthor().getName());
                table.addCell(library[i].getYear());

                String statusText;
                if (library[i].isAvailable()) {
                    statusText = ANSI_GREEN + "Available" + ANSI_RESET;
                } else {
                    statusText = ANSI_RED + "Unavailable" + ANSI_RESET;
                }
                table.addCell(statusText);
            }

            System.out.println(table.render());
            System.out.println("1. Next Page    2. Previous Page    3. First Page    4. Last Page    5. Exit");

            String choice = validateInput("-> Enter your choice: ", "^[1-5]$", "Please choose one of the options [1-6].");

            if (choice.equals("1")) {
                if (currentPage < totalPages) currentPage++;
                else System.out.println(ANSI_RED + ">> You are already on the last page." + ANSI_RESET);
            } else if (choice.equals("2")) {
                if (currentPage > 1) currentPage--;
                else System.out.println(ANSI_RED + ">> You are already on the first page." + ANSI_RESET);
            } else if (choice.equals("3")) {
                currentPage = 1;
            } else if (choice.equals("4")) {
                currentPage = totalPages;
            } else if (choice.equals("5")) {
                break; // Exit the pagination loop
            }
        }
    }

    private static void showAvailableBooks(boolean onlyAvailable) {
        if (bookCount == 0) {
            System.out.println(ANSI_YELLOW + "No books in system." + ANSI_RESET);
            return;
        }

        boolean available = false;
        for (int i = 0; i < bookCount; i++) {
            if (library[i].isAvailable()) {
                available = true;
                break;
            }
        }

        if (!available) {
            System.out.println(ANSI_RED + "No books available at the moment." + ANSI_RESET);
            return;
        }

        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        table.addCell("ID");
        table.addCell("TITLE");
        table.addCell("AUTHOR");
        table.addCell("PUBLISHED");
        table.addCell("STATUS");

        for (int i = 0; i < bookCount; i++) {
            if (library[i].isAvailable()) {
                table.addCell(String.valueOf(library[i].getId()));
                table.addCell(library[i].getTitle());
                table.addCell(library[i].getAuthor().getName());
                table.addCell(library[i].getYear());
                table.addCell(ANSI_GREEN + "Available" + ANSI_RESET);
            }
        }

        System.out.println(ANSI_GREEN + "------ AVAILABLE BOOKS --------" + ANSI_RESET);
        System.out.println(table.render());
        System.out.print("Press Enter to continue...");
        sc.nextLine();
    }

    private static void borrowBook() {
        System.out.println("\n\n========= BORROW BOOK INFO =========");
        String idStr = validateInput("=> Enter Book ID to Borrow: ", "^[0-9]+$", "Digits only.");
        int id = Integer.parseInt(idStr);

        for (int i = 0; i < bookCount; i++) {
            if (library[i].getId() == id) {
                if (library[i].isAvailable()) {
                    library[i].setAvailable(false);
                    System.out.println(ANSI_GREEN + "Success! Borrowed: " + library[i].getTitle() + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + "Already borrowed." + ANSI_RESET);
                }
                return;
            }
        }
        System.out.println(ANSI_RED + "ID Not Found." + ANSI_RESET);
    }

    private static void returnBook() {
        System.out.println("\n\n========= RETURN BOOK INFO =========");
        String idStr = validateInput("=> Enter Book ID to Return: ", "^[0-9]+$", "Digits only.");
        int id = Integer.parseInt(idStr);

        for (int i = 0; i < bookCount; i++) {
            if (library[i].getId() == id) {
                if (!library[i].isAvailable()) {
                    library[i].setAvailable(true);
                    System.out.println(ANSI_GREEN + "Success! Returned: " + library[i].getTitle() + ANSI_RESET);
                } else {
                    System.out.println(ANSI_YELLOW + "Book is already in the library." + ANSI_RESET);
                }
                return;
            }
        }
        System.out.println(ANSI_RED + "ID Not Found." + ANSI_RESET);
    }
}