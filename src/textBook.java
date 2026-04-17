public class textBook extends Book {
    private String subject;
    private String status;
    public textBook(String title, String author, String subject, int id, String status){
        super(title,author,id);
        this.subject = subject;
    }
    public textBook(String title, String author, String subject, String status){
        super(title,author);
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String Newstatus) {
        this.status = Newstatus;
    }
    public String getSubject() {
        return this.subject;
    }
    public void setSubject(String Newsubject) {
        this.subject = Newsubject;
    }
    @Override
    public void inThongTin() {
        System.out.println("ID: "+ getId() + " | Name: " + getTitle() + " | Author: " + getAuthor() + " | Subject: " + getSubject());
    }
}
