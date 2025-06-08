package com.example.appoidochef.data.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketService extends Service {

    private WebSocketClient webSocketClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        conectarWebSocket();
        return START_STICKY;
    }

    private void conectarWebSocket() {
        try {
            URI uri = new URI("ws://192.168.1.16:4567/ws/pedidos");

            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("🟢 WebSocket abierto desde servicio");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Mensaje recibido desde WebSocketService: " + message);

                    Intent intent = new Intent("MENSAJE_WEBSOCKET");
                    intent.putExtra("mensaje", message);

                    // ✅ Android 13+: marcar el broadcast como not exported si es dinámico
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        sendBroadcast(intent, null); // receptor dinámico → seguro
                    } else {
                        sendBroadcast(intent); // versiones anteriores
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("🔴 WebSocket cerrado: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            webSocketClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // no es un servicio enlazado
    }
}
