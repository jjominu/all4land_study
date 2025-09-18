package test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;

public class cnnTest {

	private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";

    @Test
    public void testConnection() throws Exception {
        Class.forName(DRIVER);
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
