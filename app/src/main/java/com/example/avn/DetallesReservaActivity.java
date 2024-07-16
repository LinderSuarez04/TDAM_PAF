package com.example.avn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.avn.Model.Cancha;
import com.example.avn.Model.ReservaInfo;
import com.example.avn.interfas.API;
import com.example.avn.interfas.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DetallesReservaActivity extends AppCompatActivity {

    private List<ReservaInfo> listaReservas;
    private List<Cancha> listaCanchas;  // Agrega la lista de canchas

    private MiReservaAdapter reservaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_reserva);

        Log.d("DetallesReservaActivity", "onCreate llamado");

        // Inicializa la lista y el adaptador
        ListView listViewReservas = findViewById(R.id.listViewReservas);
        listaReservas = new ArrayList<>();
        listaCanchas = new ArrayList<>();  // Inicializa la lista de canchas
        reservaAdapter = new MiReservaAdapter(this, listaReservas, listaCanchas);

        // Configura el adaptador para la lista
        listViewReservas.setAdapter(reservaAdapter);

        // Obtiene el token almacenado en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Bearer ", Context.MODE_PRIVATE);
        String authToken = preferences.getString("AUTH_TOKEN", "Bearer ");

        // Obtiene el ID del usuario almacenado en SharedPreferences
        int userId = preferences.getInt("USER_ID", -1);

        Log.d("DetallesReservaActivity", "AuthToken: " + authToken);
        Log.d("DetallesReservaActivity", "UserID: " + userId);

        // Verifica si userId es válido antes de hacer la llamada a la API
        if (userId != -1 && !TextUtils.isEmpty(authToken)) {
            // Crea una instancia de la interfaz Retrofit
            API api = ApiClient.getApiClient().create(API.class);

            Log.d("DetallesReservaActivity", "Realizando llamada a la API...");

            // Hace la llamada a la API con el token en la cabecera y el ID del usuario en la ruta
            Call<List<ReservaInfo>> call = api.obtenerReservasPorUsuario("Bearer " + authToken, userId);

            // Maneja la respuesta de la llamada
            call.enqueue(new Callback<List<ReservaInfo>>() {
                @Override
                public void onResponse(Call<List<ReservaInfo>> call, Response<List<ReservaInfo>> response) {
                    Log.d("DetallesReservaActivity", "Respuesta recibida");

                    if (response.isSuccessful()) {
                        // Actualiza la lista de reservas y notifica al adaptador
                        listaReservas.clear();
                        listaReservas.addAll(response.body());
                        reservaAdapter.notifyDataSetChanged();
                        Log.d("DetallesReservaActivity", "Lista de reservas actualizada");
                    } else {
                        // Maneja el error de la llamada
                        if (response.code() == 401) {
                            // Token expirado, intenta renovar el token y vuelve a realizar la llamada
                            renovarToken();
                        } else {
                            mostrarMensaje("Error al obtener la lista de reservas: " + response.code());
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("Error", "Error body: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ReservaInfo>> call, Throwable t) {
                    // Maneja el error de la red
                    mostrarMensaje("Error de red al obtener la lista de reservas: " + t.getMessage());
                }
            });
        } else {
            // Maneja el caso en que userId o authToken no son válidos
            mostrarMensaje("ID de usuario o token no válidos");
        }
    }

    private void renovarToken() {
        // Implementa la lógica para renovar el token si es necesario
    }

    private void mostrarMensaje(String mensaje) {
        // Muestra el mensaje (puedes utilizar un Toast o un TextView según tu preferencia)
        Log.e("Error", mensaje);
    }
}
