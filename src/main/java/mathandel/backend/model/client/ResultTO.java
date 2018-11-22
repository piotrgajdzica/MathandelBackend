package mathandel.backend.model.client;

public class ResultTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private ItemTO item;
    private RateTO rate;

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

    public ItemTO getItem() {
        return item;
    }

    public ResultTO setItem(ItemTO item) {
        this.item = item;
        return this;
    }

    public RateTO getRate() {
        return rate;
    }

    public ResultTO setRate(RateTO rate) {
        this.rate = rate;
        return this;
    }
}
