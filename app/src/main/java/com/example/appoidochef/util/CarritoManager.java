package com.example.appoidochef.util;

import com.example.appoidochef.data.model.ItemPedidoAPI;
import com.example.appoidochef.data.model.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoManager {

    // Mapa que guarda un carrito por cada mesa
    private static final Map<Integer, List<ItemPedidoAPI>> carritoMap = new HashMap<>();

    // Agregar producto al carrito de una mesa
    public static void agregarProducto(int mesaId, Producto producto) {
        List<ItemPedidoAPI> carrito = carritoMap.getOrDefault(mesaId, new ArrayList<>());

        // Ver si el producto ya está en el carrito
        for (ItemPedidoAPI item : carrito) {
            if (item.getProductoId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                carritoMap.put(mesaId, carrito);
                return;
            }
        }

        // Si no existe, lo agregamos como nuevo
        carrito.add(new ItemPedidoAPI(producto, 1));
        carritoMap.put(mesaId, carrito);
    }

    // Obtener carrito de una mesa
    public static List<ItemPedidoAPI> getCarrito(int mesaId) {
        return carritoMap.getOrDefault(mesaId, new ArrayList<>());
    }

    // Eliminar un producto del carrito
    public static void eliminarProducto(int mesaId, int productoId) {
        List<ItemPedidoAPI> carrito = carritoMap.get(mesaId);
        if (carrito != null) {
            carrito.removeIf(item -> item.getProductoId() == productoId);
        }
    }

    // Limpiar carrito de una mesa
    public static void limpiarCarrito(int mesaId) {
        carritoMap.remove(mesaId);
    }
}
