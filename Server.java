import java.net.*;
import java.io.*;
import java.time.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<LocalDateTime> connectedTimes = new ArrayList<>();

    /**
     * Constructs a Server listening on the given port.
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Accepts only `numClients` clients, validates handshake,
     * and processes each factorization request in a new thread.
     */
    public void serve(int numClients) {
        for (int i = 0; i < numClients; i++) {
            try {
                Socket clientSocket = serverSocket.accept();

                // Record connection time immediately (synchronized for thread safety)
                LocalDateTime connectTime = LocalDateTime.now();
                synchronized (connectedTimes) {
                    connectedTimes.add(connectTime);
                }

                // Handle client in its own thread
                Thread t = new Thread(() -> handleClient(clientSocket));
                t.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Handles a single client: handshake check, then factorize */
    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter    out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Handshake
            String passcode = in.readLine();
            if (passcode == null || !passcode.trim().equals("12345")) {
                out.println("couldn't handshake");
                out.flush();
                clientSocket.close();
                return;
            }

            // Process one factorization request
            String numberStr = in.readLine();
            if (numberStr != null) {
                String response = factorize(numberStr.trim());
                out.println(response);
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts all divisors of the number represented by the string.
     * Returns an error message if the number doesn't fit in an int.
     */
    private String factorize(String numberStr) {
        try {
            int n = Integer.parseInt(numberStr); // throws if too large for int
            int count = countDivisors(n);
            return "The number " + n + " has " + count + " factors";
        } catch (Exception e) {
            return "There was an exception on the server";
        }
    }

    /** Counts all positive divisors of n via trial division. */
    private int countDivisors(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        int count = 0;
        for (int i = 1; (long) i * i <= n; i++) {
            if (n % i == 0) {
                count++; // i is a divisor
                if (i != n / i) {
                    count++; // n/i is a distinct divisor
                }
            }
        }
        return count;
    }

    /**
     * Returns a sorted list of LocalDateTime objects representing
     * when each client connected.
     */
    public ArrayList<LocalDateTime> getConnectedTimes() {
        synchronized (connectedTimes) {
            ArrayList<LocalDateTime> sorted = new ArrayList<>(connectedTimes);
            Collections.sort(sorted);
            return sorted;
        }
    }

    /** Closes the server socket. */
    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}