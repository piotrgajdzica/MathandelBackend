package mathandel.backend.model.client;

import mathandel.backend.model.server.enums.RateTypeName;

@Deprecated
public class RateTypeTO {

    private Long id;
    private RateTypeName rateTypeName;

    public Long getId() {
        return id;
    }

    public RateTypeTO setId(Long id) {
        this.id = id;
        return this;
    }

    public RateTypeName getRateTypeName() {
        return rateTypeName;
    }

    public RateTypeTO setRateTypeName(RateTypeName rateTypeName) {
        this.rateTypeName = rateTypeName;
        return this;
    }
}
