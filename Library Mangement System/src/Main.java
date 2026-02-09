import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private static Book[] library = new Book[100];
    private static int bookCount = 0;
    private static Scanner sc = new Scanner(System.in);
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {
        int choice = 0;

        System.out.println(ANSI_BLUE + "\n>>>>>>>>>>>>>>>>>>>>>> Setup Library <<<<<<<<<<<<<<<<<<<<<<<<" + ANSI_RESET);
        System.out.print("Enter Library's Name: ");
        sc.nextLine();
        System.out.print("Enter Library's Address: ");
        sc.nextLine();
        System.out.println("\n\n");
        System.out.print("Library is already Created" + ANSI_RESET + "address successfully one" );


        while (choice != 6) {
            System.out.println("dnamic name and address of library");
            System.out.println("1. Add Book (Auto-ID)");
            System.out.println("2. Show All Books");
            System.out.println("3. Show Available Books");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit" );

            String input = getValidatedInput("=> Choose option(1-6): ", "^[1-6]$", "Invalid Option.");
            choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    System.out.println("");
                    addBook();
                    break;
                case 2: showAllBooks();
                    break;
                case 3: showAvailableBooks();
                    break;
                case 4: borrowBook();
                    break;
                case 5: returnBook();
                    break;
                case 6: System.out.println(ANSI_YELLOW + "(^-^)Good Bye!(^-^)" + ANSI_RESET);
                    break;
            }
        }
    }

    // =======>>>> VALIDATION (Reusable Method)
    private static String getValidatedInput(String prompt, String regex, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (Pattern.matches(regex, input)) {
                return input;
            }
            System.out.println(ANSI_RED + ">> " + errorMessage + ANSI_RESET);
        }
    }

    // =======>>>> CASE 1: ADD BOOK
    private static void addBook() {
        if (bookCount >= library.length) {
            System.out.println(ANSI_RED + "Library is full!" + ANSI_RESET);
            return;
        }

        System.out.println("============ ADD BOOK INFO =============");
        String title = getValidatedInput("=> Enter Book's Name : ", "^[a-zA-Z\\s]+$", "Invalid Title (Letters only).");
        String authorName = getValidatedInput("=> Enter Book Author Name: ", "^[a-zA-Z\\s]+$", "Invalid Name (Letters only).");

        //Enter Author Nationality: recheck the validatation
        String nation = getValidatedInput("=> Enter Author Year Active: ", "^(19|20)[0-9]{2}$", "Invalid Active year");
        String year = getValidatedInput("=> Enter Published Year : ", "^(19|20)[0-9]{2}$", "Invalid Year (1900-2099).");

        Author authorObj = new Author(authorName, nation);
        library[bookCount] = new Book(title, authorObj, year);

        System.out.println(ANSI_GREEN + "Book is added successfully\n " + library[bookCount].getId() + ANSI_RESET);
        bookCount++;
    }

    // =======>>>> CASE 2: SHOW ALL BOOKS: table
    private static void showAllBooks() {

        System.out.println("========= ALL BOOKS INFO =========\n");
        if (bookCount == 0) {
            System.out.println(ANSI_YELLOW + "No books in system." + ANSI_RESET);
            return;
        }
        for (int i = 0; i < bookCount; i++) {
            System.out.println(library[i].toString());
        }
        System.out.println("Press Enter to continue...");
    }

    // =======>>>> CASE 3: SHOW AVAILABLE BOOKS : table
    private static void showAvailableBooks() {

        Table table = new Table(5 ,BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        table.addCell("ID");
        table.addCell("AUTHOR");
        table.addCell("PUBLISHED DATE");
        table.addCell("STATUS");
        System.out.println("========= AVAILABLE BOOKS INFO =========\n");
        boolean found = false;
        for (int i = 0; i < bookCount; i++) {
            if (library[i].isAvailable()) {
                System.out.println(library[i].toString());
                found = true;
            }
        }
        if (!found) System.out.println(ANSI_RED + "No books currently available." + ANSI_RESET);
        System.out.println("Press Enter to continue...");
    }

    // =======>>>> CASE 4: BORROW BOOK
    private static void borrowBook() {

        System.out.println("========= BORROW BOOK INFO =========\n");
        String idIn = getValidatedInput("=> Enter Book ID to Borrow ", "^[0-9]+$", "Digits only.");
        int id = Integer.parseInt(idIn);
        System.out.println("Book ID: " + id + "");
        System.out.println("Book Author: " );
        System.out.println("Published Year:" );
        System.out.println("is borrowed successfully");
        for (int i = 0; i < bookCount; i++) {
            if (library[i].getId() == id) {
                if (library[i].isAvailable()) {
                    library[i].setAvailable(false);
                    System.out.println(ANSI_GREEN + "Success borrow" + library[i].getTitle() + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + "This book is already borrowed." + ANSI_RESET);
                }
                return;
            }
        }
        System.out.println(ANSI_RED + "ID Not Found." + ANSI_RESET);
    }

    // =======>>>> CASE 5: RETURN BOOK
    private static void returnBook() {
        System.out.println("========= RETURN BOOK INFO =========\n");
        String idIn = getValidatedInput("Enter Book ID to Return: ", "^[0-9]+$", "Digits only.");
        System.out.println("Book ID: " + idIn + "");
        System.out.println("Book Title");
        System.out.println("Book Author");
        System.out.println("Published Year:");
        int id = Integer.parseInt(idIn);

        for (int i = 0; i < bookCount; i++) {
            if (library[i].getId() == id) {
                if (!library[i].isAvailable()) {
                    library[i].setAvailable(true);
                    System.out.println(ANSI_GREEN + "Booke return successfully" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_YELLOW + "Book is in the library" + ANSI_RESET);
                }
                return;
            }
        }
        System.out.println(ANSI_RED + "ID Not Found." + ANSI_RESET);
    }
}