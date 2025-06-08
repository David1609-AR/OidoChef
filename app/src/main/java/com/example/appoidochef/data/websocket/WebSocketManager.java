package com.example.appoidochef.data.websocket;

import android.content.Context;

import com.example.appoidochef.data.model.PedidoAPI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WebSocketManager {

    private static WebSocketManager instance;
    private WebSocketClient webSocketClient;
    private final String SERVER_URI = "ws://192.168.1.16:4567/ws/pedidos";

    private final List<MessageListener> listeners = new ArrayList<>();
    private Context context;

    private WebSocketManager() {}

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public void connect() {
        if (webSocketClient != null && webSocketClient.isOpen()) return;

        URI uri = URI.create(SERVER_URI);
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("🟢 WebSocket conectado: " + getURI());
            }

            @Override
            public void onMessage(String message) {
                System.out.println("📩 Mensaje recibido: " + message);
                for (MessageListener listener : listeners) {
                    listener.onMessage(message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("🔴 WebSocket cerrado: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("❌ Error WebSocket: " + ex.getMessage());
            }
        };
        webSocketClient.connect();
    }

    public void sendOrderWhenReady(PedidoAPI pedido) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            String json = pedido.toJson();
            webSocketClient.send(json);
        } else {
            System.out.println("⚠ WebSocket no conectado");
        }
    }

    public boolean isConnected() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public interface MessageListener {
        void onMessage(String message);
    }
}