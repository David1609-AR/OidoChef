// Paquete donde se encuentra esta clase
package com.example.appoidochef.ui.main;

// Importación de clases necesarias para funcionalidades básicas de Android
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Importación de clase base para actividades con compatibilidad extendida
import androidx.appcompat.app.AppCompatActivity;

// Importación de recursos (layout y clases del proyecto)
import com.example.appoidochef.R;
import com.example.appoidochef.data.model.LoginRequest;
import com.example.appoidochef.data.model.LoginResponse;
import com.example.appoidochef.data.remote.ApiClient;
import com.example.appoidochef.data.remote.ApiService;
import com.example.appoidochef.ui.main.MainActivity;

// Importación de clases para manejar llamadas HTTP asíncronas con Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Clase que maneja la lógica de la pantalla de login
public class LoginActivity extends AppCompatActivity {

    // Declaración de campos de entrada para usuario y contraseña
    private EditText etUsuario, etContrasena;

    // Botón de login
    private Button btnLogin;

    // Preferencias compartidas para guardar información persistente localmente
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs"; // Nombre del archivo de preferencias

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método de la superclase
        setContentView(R.layout.activity_login); // Establece el diseño visual de esta actividad

        // Enlaza los campos de texto y el botón con los elementos visuales del layout
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);

        // Inicializa las preferencias compartidas en modo privado
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Configura la acción al pulsar el botón de login
        btnLogin.setOnClickListener(v -> {
            // Obtiene el texto ingresado en los campos
            String username = etUsuario.getText().toString().trim();
            String password = etContrasena.getText().toString().trim();

            // Verifica que ambos campos estén completos
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crea el objeto de solicitud con los datos ingresados
            LoginRequest request = new LoginRequest(username, password);

            // Realiza la llamada a la API para intentar hacer login
            ApiClient.getApiService().login(request)
                    .enqueue(new Callback<LoginResponse>() {

                        // Método que se ejecuta si se obtiene una respuesta del servidor
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            // Verifica que la respuesta fue exitosa y que tiene cuerpo
                            if (response.isSuccessful() && response.body() != null) {
                                boolean success = response.body().isSuccess();
                                if (success) {
                                    // Si el login fue exitoso, muestra mensaje y va a la pantalla principal
                                    Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish(); // Finaliza esta actividad para no poder volver atrás con el botón
                                } else {
                                    // Si el login falla, informa al usuario
                                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Si la respuesta no fue exitosa o vino vacía
                                Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Método que se ejecuta si ocurre un error de red o conexión
                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace(); // Imprime el error en la consola (Logcat)
                            Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
