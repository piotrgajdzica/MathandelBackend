package mathandel.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "defined_groups")
public class DefinedGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}
