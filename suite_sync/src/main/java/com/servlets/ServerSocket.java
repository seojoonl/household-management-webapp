package com.servlets;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws")
public class ServerSocket {

    private static final Set<Session> sessions = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("New session opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Session closed: " + session.getId());
    }

    public static void sendUpdateToAllClients(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                	System.out.println("Sending Message...");
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    System.err.println("Error sending update to client: " + e.getMessage());
                }
            }
        }
    }
}