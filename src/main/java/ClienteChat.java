import org.fusesource.jansi.AnsiConsole;

import java.io.*;
import java.net.*;

public class ClienteChat {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        String servidor = "localhost";
        int porta = 12345;

        try (Socket socket = new Socket(servidor, porta);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            AnsiConsole.out().println(entrada.readLine()); // mensagem "Digite seu nome de usuário:"
            String nomeUsuario = teclado.readLine();
            saida.println(nomeUsuario);

            while (true) {
                String resposta = entrada.readLine();
                if (resposta.startsWith("Nome em uso!")) {
                    AnsiConsole.out().println(resposta);
                    nomeUsuario = teclado.readLine();
                    saida.println(nomeUsuario);
                } else {
                    AnsiConsole.out().println(resposta);
                    break;
                }
            }

            new Thread(() -> {
                try {
                    String resposta;
                    while ((resposta = entrada.readLine()) != null) {
                        AnsiConsole.out().println(resposta);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String mensagem;
            while ((mensagem = teclado.readLine()) != null) {
                saida.println(mensagem);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}
