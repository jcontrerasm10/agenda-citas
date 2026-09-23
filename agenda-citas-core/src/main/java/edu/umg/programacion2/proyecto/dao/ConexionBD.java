package edu.umg.programacion2.proyecto.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mariadb://localhost:3306/agenda_citas_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "Cap2026*";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}