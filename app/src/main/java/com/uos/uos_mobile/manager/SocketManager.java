package com.uos.uos_mobile.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 장바구니를 관리하는 Manager 클래스.<br><br>
 * <p>
 * BasketMangaer는 주문 Activity(ex. OrderingActivity, MovieOrderingActivity) 실행 시 생성되며 주문
 * Activity에서 선택한 상품들을 보관하는 장바구니 역할을 합니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 * @deprecated 소켓 통신방식에서 Http 통신방식으로 변경됨에 따라 현재 사용되지 않습니다.
 */
public class SocketManager {

    /**
     * 소켓 통신 시 사용되는 Socket.
     */
    private Socket socket;

    /**
     * 통신 대상에 대한 정보를 가지고 있는 SocketAddress.
     */
    private SocketAddress socketAddress;

    /**
     * 데이터 송신 시 사용되는 PrintWriter.
     */
    private PrintWriter printWriter;

    /**
     * 데이터 수신 시 사용되는 BufferedReader.
     */
    private BufferedReader bufferedReader;

    /**
     * 기본 생성자입니다.
     */
    public SocketManager() {

    }

    /**
     * 명시적 생성자입니다. 매개변수로 연결할 주소 및 포트를 전달받습니다.
     *
     * @param targetIp 연결할 주소.
     * @param targetPort 연결할 포트.
     */
    public SocketManager(String targetIp, int targetPort) {
        socket = new Socket();
        socketAddress = new InetSocketAddress(targetIp, targetPort);
    }

    /**
     * 소켓 정보를 설정합니다. 기본 생성자 호출 후 소켓 연결 시, 또는 새로운 소켓 통신 시에 사용됩니다.
     * 
     * @param targetIp 연결할 주소.
     * @param targetPort 연결할 포트.
     * @return boolean 기존에 사용중이던 소켓의 연결이 종료되지 않았거나 해제되지 않았을 경우 false 반환.
     */
    public boolean setSocket(String targetIp, int targetPort) {
        if (socket != null && isSocketConnected())
            return false;

        socket = new Socket();
        socketAddress = new InetSocketAddress(targetIp, targetPort);

        return true;
    }

    /**
     * 소켓의 연결 상태를 반환합니다.
     *
     * @return boolean 소켓 연결 상태.
     */
    public boolean isSocketConnected() {
        return (socket.isConnected() && !socket.isClosed());
    }

    /**
     * 소켓을 설정된 주소와 포트로 연결합니다. 매개변수로는 연결 시 timeout할 시간을 전달받습니다.
     *
     * @param timeoutMills 연결 실패로 처리할 timeout 시간.
     * @return boolean 이미 소켓이 연결되어있거나 연결 실패 시 false 반환.
     */
    public boolean connect(int timeoutMills) {
        if (isSocketConnected())
            return false;

        try {
            socket.connect(socketAddress, timeoutMills);

            if (isSocketConnected()) {

                /* 소켓이 성공적으로 연결되었을 경우 */

                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSocketConnected();
    }

    /**
     * 소켓 연결을 해제합니다.
     * @return boolean 소켓 연결 해제 상태.
     */
    public boolean disconnect() {
        if (!isSocketConnected())
            return true;

        try {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSocketConnected();
    }

    /**
     * 매개변수로 전달된 String 데이터를 연결된 소켓으로 전송합니다.
     *
     * @param sendData 전송할 데이터.
     * @return boolean 소켓이 연결되어있지 않을 경우 false 반환.
     */
    public boolean send(String sendData) {
        if (!isSocketConnected())
            return false;

        printWriter.println(sendData);
        return true;
    }

    /**
     * 연결된 소켓으로부터 데이터를 수신받습니다.
     *
     * @return String 수신된 데이터를 반환. 수신 실패 시 null 반환.
     */
    public String recv() {
        if (!isSocketConnected())
            return null;

        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
