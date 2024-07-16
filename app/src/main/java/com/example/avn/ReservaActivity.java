package com.example.avn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.avn.Model.Reserva;
import com.example.avn.interfas.API;
import com.example.avn.interfas.ApiClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservaActivity extends AppCompatActivity {

    private EditText editTextFecha;
    private Spinner spinnerHorario;
    private Spinner spinnerMetodo_Pago;
    private Button btnReservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        // Obtén el nombre de la cancha del Intent
        String nombreCancha = getIntent().getStringExtra("nombreCancha");
        int idCancha = getIntent().getIntExtra("idCancha", -1);

        // Muestra el nombre de la cancha en el TextView
        TextView textViewNombreCancha = findViewById(R.id.textViewNombreCancha);
        textViewNombreCancha.setText(nombreCancha);

        // Inicializa las vistas y el botón
        editTextFecha = findViewById(R.id.editTextFecha);
        spinnerHorario = findViewById(R.id.spinnerHorario);
        spinnerMetodo_Pago = findViewById(R.id.spinnerMetodoPago);
        btnReservar = findViewById(R.id.btnRealizarReserva);

        // Maneja el clic en el botón de reserva
        btnReservar.setOnClickListener(v -> realizarReserva(nombreCancha));
    }

    private void realizarReserva(String nombreCancha) {
        // Obtén los valores de los campos
        String fecha = editTextFecha.getText().toString();
        String horario = spinnerHorario.getSelectedItem().toString();

        // Verifica si hay elementos en el spinner de método de pago antes de obtener el valor
        String metodoPago;
        if (spinnerMetodo_Pago.getCount() > 0) {
            metodoPago = spinnerMetodo_Pago.getSelectedItem().toString();
        } else {
            // Manejo de error o establecer un valor predeterminado
            metodoPago = "Pago no especificado";
        }

        // Log para verificar el valor de metodoPago
        Log.d("ReservaActivity", "Metodo de Pago: " + metodoPago);

        // Obtén el token almacenado en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Bearer ", MODE_PRIVATE);
        String tuAuthToken = preferences.getString("AUTH_TOKEN", "super-secret");

        // Log para verificar el valor del token
        Log.d("ReservaActivity", "Token: " + tuAuthToken);

        // Verifica que el token no esté vacío antes de realizar la solicitud
        if (!tuAuthToken.isEmpty()) {
            // Obtiene la instancia de Retrofit desde ApiClient
            Retrofit retrofit = ApiClient.getApiClient();
            API api = retrofit.create(API.class);

            // Obtiene los IDs de la cancha y del usuario
            int canchaId = obtenerCanchaId(nombreCancha);
            int usuarioId = obtenerUsuarioId();

            // Crea el objeto Reserva con los datos
            Reserva reserva = new Reserva( canchaId, usuarioId, fecha, horario, metodoPago);

            // Log para verificar los datos de la reserva antes de la llamada
            Log.d("ReservaActivity", "Datos de reserva - "
                    + "ID de la Cancha: " + reserva.getCancha_id()
                    + ", ID del Usuario: " + reserva.getUsuario_id()
                    + ", Fecha: " + reserva.getFecha()
                    + ", Horario: " + reserva.getHorario()
                    + ", Método de Pago: " + reserva.getMetodo_pago());

            // Realiza la llamada a la API para guardar la reserva
            Call<Void> call = api.guardarReserva("Bearer " + tuAuthToken, reserva);

            // Log para verificar el inicio de la llamada a la API
            Log.d("ReservaActivity", "Realizando la llamada a la API...");

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("ReservaActivity", "Respuesta de la API recibida");

                    if (response.isSuccessful()) {
                        // La reserva se ha creado correctamente
                        Toast.makeText(ReservaActivity.this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                        // Puedes agregar aquí la lógica para navegar a otra actividad o realizar otras acciones
                    } else {
                        // Hubo un error al realizar la reserva
                        Toast.makeText(ReservaActivity.this, "Error al realizar la reserva", Toast.LENGTH_SHORT).show();
                        // Log para verificar el código de error de la respuesta
                        Log.e("ReservaActivity", "Código de error: " + response.code());

                        // Log para imprimir el cuerpo de la respuesta
                        try {
                            Log.e("ReservaActivity", "Respuesta del servidor: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Hubo un error de red al realizar la reserva
                    Toast.makeText(ReservaActivity.this, "Error de red al realizar la reserva", Toast.LENGTH_SHORT).show();
                    // Log para verificar el mensaje de error
                    Log.e("ReservaActivity", "Error de red: " + t.getMessage());
                }
            });
        } else {
            // Maneja el caso en que el token no está disponible (puede redirigir a la pantalla de inicio de sesión)
            Toast.makeText(ReservaActivity.this, "Token de autenticación no válido. Inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
            // Aquí puedes implementar la lógica para redirigir a la pantalla de inicio de sesión
        }
    }

    private int obtenerCanchaId(String nombreCancha) {

        return getIntent().getIntExtra("idCancha", -1);
    }

    private int obtenerUsuarioId() {
        SharedPreferences preferences = getSharedPreferences("Bearer ", MODE_PRIVATE);
        return preferences.getInt("USER_ID", -1); // -1 es el valor predeterminado si no se encuentra el ID
    }
}

