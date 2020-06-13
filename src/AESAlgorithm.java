import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AESAlgorithm {

    public static void main(String[] args) throws Exception {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        File selectedFile;

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
            FileReader fr = new FileReader(selectedFile);

            int i = fr.read();
            List<Byte> byteList = new ArrayList();
            while (i != -1) {
                Integer integer = i;
                byte byteValue = integer.byteValue();
                byteList.add(byteValue);
                i = fr.read();
            }

            final byte[] msgOriginal = new byte[byteList.size()];
            AtomicInteger count = new AtomicInteger();
            count.set(0);
            byteList.forEach(aByte -> {
                msgOriginal[count.get()] = aByte.byteValue();
                count.getAndIncrement();
            });

            String password = JOptionPane.showInputDialog("Informe uma senha com 16 bytes separados por virgula");
            byte[] passwordBytes = new byte[16];
            String[] pedacos = password.split(",");

            try {
                for (int j = 0; j < pedacos.length; j++) {
                    passwordBytes[j] = (byte) Integer.parseInt(pedacos[j]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            MessageDigest messageDigestSHA1 = MessageDigest.getInstance("SHA-1");
            passwordBytes = messageDigestSHA1.digest(password.getBytes());
            System.out.println("SHA-1: " + new BigInteger(1, messageDigestSHA1.digest()).toString(16));
            SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(passwordBytes, 16), "AES");
            //Senha

            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] aesEncript = aes.doFinal(msgOriginal);

            Cipher aesD = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] aesDecript = aes.doFinal(aesEncript);

            System.out.println("E2-Original: " + new String(msgOriginal));
            System.out.println("E2-Criptografada: " + aesEncript.toString());
            System.out.println("E2-Decriptografada: " + new String(aesDecript));

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showOpenDialog(null);

            String m = JOptionPane.showInputDialog("Dê um nome seguido da extensão do arquivo de saída (arquivo.extensao)");

            FileWriter fileWriter = new FileWriter(chooser.getSelectedFile() + "\\"+ m);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf(aesEncript.toString());
            printWriter.close();
        }

    }
}
