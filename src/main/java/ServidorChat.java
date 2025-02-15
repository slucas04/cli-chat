import java.io.*;
import java.net.*;
import org.fusesource.jansi.AnsiConsole;

public class ServidorChat {

    private static final int PORTA = 12345;


    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        System.out.println("Servidor iniciado na porta " + PORTA);

        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Novo cliente conectado: " + cliente.getInetAddress());

                new Thread(new ClienteService(cliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
