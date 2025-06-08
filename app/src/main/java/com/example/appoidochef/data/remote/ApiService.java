// Paquete donde se encuentra esta interfaz
package com.example.appoidochef.data.remote;

// Importaciones de modelos usados en las peticiones y respuestas HTTP
import com.example.appoidochef.data.model.LoginRequest;
import com.example.appoidochef.data.model.LoginResponse;
import com.example.appoidochef.data.model.MesaAPI;
import com.example.appoidochef.data.model.PedidoAPI;
import com.example.appoidochef.data.model.Producto;
import com.example.appoidochef.data.model.ProductoAPI;

import java.util.List; // Para listas de objetos

// Importación de anotaciones de Retrofit para declarar endpoints
import retrofit2.Call;
import retrofit2.http.*;


// Interfaz de Retrofit que define los endpoints de la API REST
public interface ApiService {

    // Obtiene la lista de mesas (GET /mesas)
    @GET("mesas")
    Call<List<MesaAPI>> getMesas();

    // Obtiene el pedido activo de una mesa específica (GET /pedidos/{mesaId})
    @GET("pedidos/{mesaId}")
    Call<PedidoAPI> obtenerPedidoActivo(@Path("mesaId") int mesaId);

    // Envía un nuevo pedido (POST /pedidos)
    @POST("pedidos")
    Call<Void> enviarPedido(@Body PedidoAPI pedido);

    // Obtiene la lista de productos (GET /productos)
    @GET("productos")
    Call<List<Producto>> getProductos();

    // Obtiene todos los pedidos (GET /pedidos)
    @GET("/pedidos")
    Call<List<PedidoAPI>> obtenerPedidos();

    // Otra forma de obtener mesas, puede ser redundante con `getMesas`
    @GET("mesas")
    Call<List<MesaAPI>> obtenerMesas();

    // Obtiene todos los pedidos activos (GET /pedidos/activos)
    @GET("pedidos/activos")
    Call<List<PedidoAPI>> obtenerPedidosActivos();

    // Obtiene un pedido según el número de mesa (GET /pedido/mesa/{mesaId})
    @GET("/pedido/mesa/{mesaId}")
    Call<PedidoAPI> obtenerPedidoPorMesa(@Path("mesaId") int mesaId);

    // Crea un pedido (POST /pedido)
    @POST("/pedido")
    Call<Void> crearPedido(@Body PedidoAPI pedido);

    // Login de usuario (POST /login)
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
