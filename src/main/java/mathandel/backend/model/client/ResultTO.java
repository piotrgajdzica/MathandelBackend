package mathandel.backend.model.client;

public class ResultTO {

    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private Long editionId;
    private Long productId;
    private Boolean isRated;

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

    public String getSenderUsername() {
        return senderUsername;
    }

    public ResultTO setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
        return this;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public ResultTO setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public ResultTO setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
        return this;
    }

    public Long getEditionId() {
        return editionId;
    }

    public ResultTO setEditionId(Long editionId) {
        this.editionId = editionId;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public ResultTO setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Boolean getRated() {
        return isRated;
    }

    public ResultTO setRated(Boolean rated) {
        isRated = rated;
        return this;
    }
}
