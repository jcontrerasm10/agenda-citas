package edu.umg.programacion2.proyecto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import edu.umg.programacion2.proyecto.modelo.Cita;

public class CitaDAO {

    public Cita crear(Cita cita) throws SQLException {
        String sql = "INSERT INTO citas (cliente, fecha_hora, servicio, duracion_minutos, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cita.getCliente());
            ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            ps.setString(3, cita.getServicio());
            ps.setInt(4, cita.getDuracionMinutos());
            ps.setString(5, cita.getEstado().getValorBD());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cita.setId(rs.getInt(1));
                }
            }
        }

        return cita;
    }
}