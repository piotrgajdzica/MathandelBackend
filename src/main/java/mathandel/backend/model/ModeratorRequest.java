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

    public ModeratorRequest() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ModeratorRequestStatus getModeratorRequestStatus() {
        return moderatorRequestStatus;
    }

    public void setModeratorRequestStatus(ModeratorRequestStatus moderatorRequestStatus) {
        this.moderatorRequestStatus = moderatorRequestStatus;
    }
}
