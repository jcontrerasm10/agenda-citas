package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.proyecto.dao.CitaDAO;
import edu.umg.programacion2.proyecto.modelo.Cita;
import edu.umg.programacion2.proyecto.modelo.EstadoCita;

public class VentanaPrincipal extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final CitaDAO citaDAO = new CitaDAO();

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;

    private JTextField txtCliente;
    private JTextField txtFechaHora;
    private JTextField txtServicio;
    private JTextField txtDuracion;

    public VentanaPrincipal() {
        setTitle("Agenda de Citas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarTabla();
        inicializarFormulario();
        cargarDatos();
    }

    private void inicializarTabla() {
        String[] columnas = {"ID", "Cliente", "Fecha y hora", "Servicio", "Duracion (min)", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void inicializarFormulario() {
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));

        txtCliente = new JTextField();
        txtFechaHora = new JTextField();
        txtServicio = new JTextField();
        txtDuracion = new JTextField();

        panelCampos.add(new JLabel("Cliente:"));
        panelCampos.add(txtCliente);
        panelCampos.add(new JLabel("Fecha y hora (yyyy-MM-dd HH:mm):"));
        panelCampos.add(txtFechaHora);
        panelCampos.add(new JLabel("Servicio:"));
        panelCampos.add(txtServicio);
        panelCampos.add(new JLabel("Duracion (min):"));
        panelCampos.add(txtDuracion);

        JButton btnAgregar = new JButton("Agregar cita");
        btnAgregar.addActionListener(e -> agregarCita());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);

        JPanel panelFormulario = new JPanel(new BorderLayout());
        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.SOUTH);
    }

    private void agregarCita() {
        String cliente = txtCliente.getText().trim();
        String servicio = txtServicio.getText().trim();
        String textoFecha = txtFechaHora.getText().trim();
        String textoDuracion = txtDuracion.getText().trim();

        if (cliente.isEmpty() || servicio.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El cliente y el servicio no pueden quedar vacios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(textoFecha, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "La fecha y hora deben tener el formato yyyy-MM-dd HH:mm (ej. 2026-09-20 14:30).",
                    "Formato invalido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fechaHora.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this,
                    "La fecha y hora de la cita no puede ser una fecha que ya paso.",
                    "Fecha invalida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int duracion;
        try {
            duracion = Integer.parseInt(textoDuracion);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "La duracion debe ser un numero entero.",
                    "Dato invalido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (duracion <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La duracion estimada debe ser mayor a cero minutos.",
                    "Dato invalido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cita nuevaCita = new Cita(cliente, fechaHora, servicio, duracion, EstadoCita.PENDIENTE);

        try {
            citaDAO.crear(nuevaCita);
            cargarDatos();
            limpiarFormulario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la cita. Intenta de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtCliente.setText("");
        txtFechaHora.setText("");
        txtServicio.setText("");
        txtDuracion.setText("");
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);

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