package mathandel.backend.client.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DefinedGroupTO {

    private Long id;
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;
    private int numberOfProducts;

    public Long getId() {
        return id;
    }

    public DefinedGroupTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DefinedGroupTO setName(String name) {
        this.name = name;
        return this;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public DefinedGroupTO setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        return this;
    }
}
