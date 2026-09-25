package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
    private JComboBox<EstadoCita> comboEstado;
    private JCheckBox chkRequiereConfirmacionLlamada;
    private JCheckBox chkEsPrimeraVisita;

    private int idSeleccionado = -1; // -1 = no hay ninguna cita seleccionada (modo crear)

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

        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionEnFormulario();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void inicializarFormulario() {
    	JPanel panelCampos = new JPanel(new GridLayout(7, 2, 5, 5));

        txtCliente = new JTextField();
        txtFechaHora = new JTextField();
        txtServicio = new JTextField();
        txtDuracion = new JTextField();
        comboEstado = new JComboBox<>(EstadoCita.values());
        chkRequiereConfirmacionLlamada = new JCheckBox();
        chkEsPrimeraVisita = new JCheckBox();

        panelCampos.add(new JLabel("Cliente:"));
        panelCampos.add(txtCliente);
        panelCampos.add(new JLabel("Fecha y hora (yyyy-MM-dd HH:mm):"));
        panelCampos.add(txtFechaHora);
        panelCampos.add(new JLabel("Servicio:"));
        panelCampos.add(txtServicio);
        panelCampos.add(new JLabel("Duracion (min):"));
        panelCampos.add(txtDuracion);
        panelCampos.add(new JLabel("Estado:"));
        panelCampos.add(comboEstado);
        panelCampos.add(new JLabel("Requiere confirmación por llamada:"));
        panelCampos.add(chkRequiereConfirmacionLlamada);
        panelCampos.add(new JLabel("Es primera visita;"));
        panelCampos.add(chkEsPrimeraVisita);

        JButton btnAgregar = new JButton("Agregar cita");
        btnAgregar.addActionListener(e -> agregarCita());

        JButton btnActualizar = new JButton("Actualizar cita seleccionada");
        btnActualizar.addActionListener(e -> actualizarCita());

        JButton btnEliminar = new JButton("Eliminar cita seleccionada");
        btnEliminar.addActionListener(e -> eliminarCita());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        JPanel panelFormulario = new JPanel(new BorderLayout());
        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.SOUTH);
    }

    /**
     * Lee y valida los campos comunes del formulario (cliente, servicio,
     * fecha/hora, duracion). No valida "fecha pasada" aqui porque esa regla
     * solo aplica al crear, no al reprogramar una cita existente.
     * Devuelve null si algo es invalido (y ya mostro el JOptionPane correspondiente).
     */
    private Cita leerFormulario() {
        String cliente = txtCliente.getText().trim();
        String servicio = txtServicio.getText().trim();
        String textoFecha = txtFechaHora.getText().trim();
        String textoDuracion = txtDuracion.getText().trim();

        if (cliente.isEmpty() || servicio.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El cliente y el servicio no pueden quedar vacios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(textoFecha, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "La fecha y hora deben tener el formato yyyy-MM-dd HH:mm (ej. 2026-09-20 14:30).",
                    "Formato invalido",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int duracion;
        try {
            duracion = Integer.parseInt(textoDuracion);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "La duracion debe ser un numero entero.",
                    "Dato invalido",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (duracion <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La duracion estimada debe ser mayor a cero minutos.",
                    "Dato invalido",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        EstadoCita estado = (EstadoCita) comboEstado.getSelectedItem();
        boolean requiereConfirmacionLlamada = chkRequiereConfirmacionLlamada.isSelected();
        boolean esPrimeraVisita = chkEsPrimeraVisita.isSelected();
        return new Cita(cliente, fechaHora, servicio, duracion, estado,
                requiereConfirmacionLlamada, esPrimeraVisita);
    }

    private void agregarCita() {
        Cita datos = leerFormulario();
        if (datos == null) {
            return;
        }

        if (datos.getFechaHora().isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this,
                    "La fecha y hora de la cita no puede ser una fecha que ya paso.",
                    "Fecha invalida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cita nuevaCita = new Cita(datos.getCliente(), datos.getFechaHora(),
                datos.getServicio(), datos.getDuracionMinutos(), EstadoCita.PENDIENTE,
                datos.isRequiereConfirmacionLlamada(), datos.isEsPrimeraVisita());
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

    private void actualizarCita() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una cita de la tabla para actualizar.",
                    "Ninguna cita seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cita datos = leerFormulario();
        if (datos == null) {
            return;
        }

        datos.setId(idSeleccionado);

        try {
            boolean actualizado = citaDAO.actualizar(datos);
            if (actualizado) {
                cargarDatos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontro la cita a actualizar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar la cita.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCita() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una cita de la tabla para eliminar.",
                    "Ninguna cita seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar esta cita? Esta accion no se puede deshacer.",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean eliminado = citaDAO.eliminar(idSeleccionado);
            if (eliminado) {
                cargarDatos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontro la cita a eliminar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar la cita.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            return;
        }

        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtCliente.setText((String) modeloTabla.getValueAt(fila, 1));
        txtFechaHora.setText((String) modeloTabla.getValueAt(fila, 2));
        txtServicio.setText((String) modeloTabla.getValueAt(fila, 3));
        txtDuracion.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));

        String estadoTexto = (String) modeloTabla.getValueAt(fila, 5);
        for (int i = 0; i < comboEstado.getItemCount(); i++) {
            EstadoCita estado = comboEstado.getItemAt(i);
            if (estado.toString().equalsIgnoreCase(estadoTexto)) {
                comboEstado.setSelectedItem(estado);
                break;
            }
        }
        chkRequiereConfirmacionLlamada.setSelected((boolean) modeloTabla.getValueAt(fila, 6));
        chkEsPrimeraVisita.setSelected((boolean) modeloTabla.getValueAt(fila, 7));
    }

    private void limpiarFormulario() {
        txtCliente.setText("");
        txtFechaHora.setText("");
        txtServicio.setText("");
        txtDuracion.setText("");
        comboEstado.setSelectedItem(EstadoCita.PENDIENTE);
        chkRequiereConfirmacionLlamada.setSelected(false);
        chkEsPrimeraVisita.setSelected(false);
        idSeleccionado = -1;
        tablaCitas.clearSelection();
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