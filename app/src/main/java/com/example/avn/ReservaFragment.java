package com.example.avn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avn.Model.Cancha;
import com.example.avn.interfas.API;
import com.example.avn.interfas.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservaFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Cancha> listaCanchas = new ArrayList<>();
    private CanchaAdapter canchaAdapter;

    public ReservaFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reserva_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializa el adaptador del RecyclerView
        canchaAdapter = new CanchaAdapter(listaCanchas, new CanchaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cancha cancha) {
                // Maneja el clic en un elemento de la lista
                abrirOtraActividad(cancha);
            }
        });
        recyclerView.setAdapter(canchaAdapter);

        // Obtiene el token almacenado en SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("Bearer ", Context.MODE_PRIVATE);
        String authToken = preferences.getString("AUTH_TOKEN", "Bearer ");

        // Verifica que el authToken no sea nulo o vacío antes de realizar la llamada a la API
        if (!TextUtils.isEmpty(authToken)) {
            obtenerCanchas(authToken);
        } else {
            // Maneja el caso en que el token no está disponible (puede redirigir a la pantalla de inicio de sesión)
            mostrarMensaje("Token de autenticación no válido. Inicie sesión nuevamente.");
            // Aquí puedes implementar la lógica para redirigir a la pantalla de inicio de sesión
        }

        return rootView;
    }

    private void obtenerCanchas(String authToken) {
        Log.d("ReservaFragment", "Iniciando obtención de canchas");

        // Imprime el token para verificar su valor
        Log.d("ReservaFragment", "Token: " + authToken);

        // Crea una instancia de la interfaz Retrofit
        API api = ApiClient.getApiClient().create(API.class);

        // Hace la llamada a la API con el token en la cabecera
        Call<List<Cancha>> call = api.obtenerCanchas("Bearer " + authToken);

        // Maneja la respuesta de la llamada
        call.enqueue(new Callback<List<Cancha>>() {
            @Override
            public void onResponse(Call<List<Cancha>> call, Response<List<Cancha>> response) {
                Log.d("ReservaFragment", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    // Actualiza la lista de canchas y notifica al adaptador
                    listaCanchas.clear();
                    listaCanchas.addAll(response.body());
                    canchaAdapter.notifyDataSetChanged();
                } else {
                    // Maneja el error de la llamada
                    if (response.code() == 401) {
                        // Token expirado, intenta renovar el token y vuelve a realizar la llamada
                        renovarToken();
                    } else {
                        mostrarMensaje("Error al obtener las canchas: " + response.code());
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("Error", "Error body: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cancha>> call, Throwable t) {
                // Maneja el error de la red
                mostrarMensaje("Error de red al obtener las canchas: " + t.getMessage());
            }
        });
    }

    private void renovarToken() {
        // Aquí puedes implementar la lógica para renovar el token
    }

    private void abrirOtraActividad(Cancha cancha) {
        int canchaId = cancha.getId();
        // Aquí debes implementar el código para abrir otra actividad.
        // Puedes usar un Intent para esto. Por ejemplo:
        Intent intent = new Intent(requireContext(), ReservaActivity.class);
        intent.putExtra("nombreCancha", cancha.getNombre());
        intent.putExtra("idCancha", canchaId);
        startActivity(intent);
    }
    private void mostrarMensaje(String mensaje) {
        // Muestra el mensaje (puedes utilizar un Toast o un TextView según tu preferencia)
        Log.e("Error", mensaje);
    }
}
