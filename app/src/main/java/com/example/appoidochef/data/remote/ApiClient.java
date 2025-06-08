// Paquete donde se encuentra esta clase
package com.example.appoidochef.data.remote;

// Importación de clases de Retrofit necesarias para construir el cliente HTTP
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Clase que configura y proporciona una instancia de Retrofit para consumir la API
public class ApiClient {

    // Dirección base del servidor API. Puedes cambiarla según el entorno.
    // private static final String BASE_URL = "http://192.168.40.132:4567"; // IP para pruebas con el móvil

    private static final String BASE_URL = "http://192.168.1.16:4567"; // IP para servidor local (por ejemplo, PC)

    // Instancia singleton de Retrofit
    private static Retrofit retrofit = null;

    // Método para obtener una instancia del servicio de API definido en ApiService.java
    public static ApiService getApiService() {
        // Si no se ha creado aún la instancia de Retrofit, la crea con la base URL y conversor JSON
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Define la URL base para todas las peticiones
                    .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para deserializar JSON
                    .build();
        }
        return retrofit.create(ApiService.class); // Crea la implementación del ApiService
    }

    // Método alternativo para obtener el cliente Retrofit directamente
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Otro método redundante que simplemente devuelve el cliente Retrofit
    public static Retrofit getRetrofit() {
        return getClient(); // o se podría simplificar a `return retrofit;`
    }
}
