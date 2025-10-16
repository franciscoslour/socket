import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Scanner;



public class Client {

    private Socket socket;

    public void comunicarComServidor() throws Exception{

        String textoRequisicao  = "Conexão estabelecida";
        String textoRecebido  = "";
        String textoDecifrado = "";
        String textoCifrado = "";

        socket = new Socket("localhost", 9600);

        System.out.println("Gerando chave RSA...");
        KeyPair chaves = CriptografiaClienteServidor.gerarChavesPublicoPrivada();

        Scanner input = new Scanner(System.in);
        System.out.println("Recebendo chave pública do servidor... ");
        PublicKey chavePublica = Conexao.receberChave(socket);

        System.out.println("Enviando chave pública...");
        Conexao.enviarChave(socket, chaves.getPublic());

        System.out.println("\nDigite a sua mensagem: ");
        textoRequisicao = input.nextLine();

        textoCifrado = CriptografiaClienteServidor.cifrar(textoRequisicao, chavePublica);

        System.out.println("Mensagem cifrada: "+textoCifrado);
        Conexao.enviar(socket, textoCifrado);

        textoRecebido = Conexao.receber(socket);
        System.out.println("\nServidor enviou: "+textoRecebido);

        textoDecifrado = CriptografiaClienteServidor.decifrar(textoRecebido, chaves.getPrivate());
        System.out.println("Texto decifrado: "+textoDecifrado);
    }

    public static void main(String[] args){

        try{
            Client cliente = new Client();
            cliente.comunicarComServidor();
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

}
