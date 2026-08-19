/*
 * SQL Injection Remediation
 *
 * SECURE VERSION
 *
 * Uses a PreparedStatement so user-controlled values are
 * handled as parameters rather than executable SQL syntax.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecureLogin {

    private static final String AUTH_QUERY =
            "SELECT 1 FROM users "
            + "WHERE username = ? AND password = ?";

    public static boolean authenticate(
            Connection connection,
            String username,
            String password) throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(AUTH_QUERY)) {

            /*
             * User-controlled values are bound as data.
             */
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}
