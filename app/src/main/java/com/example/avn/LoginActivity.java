package com.example.avn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.avn.interfas.API;
import com.example.avn.token.AuthRequest;
import com.example.avn.token.AuthResponse;
import com.example.avn.token.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            obtenerTokenUsuario(username, password);
        });
    }

    private void obtenerTokenUsuario(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://TDAMGrupo02.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API alumnoApi = retrofit.create(API.class);

        AuthRequest authRequest = new AuthRequest(username, password);

        Call<AuthResponse> call = alumnoApi.obtenerToken(authRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        String authToken = authResponse.getAccessToken();
                        Usuario usuario = authResponse.getUser();
                        guardarTokenEnSharedPreferences(username, authToken);
                        guardarUsuarioEnSharedPreferences(usuario);
                        irAMainActivity(authToken);
                    } else {
                        mostrarMensaje("Error: Respuesta de token nula");
                    }
                } else {
                    mostrarMensaje("Error al obtener el token: " + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Error", "Error body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                mostrarMensaje("Error de red al obtener el token: " + t.getMessage());
            }
        });
    }
    private void guardarUsuarioEnSharedPreferences(Usuario usuario) {
        SharedPreferences preferences = getSharedPreferences("Bearer ", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("USER_ID", usuario.getId());
        editor.putString("USERNAME", usuario.getUsername());
        // Agrega más campos según sea necesario
        editor.apply();
    }
    private void guardarTokenEnSharedPreferences(String username, String authToken) {
        SharedPreferences preferences = getSharedPreferences("Bearer ", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME", username);
        editor.putString("AUTH_TOKEN", authToken);
        editor.apply();
    }

    private void irAMainActivity(String authToken) {
        Intent intent = new Intent(LoginActivity.this, Activity_usuario.class);
        intent.putExtra("AUTH_TOKEN", authToken);
        startActivity(intent);
        finish();
    }

    private void mostrarMensaje(String mensaje) {
        Log.e("Error", mensaje);
    }
}
