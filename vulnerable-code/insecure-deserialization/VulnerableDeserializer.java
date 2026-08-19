/*
 * Insecure Deserialization Demonstration
 *
 * INTENTIONALLY VULNERABLE CODE
 *
 * This example deserializes attacker-controlled input without
 * validating which object types may be reconstructed.
 *
 * Educational and authorized laboratory use only.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class VulnerableDeserializer {

    public static Object deserialize(InputStream inputStream)
            throws IOException, ClassNotFoundException {

        /*
         * VULNERABLE:
         * Any serialized object accepted by ObjectInputStream
         * may be reconstructed.
         */
        try (ObjectInputStream objectInputStream =
                     new ObjectInputStream(inputStream)) {

            return objectInputStream.readObject();
        }
    }
}
