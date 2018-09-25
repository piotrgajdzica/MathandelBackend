package mathandel.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "moderator_request")
public class ModeratorRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;

    @Column(length = 300)
    private String reason;

    @ManyToOne(cascade = CascadeType.ALL)
    private ModeratorRequestStatus moderatorRequestStatus;

    public ModeratorRequest() {
    }

    public Long getId() {
        return id;
    }

    public ModeratorRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ModeratorRequest setUser(User user) {
        this.user = user;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public ModeratorRequest setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ModeratorRequestStatus getModeratorRequestStatus() {
        return moderatorRequestStatus;
    }

    public ModeratorRequest setModeratorRequestStatus(ModeratorRequestStatus moderatorRequestStatus) {
        this.moderatorRequestStatus = moderatorRequestStatus;
        return this;
    }
}
