package bharatiya.innovators.resaiqrscanner;

public class AuthModelData {
    private String name, email, password, dateTime;
    private long timeStamp;

    public AuthModelData(String name, String email, String password, String dateTime, long timeStamp) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateTime = dateTime;
        this.timeStamp = timeStamp;
    }

    public AuthModelData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
