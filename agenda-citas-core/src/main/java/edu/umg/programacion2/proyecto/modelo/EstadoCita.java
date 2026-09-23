package edu.umg.programacion2.proyecto.modelo;

public enum EstadoCita {
    PENDIENTE("pendiente"),
    CONFIRMADA("confirmada"),
    CANCELADA("cancelada");

    private final String valorBD;

    EstadoCita(String valorBD) {
        this.valorBD = valorBD;
    }

    public String getValorBD() {
        return valorBD;
    }

    /**
     * Convierte el texto guardado en la columna 'estado' de la BD
     * al valor del enum correspondiente.
     */
    public static EstadoCita desdeValorBD(String valor) {
        for (EstadoCita estado : values()) {
            if (estado.valorBD.equalsIgnoreCase(valor)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no valido: " + valor);
    }

    @Override
    public String toString() {
        // Texto legible para mostrar en la tabla/formulario de Swing
        return valorBD.substring(0, 1).toUpperCase() + valorBD.substring(1);
    }
}