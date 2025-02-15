import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClienteService implements Runnable {
    private Socket socket;
    private PrintWriter saida;
    private BufferedReader entrada;
    private String nomeUsuario;
    private String corUsuario;
    private static Map<String, PrintWriter> clientes = new HashMap<>();
    private static Map<String, String> coresUsuarios = new HashMap<>();

    private static final String[] CORES = {
            "\u001B[31m", // vermelho
            "\u001B[32m", // verde
            "\u001B[33m", // amarelo
            "\u001B[34m", // azul
            "\u001B[35m", // roxo
            "\u001B[36m"  // ciano
    };
    private static final String RESET = "\u001B[0m";

    public ClienteService(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                saida.println("Digite seu username:");
                nomeUsuario = entrada.readLine();

                if (nomeUsuario == null || nomeUsuario.trim().isEmpty() || clientes.containsKey(nomeUsuario)) {
                    saida.println("Nome em uso. Escolha outro.");
                } else {
                    synchronized (clientes) {
                        clientes.put(nomeUsuario, saida);
                        corUsuario = CORES[new Random().nextInt(CORES.length)];
                        if (nomeUsuario.equals("mume")) { corUsuario = "\u001B[32m"; }
                        coresUsuarios.put(nomeUsuario, corUsuario);
                    }
                    break;
                }
            }

            broadcastInOut(corUsuario + nomeUsuario + " entrou no chat!" + RESET);

            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                if (mensagem.startsWith("/")) {
                    processarComando(mensagem);
                    if (socket.isClosed()) break;
                } else {
                    broadcast(corUsuario + nomeUsuario + RESET + ": " + mensagem, nomeUsuario);
                }
            }
        } catch (IOException e) {
            if (!(e instanceof SocketException && "Socket closed".equals(e.getMessage()))) {
                e.printStackTrace();
            }
        } finally {
            desconectarUsuario(corUsuario, nomeUsuario);
        }
    }




    private void processarComando(String comando) {
        if (comando.equalsIgnoreCase("/sair")) {
            sair();
        } else if (comando.startsWith("/privado ")) {
            privado(comando);
        } else if (comando.startsWith("/help")) {
            ajuda();
        } else {
            saida.println("Comando desconhecido. Use '/help' para saber mais sobres os comandos do chat.");
        }
    }



    private void sair() {
        saida.println(corUsuario + nomeUsuario + " saiu do chat." + RESET);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ajuda() {
        saida.println("Oi, " + corUsuario + nomeUsuario + RESET + "! Abaixo tem alguns comandos que podem te ajudar:");
        saida.println("");
        saida.println("- '/privado <username> <mensagem>' envia uma mensagem privada que apenas o user recebe pode ler!");
        saida.println("- '/sair' desconecta do chat!");
    }

    private void privado(String comando){
        String[] partes = comando.split(" ", 3);
        if (partes.length < 3) {
            saida.println("Uso correto: /privado <username> <mensagem>");
            return;
        }

        String destinatario = partes[1];
        String mensagemPrivada = partes[2];


        PrintWriter destinoSaida = clientes.get(destinatario);
        if (destinoSaida != null) {
            String corDestinatario = coresUsuarios.get(destinatario);

            saida.println("[Privado para " + corDestinatario + destinatario + RESET + "] " + mensagemPrivada);
            destinoSaida.println("[Privado] " + corUsuario + nomeUsuario + RESET + ": " + mensagemPrivada);
        } else {
            saida.println("Username indefinido.");
        }

    }

    public void desconectarUsuario(String corUsuario, String nomeUsuario) {
        try {
            synchronized (clientes) {
                clientes.remove(nomeUsuario);
                coresUsuarios.remove(nomeUsuario);
            }
            broadcastInOut(corUsuario + nomeUsuario + " saiu do chat." + RESET);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String mensagem, String nomeUsuario) {
        synchronized (clientes) {
            PrintWriter remetente = clientes.get(nomeUsuario);
            for (PrintWriter clienteSaida : clientes.values()) {
                if(clienteSaida != remetente) {
                    clienteSaida.println(mensagem);
                }
            }
        }
    }

    public void broadcastInOut(String mensagem) {
        synchronized (clientes) {
            for (PrintWriter clienteSaida : clientes.values()) {
                clienteSaida.println(mensagem);
            }
        }
    }


}