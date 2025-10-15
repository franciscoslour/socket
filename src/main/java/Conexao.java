import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

public class Conexao {

    public static String receber(Socket socket) throws IOException {

        InputStream in = socket.getInputStream();
        byte[] infoBytes = new byte[256];
        int bytesLidos = in.read(infoBytes);

        return bytesLidos > 0 ? Base64.getEncoder().encodeToString(infoBytes) : "";

    }

    public static PublicKey receberChave(Socket socket) throws Exception{
        InputStream in = socket.getInputStream();
        byte[] infoBytes = new byte[256];
        int bytesLidos = in.read(infoBytes);

        return bytesLidos > 0 ? CriptografiaClienteServidor.bytesParaChave(infoBytes) : null;
    }

    public static void enviarChave(Socket socket, PublicKey chave) throws  IOException{
        OutputStream out = socket.getOutputStream();
        out.write(chave.getEncoded());
    }

    public static void enviar(Socket socket, String textoRequisicao) throws IOException{
        byte[] bytesRequisicao = Base64.getDecoder().decode(textoRequisicao);
        OutputStream out = socket.getOutputStream();
        out.write(bytesRequisicao);
    }

}
