package com.slgr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.*;

public class test {
    public static void main(String[] args) throws SQLException {
        // Gets sensitive information from a .env file
        Dotenv dotenv = Dotenv.load();

        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");


        // Connects to the database and executes the query
        Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM get_team_name_logo()";
        ResultSet results = statement.executeQuery(query);


        // Prints the results
        while (results.next()) {
            String teamName = results.getString("team_name");
            String teamLogo = results.getString("logo_link");
        }
    }
}
