package com.uof.uof_mobile;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UofSocket {
    private Socket socket;
    private SocketAddress socketAddress;
    private OutputStream outputStream;
    private InputStream inputStream;
    private byte[] recvData;

    public UofSocket(){

    }

    public UofSocket(String targetIp, int targetPort) {
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
        return !(socket.isClosed() || (!socket.isClosed() && !socket.isConnected()));
    }

    // Connect socket
    public boolean connect(int timeoutMills) {
        if (isSocketConnected())
            return false;

        try {
            socket.connect(socketAddress, timeoutMills);
            if (isSocketConnected()) {
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
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
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSocketConnected();
    }

    // Send data to connected socket
    public boolean send(String sendData) {
        if(!isSocketConnected())
            return false;

        try {
            outputStream.write(sendData.getBytes("euc-kr"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Receive data from connected socket
    public String recv() {
        if (!isSocketConnected())
            return null;

        try {
            recvData = new byte[Constants.SOCKET_MAX_RECV_SIZE];

            // If received data size is 0 => means that an error occurred in the data transmission/reception process
            if(inputStream.read(recvData) == 0)
                return null;

            return new String(recvData, "euc-kr");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
