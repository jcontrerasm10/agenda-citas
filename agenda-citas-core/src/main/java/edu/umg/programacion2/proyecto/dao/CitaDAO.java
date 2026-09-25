package edu.umg.programacion2.proyecto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.umg.programacion2.proyecto.modelo.EstadoCita;

import edu.umg.programacion2.proyecto.modelo.Cita;

public class CitaDAO {

	public Cita crear(Cita cita) throws SQLException {
	    String sql = "INSERT INTO citas (cliente, fecha_hora, servicio, "
	            + "duracion_minutos, estado, requiere_confirmacion_llamada, "
	            + "es_primera_visita) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conexion = ConexionBD.obtenerConexion();
	         PreparedStatement ps = conexion.prepareStatement(
	                 sql, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, cita.getCliente());
	        ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
	        ps.setString(3, cita.getServicio());
	        ps.setInt(4, cita.getDuracionMinutos());
	        ps.setString(5, cita.getEstado().getValorBD());
	        ps.setBoolean(6, cita.isRequiereConfirmacionLlamada());
	        ps.setBoolean(7, cita.isEsPrimeraVisita());

	        ps.executeUpdate();

	        try (ResultSet generadas = ps.getGeneratedKeys()) {
	            if (generadas.next()) {
	                cita.setId(generadas.getInt(1));
	            }
	        }
	    }
	    return cita;
	}
    
    public List<Cita> listarTodos() throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT id, cliente, fecha_hora, servicio, duracion_minutos, estado "
                + "FROM citas ORDER BY fecha_hora";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }
        }

        return citas;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Cita.
     * Se reutiliza en listarTodos() y en buscarPorId().
     */
    private Cita mapearCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setId(rs.getInt("id"));
        cita.setCliente(rs.getString("cliente"));
        cita.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        cita.setServicio(rs.getString("servicio"));
        cita.setDuracionMinutos(rs.getInt("duracion_minutos"));
        cita.setEstado(EstadoCita.desdeValorBD(rs.getString("estado")));
        cita.setRequiereConfirmacionLlamada(rs.getBoolean("requiere_confirmacion_llamada"));
        cita.setEsPrimeraVisita(rs.getBoolean("es_primera_visita"));
        return cita;
    }
    
    public Optional<Cita> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, cliente, fecha_hora, servicio, duracion_minutos, estado "
                + "FROM citas WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCita(rs));
                }
            }
        }

        return Optional.empty();
    }
    
    public boolean actualizar(Cita cita) throws SQLException {
        String sql = "UPDATE citas SET cliente = ?, fecha_hora = ?, servicio = ?, "
                + "duracion_minutos = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cita.getCliente());
            ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            ps.setString(3, cita.getServicio());
            ps.setInt(4, cita.getDuracionMinutos());
            ps.setString(5, cita.getEstado().getValorBD());
            ps.setInt(6, cita.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM citas WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
}