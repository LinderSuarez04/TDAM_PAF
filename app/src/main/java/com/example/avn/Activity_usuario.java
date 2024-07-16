package com.example.avn;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_usuario extends AppCompatActivity {

    // Listener para manejar la selección de elementos en el BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            // Utilizando if-else para evitar el error "Constant expression required"
            if (item.getItemId() == R.id.menu_perfil) {
                selectedFragment = new PerfilFragment(); // Reemplaza PerfilFragment con el nombre de tu fragmento
            } else if (item.getItemId() == R.id.menu_reserva) {
                selectedFragment = new ReservaFragment(); // Reemplaza ReservaFragment con el nombre de tu fragmento
            } else if (item.getItemId() == R.id.menu_tienda) {
                selectedFragment = new TiendaFragment(); // Reemplaza TiendaFragment con el nombre de tu fragmento
            } else if (item.getItemId() == R.id.menu_carrito) {
                selectedFragment = new CarritoFragment(); // Reemplaza CarritoFragment con el nombre de tu fragmento
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        }
    };

    // Función para cargar un fragmento en el contenedor
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Cargar el fragmento inicial
        loadFragment(new PerfilFragment()); // Reemplaza PerfilFragment con el fragmento que deseas mostrar inicialmente
    }
}
