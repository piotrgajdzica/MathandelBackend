package mathandel.backend.client.response;

public class FacebookResponse {

    private boolean userExists;

    public boolean isUserExists() {
        return userExists;
    }

    public FacebookResponse setUserExists(boolean userExists) {
        this.userExists = userExists;
        return this;
    }
}
