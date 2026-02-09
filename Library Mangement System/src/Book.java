class Book {
    private static int idCounter = 1001;
    private int id;
    private String title;
    private Author author;
    private String year;
    private boolean isAvailable;

    public Book(String title, Author author, String year) {
        this.id = idCounter++;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean status) { this.isAvailable = status; }

    @Override
    public String toString() {
        String statusText = isAvailable ? "\u001B[32mAvailable\u001B[0m" : "\u001B[31mBorrowed\u001B[0m";
        return String.format("ID: %d | Title: %-15s | Author: %-15s | Year: %s | Status: %s",
                id, title, author.getName(), year, statusText);
    }
}