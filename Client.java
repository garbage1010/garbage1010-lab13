import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructs client and connects to the given host/port.
     */
    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /** Returns the underlying socket. */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Sends the handshake passcode
     */
    public void handshake() {
        out.println("12345");
        out.flush();
    }

    /**
     * Sends a factorization request for the given number string
     * and returns the server's response line.
     */
    public String request(String number) throws IOException {
        out.println(number);
        out.flush();
        return in.readLine();
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}