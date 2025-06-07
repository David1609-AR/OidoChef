package com.example.appoidochef.data.remote;

import com.example.appoidochef.data.model.LoginRequest;
import com.example.appoidochef.data.model.LoginResponse;
import com.example.appoidochef.data.model.MesaAPI;
import com.example.appoidochef.data.model.PedidoAPI;
import com.example.appoidochef.data.model.Producto;
import com.example.appoidochef.data.model.ProductoAPI;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("mesas")
    Call<List<MesaAPI>> getMesas();

    @GET("pedidos/{mesaId}")
    Call<PedidoAPI> obtenerPedidoActivo(@Path("mesaId") int mesaId);

    @POST("pedidos")
    Call<Void> enviarPedido(@Body PedidoAPI pedido);

    @GET("productos")
    Call<List<Producto>> getProductos();

    @GET("/pedidos")
    Call<List<PedidoAPI>> obtenerPedidos();

    @GET("mesas")
    Call<List<MesaAPI>> obtenerMesas();

    @GET("pedidos/activos")
    Call<List<PedidoAPI>> obtenerPedidosActivos();

    @GET("/pedido/mesa/{mesaId}")
    Call<PedidoAPI> obtenerPedidoPorMesa(@Path("mesaId") int mesaId);

    @POST("/pedido")
    Call<Void> crearPedido(@Body PedidoAPI pedido);

    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}

