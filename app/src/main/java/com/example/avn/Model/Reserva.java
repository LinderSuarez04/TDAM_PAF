package com.example.avn.Model;

public class Reserva {
    private int cancha_id;
    private int usuario_id;
    private String fecha;
    private String horario;
    private String metodo_pago;

    // Constructor vacío requerido por Retrofit
    public Reserva() {
    }

    // Constructor sin el parámetro id
    public Reserva(int cancha_id, int usuario_id, String fecha, String horario, String metodo_pago) {
        this.cancha_id = cancha_id;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.horario = horario;
        this.metodo_pago = metodo_pago;
    }

    // Getters y setters (puedes generarlos automáticamente en Android Studio)

    public int getCancha_id() {
        return cancha_id;
    }

    public void setCancha_id(int cancha_id) {
        this.cancha_id = cancha_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }
}
