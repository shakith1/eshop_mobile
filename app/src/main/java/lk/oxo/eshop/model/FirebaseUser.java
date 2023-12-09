package lk.oxo.eshop.model;

public class FirebaseUser extends User {
    private String uid;

    public FirebaseUser() {

    }
    public FirebaseUser(String uid, String email, String fname, String lname) {
        super(email, fname, lname);
        this.uid = uid;
    }

    public FirebaseUser(String uid, User user) {
        super(user.getEmail(), user.getFname(), user.getLname(), user.getMobile());
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
