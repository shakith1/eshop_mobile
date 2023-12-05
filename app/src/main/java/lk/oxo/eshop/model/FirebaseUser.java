package lk.oxo.eshop.model;

public class FirebaseUser {
    private String uid;
    private String email;
    private String fname;
    private String lname;

    public FirebaseUser(String uid, String email, String fname, String lname) {
        this.uid = uid;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
