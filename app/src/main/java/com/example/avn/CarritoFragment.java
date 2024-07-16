package com.example.avn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avn.Model.Carrito;
import com.example.avn.interfas.API;
import com.example.avn.interfas.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Carrito> listaCarritos = new ArrayList<>();
    private CarritoAdapter carritoAdapter;

    private String authToken;

    public CarritoFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.carrito_fragmen, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializa el adaptador del RecyclerView
        carritoAdapter = new CarritoAdapter(listaCarritos, new CarritoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Carrito carrito) {
                // Maneja el clic en un elemento de la lista
                seleccionarUnCarrito(carrito);
            }
        });
        recyclerView.setAdapter(carritoAdapter);

        // Obtiene el token almacenado en SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("Bearer ", Context.MODE_PRIVATE);
        String authToken = preferences.getString("AUTH_TOKEN", "");

        // Verifica que el authToken no sea nulo o vacío antes de realizar la llamada a la API
        if (!TextUtils.isEmpty(authToken)) {
            obtenerCarritos(authToken);
        } else {
            // Maneja el caso en que el token no está disponible (puede redirigir a la pantalla de inicio de sesión)
            mostrarMensaje("Token de autenticación no válido. Inicie sesión nuevamente.");
            // Aquí puedes implementar la lógica para redirigir a la pantalla de inicio de sesión
        }

        return rootView;
    }

    private void obtenerCarritos(String authToken) {
        Log.d("CarritoFragment", "Iniciando obtención de carrito");

        // Imprime el token para verificar su valor
        Log.d("CarritoFragment", "Token: " + authToken);

        // Crea una instancia de la interfaz Retrofit
        API api = ApiClient.getApiClient().create(API.class);

        // Hace la llamada a la API con el token en la cabecera
        Call<List<Carrito>> call = api.obtenerCarritos("Bearer " + authToken);

        // Maneja la respuesta de la llamada
        call.enqueue(new Callback<List<Carrito>>() {
            @Override
            public void onResponse(Call<List<Carrito>> call, Response<List<Carrito>> response) {
                Log.d("CarritoFragment", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    // Actualiza la lista de canchas y notifica al adaptador
                    listaCarritos.clear();
                    listaCarritos.addAll(response.body());
                    carritoAdapter.notifyDataSetChanged();
                } else {
                    // Maneja el error de la llamada
                    if (response.code() == 401) {
                        // Token expirado, intenta renovar el token y vuelve a realizar la llamada
                        renovarToken();
                    } else {
                        mostrarMensaje("Error al obtener los carritos: " + response.code());
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
            public void onFailure(Call<List<Carrito>> call, Throwable t) {
                // Maneja el error de la red
                mostrarMensaje("Error de red al obtener los carritos: " + t.getMessage());
            }
        });
    }

    private void renovarToken() {
        // Aquí puedes implementar la lógica para renovar el token
    }

    private void seleccionarUnCarrito(Carrito carrito) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("¿Deseas eliminar el carrito seleccionado?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lógica para eliminar el carrito
                        // Supongamos que tienes un método en tu API para eliminar un carrito
                        // Puedes ajustar esto según tu implementación real
                        API api = ApiClient.getApiClient().create(API.class);
                        Call<Void> call = api.eliminarCarrito("Bearer " + authToken, carrito.getId());

                        // Maneja la respuesta de la llamada
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // La eliminación en el servidor fue exitosa
                                    Log.d("CarritoFragment", "Carrito eliminado en el servidor");

                                    // Después de eliminar el carrito, actualiza la lista y notifica al adaptador
                                    listaCarritos.remove(carrito);
                                    carritoAdapter.notifyDataSetChanged();

                                    // Muestra un mensaje indicando que el carrito ha sido eliminado
                                    showToast("Carrito eliminado correctamente");
                                } else {
                                    // Maneja el error de la llamada al servidor
                                    mostrarMensaje("Error al eliminar el carrito en el servidor: " + response.code());
                                    try {
                                        String errorBody = response.errorBody().string();
                                        Log.e("Error", "Error body: " + errorBody);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Maneja el error de la red al intentar eliminar el carrito en el servidor
                                mostrarMensaje("Error de red al intentar eliminar el carrito en el servidor: " + t.getMessage());
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No se realiza ninguna acción al cancelar
                    }
                });

        // Crea el AlertDialog y muestra
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método auxiliar para mostrar un mensaje corto
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void mostrarMensaje(String mensaje) {
        // Muestra el mensaje (puedes utilizar un Toast o un TextView según tu preferencia)
        Log.e("Error", mensaje);
    }
}
