public abstract class  Book {
    private String title;
    private String author;
    private int id;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;

    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    public int getId() {
        return id;
    }
    public void setId(int newId) {
        this.id = newId;
    }
    public abstract void inThongTin();


}
