

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Scanner;

public class Server {

    private Socket socketCliente;
    private ServerSocket serverSocket;

    public boolean connect() {
        try {
            socketCliente = serverSocket.accept();
            return socketCliente.isConnected();
        } catch (IOException erro) {
            System.err.println("Não fez conexão: " + erro.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            Server servidor = new Server();
            servidor.rodarServidor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rodarServidor() throws Exception {
        String textoRecebido = "";
        String textoEnviado = "Olá, Cliente";
        String textoDecifrado;
        String textoCifrado;

        Scanner input = new Scanner(System.in);

        serverSocket = new ServerSocket(9600);
        System.out.println("Servidor iniciado!");

        while (true) {
            if (connect()) {
                System.out.println("Gerando chave RSA...");
                KeyPair chaves = CriptografiaClienteServidor.gerarChavesPublicoPrivada();

                System.out.println("Enviando chave pública...");
                Conexao.enviarChave(socketCliente, chaves.getPublic());

                System.out.println("Recebendo chave pública do cliente...");
                PublicKey chavePublica = Conexao.receberChave(socketCliente);

                while (true) {
                    textoRecebido = Conexao.receber(socketCliente);
                    System.out.println("\nMensagem recebida: " + textoRecebido);

                    textoDecifrado = CriptografiaClienteServidor.decifrar(textoRecebido, chaves.getPrivate());
                    System.out.println("Cliente enviou: " + textoDecifrado);

                    if ("sair".equalsIgnoreCase(textoDecifrado)) {
                        System.out.println("Cliente encerrou a comunicação.");
                        break;
                    }

                    System.out.println("Digite a sua mensagem...");
                    textoEnviado = input.nextLine();

                    textoCifrado = CriptografiaClienteServidor.cifrar(textoEnviado, chavePublica);
                    System.out.println("Texto enviado: " + textoCifrado);

                    Conexao.enviar(socketCliente, textoCifrado);
                }

                socketCliente.close();
            }
        }
    }
}
