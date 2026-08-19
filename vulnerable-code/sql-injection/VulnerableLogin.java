/*
 * SQL Injection Demonstration
 *
 * INTENTIONALLY VULNERABLE CODE
 *
 * This example demonstrates unsafe construction of SQL queries
 * using untrusted user-controlled input.
 *
 * Educational and authorized laboratory use only.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VulnerableLogin {

    public static boolean authenticate(
            Connection connection,
            String username,
            String password) throws SQLException {

        /*
         * VULNERABLE:
         * User-controlled values are concatenated directly
         * into the SQL statement.
         */
        String query =
                "SELECT * FROM users WHERE username='"
                + username
                + "' AND password='"
                + password
                + "'";

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            return result.next();
        }
    }
}
