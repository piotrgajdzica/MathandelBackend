package mathandel.backend.model.client.request;

public class MakeUserEditionModeratorRequest {

    private String username;

    public String getUsername() {
        return username;
    }

    public MakeUserEditionModeratorRequest setUsername(String username) {
        this.username = username;
        return this;
    }
}
