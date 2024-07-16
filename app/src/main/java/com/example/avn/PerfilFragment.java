package com.example.avn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class PerfilFragment extends Fragment {

    private String username;
    private String jsonString;

    public PerfilFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.perfil_fragmen, container, false);

        // Obtener referencia al botón
        Button buttonMisReservas = rootView.findViewById(R.id.button);

        // Agregar OnClickListener al botón
        buttonMisReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acción a realizar cuando se hace clic en el botón
                abrirOtraActivity();
            }
        });

        // Resto del código...

        SharedPreferences preferences = requireContext().getSharedPreferences("Bearer ", Context.MODE_PRIVATE);
        username = preferences.getString("USERNAME", "");

        TextView textViewNombreUsuario = rootView.findViewById(R.id.textViewNombreUsuario);
        textViewNombreUsuario.setText(username);

        try {
            jsonString = loadJSONFromAsset(requireContext(), "img.json");
            JSONObject jsonObject = new JSONObject(jsonString);
            String imageUrl = jsonObject.optString("imagen_url");

            ImageView imageViewPerfil = rootView.findViewById(R.id.imageViewPerfil);
            Picasso.get().load(imageUrl).into(imageViewPerfil);

        } catch (IOException | JSONException e) {
            // Manejar la excepción de manera adecuada, por ejemplo, mostrar una imagen de marcador de posición.
            e.printStackTrace();
        }

        return rootView;
    }

    // Método para abrir otra Activity
    private void abrirOtraActivity() {
        Intent intent = new Intent(getActivity(), DetallesReservaActivity.class);
        startActivity(intent);
    }

    private String loadJSONFromAsset(Context context, String fileName) throws IOException {
        try (InputStream inputStream = context.getAssets().open(fileName)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            return new String(buffer, "UTF-8");
        }
    }
}
