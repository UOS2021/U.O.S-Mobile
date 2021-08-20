package com.uof.uof_mobile.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketManager {
    private Socket socket;
    private SocketAddress socketAddress;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public SocketManager() {

    }

    public SocketManager(String targetIp, int targetPort) {
        socket = new Socket();
        socketAddress = new InetSocketAddress(targetIp, targetPort);
    }

    // Set socket information
    public boolean setSocket(String targetIp, int targetPort) {
        if (socket != null && isSocketConnected())
            return false;

        socket = new Socket();
        socketAddress = new InetSocketAddress(targetIp, targetPort);

        return true;
    }

    // Return socket connected state
    public boolean isSocketConnected() {
        return (socket.isConnected() && !socket.isClosed());
    }

    // Connect socket
    public boolean connect(int timeoutMills) {
        if (isSocketConnected())
            return false;

        try {
            socket.connect(socketAddress, timeoutMills);
            if (isSocketConnected()) {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC-KR"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSocketConnected();
    }

    // Disconnect socket
    public boolean disconnect() {
        if (!isSocketConnected())
            return true;

        try {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSocketConnected();
    }

    // Send data to connected socket
    public boolean send(String sendData) {
        if (!isSocketConnected())
            return false;

        printWriter.println(sendData);
        return true;
    }

    // Receive data from connected socket
    public String recv() {
        if (!isSocketConnected())
            return null;

        try {
            return bufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
