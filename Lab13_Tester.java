import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;
import java.io.*;
import java.net.*;
import java.time.*;

public class Lab13_Tester {

  @Test
  public void test1() {
    System.out.println("checking client constructor");

    ServerSocket serverSocket = null;
    try{
        serverSocket = new ServerSocket(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open serverSocket");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client = new Client("localhost", 2021);
        assertEquals("/127.0.0.1", client.getSocket().getLocalAddress().toString());
        assertEquals(2021, client.getSocket().getPort());

        Socket remote = serverSocket.accept();
        client.handshake();
        Thread.sleep(1000); // give it a second to actually flush
        BufferedReader incoming = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        line = incoming.readLine();
        incoming.close();
        remote.close();
        client.disconnect();
    } catch (Exception e){
        e.printStackTrace();
    }

    try{
        serverSocket.close();
    } catch (Exception e){
        e.printStackTrace();
    }

    assertEquals("12345", line);

  }

  @Test
  public void test2() {
    System.out.println("checking client communication with server for small number");

    Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client = new Client("localhost", 2021);

        client.handshake();
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(1);
        Thread.sleep(1000); // give it a second to actually flush

        line = client.request("17");
        Thread.sleep(1000); // give it a second to actually flush

    } catch (Exception e){
        e.printStackTrace();
    }
    server.disconnect();
    assertEquals("The number 17 has 2 factors", line);
  }

  @Test
  public void test3() {
    System.out.println("checking client communication with server for medium number");

    Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client = new Client("localhost", 2021);

        client.handshake();
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(1);
        Thread.sleep(1000); // give it a second to actually flush

        Integer number = 2*3*4*5*6*7*8*9;
        line = client.request(number.toString());
        Thread.sleep(1000); // give it a second to actually flush

    } catch (Exception e){
        e.printStackTrace();
    }
    server.disconnect();
    assertEquals("The number 362880 has 160 factors", line);
  }

  @Test
  public void test4() {
    System.out.println("checking client communication with server for large number");

    Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client = new Client("localhost", 2021);

        client.handshake();
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(1);
        Thread.sleep(1000); // give it a second to actually flush

        line = client.request("2147483642");
        Thread.sleep(1000); // give it a second to actually flush

    } catch (Exception e){
        e.printStackTrace();
    }
    server.disconnect();
    assertEquals("The number 2147483642 has 8 factors", line);
  }

  @Test
  public void test5() {
    System.out.println("checking client communication with server for too large number");

    Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client = new Client("localhost", 2021);

        client.handshake();
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(1);
        Thread.sleep(1000); // give it a second to actually flush

        line = client.request("92147483642");
        Thread.sleep(1000); // give it a second to actually flush

    } catch (Exception e){
        e.printStackTrace();
    }
    server.disconnect();
    assertEquals("There was an exception on the server", line);
  }

  @Test
  public void test6() {
    System.out.println("checking two client communication with server for medium number");

    Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client1 = new Client("localhost", 2021);
        Client client2 = new Client("localhost", 2021);

        client1.handshake();
        client2.handshake();
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(2);
        Thread.sleep(1000); // give it a second to actually flush

        line = client1.request("47483647");
        Thread.sleep(1000); // give it a second to actually flush

        line += "\t" + client2.request("362880");
        Thread.sleep(1000); // give it a second to actually flush

    } catch (Exception e){
        e.printStackTrace();
    }
    server.disconnect();
    assertEquals("The number 47483647 has 4 factors\tThe number 362880 has 160 factors", line);
  }

  @Test
  public void test7() {
    System.out.println("checking eight clients communication with server for large number");

    Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Client client1 = new Client("localhost", 2021);
        Client client2 = new Client("localhost", 2021);
        Client client3 = new Client("localhost", 2021);
        Client client4 = new Client("localhost", 2021);
        Client client5 = new Client("localhost", 2021);
        Client client6 = new Client("localhost", 2021);
        Client client7 = new Client("localhost", 2021);
        Client client8 = new Client("localhost", 2021);

        client1.handshake();
        client2.handshake();
        client3.handshake();
        client4.handshake();
        client5.handshake();
        client6.handshake();
        client7.handshake();
        client8.handshake();
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(8);
        Thread.sleep(1000); // give it a second to actually flush

        line = client8.request("2147483642");
        line += "\t" + client7.request("2147483642");
        line += "\t" + client6.request("2147483642");
        line += "\t" + client5.request("2147483642"); 
        line += "\t" + client4.request("2147483642"); 
        line += "\t" + client3.request("2147483642");
        line += "\t" + client2.request("2147483642");
        line += "\t" + client1.request("2147483642");      

        Thread.sleep(1000); // give it a second to actually flush

        ArrayList<LocalDateTime> times = server.getConnectedTimes();
        Duration duration = Duration.between(times.get(0), times.get(times.size()-1));
        long seconds = duration.toMillis();
        System.out.println("toMillis: " + seconds);
        assertEquals(500, seconds, 500);

    } catch (Exception e){
        e.printStackTrace();
    }
    server.disconnect();
    assertEquals("The number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors\tThe number 2147483642 has 8 factors", line);
  }

  @Test
  public void test8() {
    System.out.println("checking server incorrect handshake recieved");

     Server server = null;
    try{
        server = new Server(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open server");
        e.printStackTrace();
    }

    String line = "An exception happened.";
    try {
        Socket client = new Socket("localhost", 2021);
        PrintWriter out = new PrintWriter(client.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out.println("1234555555");
        out.flush();
        System.out.println("handshake sent");
        Thread.sleep(1000); // give it a second to actually flush

        server.serve(1);
        Thread.sleep(1000); // give it a second to actually flush

        line = in.readLine();
        in.close();
        client.close();
    } catch (Exception e){
        e.printStackTrace();
    }

    try{
        server.disconnect();
    } catch (Exception e){
        e.printStackTrace();
    }

    assertEquals("couldn't handshake", line);

  }
}
