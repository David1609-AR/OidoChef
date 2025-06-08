// Paquete al que pertenece la clase
package com.example.appoidochef.data.websocket;

// Importación de clases necesarias para crear un servicio en Android
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

// Importaciones para trabajar con WebSockets usando la librería Java-WebSocket
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

// Servicio de Android que mantiene abierta una conexión WebSocket en segundo plano
public class WebSocketService extends Service {

    // Cliente WebSocket que gestiona la conexión
    private WebSocketClient webSocketClient;

    // Método llamado cuando se inicia el servicio
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        conectarWebSocket(); // Intenta conectarse al WebSocket
        return START_STICKY; // El sistema intenta reiniciar el servicio si se detiene
    }

    // Método que configura y conecta el WebSocket
    private void conectarWebSocket() {
        try {
            // Dirección del servidor WebSocket (ajustar según la IP del servidor)
            URI uri = new URI("ws://192.168.1.16:4567/ws/pedidos");

            // Instancia del WebSocketClient con los callbacks necesarios
            webSocketClient = new WebSocketClient(uri) {

                // Llamado cuando se abre la conexión
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("🟢 WebSocket abierto desde servicio");
                }

                // Llamado cuando se recibe un mensaje del servidor
                @Override
                public void onMessage(String message) {
                    System.out.println("Mensaje recibido desde WebSocketService: " + message);

                    // Crea un Intent para notificar al receptor (BroadcastReceiver) que hay un mensaje nuevo
                    Intent intent = new Intent("MENSAJE_WEBSOCKET");
                    intent.putExtra("mensaje", message);

                    // Si Android 13+ se recomienda marcar explícitamente el broadcast como dinámico
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        sendBroadcast(intent, null); // Para receptores registrados dinámicamente
                    } else {
                        sendBroadcast(intent); // En versiones anteriores basta con esto
                    }
                }

                // Llamado cuando se cierra la conexión
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("🔴 WebSocket cerrado: " + reason);
                }

                // Llamado cuando ocurre un error en la conexión
                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace(); // Muestra el error en consola
                }
            };

            // Inicia la conexión con el servidor WebSocket
            webSocketClient.connect();

        } catch (Exception e) {
            e.printStackTrace(); // Captura y muestra cualquier error en la conexión
        }
    }

    // Método que se llama cuando el servicio es destruido
    @Override
    public void onDestroy() {
        // Cierra el WebSocket si está conectado
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        super.onDestroy(); // Llama al método base
    }

    // Este servicio no permite binding, por eso retorna null
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
