/*
 * Insecure Deserialization Remediation
 *
 * SECURE VERSION
 *
 * Restricts deserialization using an ObjectInputFilter.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;

public class SecureDeserializer {

    public static Object deserialize(InputStream inputStream)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream objectInputStream =
                     new ObjectInputStream(inputStream)) {

            /*
             * Only allow objects from the expected application package.
             * Reject everything else.
             */
            ObjectInputFilter filter =
                    ObjectInputFilter.Config.createFilter(
                            "com.myapp.*;java.base/*;!*"
                    );

            objectInputStream.setObjectInputFilter(filter);

            return objectInputStream.readObject();
        }
    }
}
