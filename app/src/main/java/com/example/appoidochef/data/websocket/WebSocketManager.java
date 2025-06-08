package com.example.appoidochef.data.websocket;

// Librerías necesarias para WebSocket y manejo de URLs
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

// Clase que maneja la conexión WebSocket de forma centralizada (Singleton)
public class WebSocketManager {

    private static WebSocketManager instance; // Instancia única de la clase
    private WebSocketClient webSocketClient; // Cliente WebSocket

    // URL del servidor WebSocket (se puede cambiar si cambia la IP/puerto)
    private static final String SERVER_URL = "ws://192.168.1.16:4567/ws/pedidos";

    // Constructor privado para que solo pueda instanciarse dentro de la clase (patrón Singleton)
    private WebSocketManager() {
        try {
            URI uri = new URI(SERVER_URL); // Crea URI con la dirección del WebSocket

            // Crea el cliente WebSocket con métodos sobrescritos para eventos
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("🟢 WebSocket abierto (WebSocketManager)");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("📨 Mensaje recibido en WebSocketManager: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("🔴 WebSocket cerrado (WebSocketManager): " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace(); // Muestra el error en consola
                }
            };
        } catch (Exception e) {
            e.printStackTrace(); // Maneja errores de URI
        }
    }

    // Devuelve la instancia única de WebSocketManager
    public static WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager(); // Si no existe, la crea
        }
        return instance;
    }

    // Conecta el cliente al servidor WebSocket si no está ya conectado
    public void connect() {
        if (webSocketClient != null && !webSocketClient.isOpen()) {
            webSocketClient.connect(); // Inicia conexión si aún no está abierta
        }
    }

    // Envía un mensaje al servidor WebSocket si está conectado
    public void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message); // Envia mensaje por el WebSocket
        }
    }

    // Cierra la conexión WebSocket si está activa
    public void close() {
        if (webSocketClient != null) {
            webSocketClient.close(); // Cierra el WebSocket
        }
    }
}
