package android.example.appchoco.model;

public class User {
    private String ID;
    private String UserName;
    private String ImageURL;
    private String ImageURLcoverimg;
    private String Search;

    public User(String ID, String userName, String imageURL, String imageURLcoverimg, String search) {
        this.ID = ID;
        UserName = userName;
        ImageURL = imageURL;
        ImageURLcoverimg = imageURLcoverimg;
        Search = search;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getImageURLcoverimg() {
        return ImageURLcoverimg;
    }

    public void setImageURLcoverimg(String imageURLcoverimg) {
        ImageURLcoverimg = imageURLcoverimg;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }

    public User()
    {

    }
}
