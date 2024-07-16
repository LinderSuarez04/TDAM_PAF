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

import com.example.avn.Model.Producto;
import com.example.avn.interfas.API;
import com.example.avn.interfas.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiendaFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Producto> listaProductos = new ArrayList<>();
    private ProductoAdapter productoAdapter;

    public TiendaFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tienda_fragmen, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializa el adaptador del RecyclerView
        productoAdapter = new ProductoAdapter(listaProductos, new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto) {
                // Maneja el clic en un elemento de la lista
                seleccionarUnProducto(producto);
            }
        });
        recyclerView.setAdapter(productoAdapter);

        // Obtiene el token almacenado en SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("Bearer ", Context.MODE_PRIVATE);
        String authToken = preferences.getString("AUTH_TOKEN", "");

        // Verifica que el authToken no sea nulo o vacío antes de realizar la llamada a la API
        if (!TextUtils.isEmpty(authToken)) {
            obtenerProductos(authToken);
        } else {
            // Maneja el caso en que el token no está disponible (puede redirigir a la pantalla de inicio de sesión)
            mostrarMensaje("Token de autenticación no válido. Inicie sesión nuevamente.");
            // Aquí puedes implementar la lógica para redirigir a la pantalla de inicio de sesión
        }

        return rootView;
    }

    private void obtenerProductos(String authToken) {
        Log.d("TiendaFragment", "Iniciando obtención de canchas");

        // Imprime el token para verificar su valor
        Log.d("TiendaFragment", "Token: " + authToken);

        // Crea una instancia de la interfaz Retrofit
        API api = ApiClient.getApiClient().create(API.class);

        // Hace la llamada a la API con el token en la cabecera
        Call<List<Producto>> call = api.obtenerProductos("Bearer " + authToken);

        // Maneja la respuesta de la llamada
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                Log.d("TiendaFragment", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    // Actualiza la lista de canchas y notifica al adaptador
                    listaProductos.clear();
                    listaProductos.addAll(response.body());
                    productoAdapter.notifyDataSetChanged();
                } else {
                    // Maneja el error de la llamada
                    if (response.code() == 401) {
                        // Token expirado, intenta renovar el token y vuelve a realizar la llamada
                        renovarToken();
                    } else {
                        mostrarMensaje("Error al obtener los productos: " + response.code());
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
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                // Maneja el error de la red
                mostrarMensaje("Error de red al obtener las canchas: " + t.getMessage());
            }
        });
    }

    private void renovarToken() {
        // Aquí puedes implementar la lógica para renovar el token
    }

    private void seleccionarUnProducto(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("¿Deseas añadir este producto al carrito?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lógica para añadir el producto al carrito
                        agregarAlCarrito(producto);
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

    private void agregarAlCarrito(Producto producto) {
        // Verifica si el carrito ya contiene el producto
        if (carritoContieneProducto(producto)) {
            showToast("El producto ya está en el carrito");
        } else {
            // Agrega el producto al carrito
            listaProductos.add(producto);
            showToast("Producto añadido al carrito correctamente");

            // Notifica al adaptador que los datos han cambiado
            productoAdapter.notifyDataSetChanged();
        }
    }
    private boolean carritoContieneProducto(Producto producto) {
        // Verifica si el carrito ya contiene el producto
        return listaProductos.contains(producto);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void mostrarMensaje(String mensaje) {
        // Muestra el mensaje (puedes utilizar un Toast o un TextView según tu preferencia)
        Log.e("Error", mensaje);
    }
}
