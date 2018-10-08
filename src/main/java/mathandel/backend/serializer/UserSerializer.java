package mathandel.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import mathandel.backend.model.User;

import java.io.IOException;
import java.util.Set;

public class UserSerializer extends JsonSerializer<Set<User>> {

    @Override
    public void serialize(Set<User> moderators, JsonGenerator gen, SerializerProvider provider) throws IOException {
        long[] idsArray = moderators.stream().mapToLong(User::getId).toArray();
        gen.writeArray(idsArray, 0, idsArray.length);
    }
}
