package mathandel.backend.client.model;

import mathandel.backend.model.enums.RoleName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RoleTO {

    private RoleName roleName;

    public RoleName getRoleName() {
        return roleName;
    }

    public RoleTO setRoleName(RoleName roleName) {
        this.roleName = roleName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RoleTO roleTO = (RoleTO) o;

        return new EqualsBuilder()
                .append(roleName, roleTO.roleName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(roleName)
                .toHashCode();
    }
}
