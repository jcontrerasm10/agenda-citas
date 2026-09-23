package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.proyecto.dao.CitaDAO;
import edu.umg.programacion2.proyecto.modelo.Cita;

public class VentanaPrincipal extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final CitaDAO citaDAO = new CitaDAO();

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal() {
        setTitle("Agenda de Citas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarTabla();
        cargarDatos();
    }

    private void inicializarTabla() {
        String[] columnas = {"ID", "Cliente", "Fecha y hora", "Servicio", "Duracion (min)", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // la tabla es solo de lectura; la edicion se hace por el formulario
            }
        };

        tablaCitas = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0); // limpia la tabla antes de recargar

            List<Cita> citas = citaDAO.listarTodos();
            for (Cita cita : citas) {
                Object[] fila = {
                        cita.getId(),
                        cita.getCliente(),
                        cita.getFechaHora().format(FORMATO_FECHA),
                        cita.getServicio(),
                        cita.getDuracionMinutos(),
                        cita.getEstado().toString()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar el listado de citas. Verifica la conexion a la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}