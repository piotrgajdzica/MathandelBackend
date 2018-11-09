package mathandel.backend.model.client;

public class ResultTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long editionId;

    public Long getId() {
        return id;
    }

    public ResultTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSenderId() {
        return senderId;
    }

    public ResultTO setSenderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public ResultTO setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public Long getEditionId() {
        return editionId;
    }

    public ResultTO setEditionId(Long editionId) {
        this.editionId = editionId;
        return this;
    }
}
