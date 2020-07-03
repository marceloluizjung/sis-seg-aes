import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AlgoritmoAES {

    final static int[] sbox = {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76, 0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0, 0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15, 0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75, 0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84, 0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF, 0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8, 0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2, 0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73, 0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB, 0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79, 0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08, 0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A, 0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E, 0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF, 0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16};

    final static int[] LogTable = {
            0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3,
            100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193,
            125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120,
            101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142,
            150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56,
            102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16,
            126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186,
            43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87,
            175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232,
            44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160,
            127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183,
            204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157,
            151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209,
            83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171,
            68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165,
            103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7};

    final static int[] AlogTable = {
            1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53,
            95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170,
            229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49,
            83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205,
            76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136,
            131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154,
            181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163,
            254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160,
            251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65,
            195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117,
            159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128,
            155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84,
            252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202,
            69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14,
            18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23,
            57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1};

    public static byte[][][] keySchedule;
    public static byte[][] matrizEstado;

    // Rotacionar os bytes da palavra
    // Vulgo escrever de tras pra frente
    public static byte[] rotWord(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length-1; i++) {
            result[i] = input[i + 1];
        }
        result[result.length-1] = input[0];
        return result;
    }

    // Substituir os bytes da palavra com base na S-BOX
    public static byte[] subWord(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            int j = input[i];
            result[i] = (byte)(sbox[j & 0x000000FF] & 0xFF);
            //          (byte)(sbox[j / 16] % 16);
        }
        return result;
    }

    // Realizar OR exclusivo com as entradas
    public static byte[] xorWord(byte[] inputA, byte[] inputB) {
        byte[] result = new byte[inputA.length];
        for (int i = 0; i < inputA.length; i++) {
            result[i] = (byte) (inputA[i] ^ inputB[i]);
        }
        return result;
    }

    // Cria um texto em hexa com base nos bytes
    public static String bytesToHex(byte[] input) {
        StringBuilder result = new StringBuilder();
        int contador = 0;
        for (byte porcao : input) {
            result.append("0x");
            result.append(String.format("%02x", porcao));
            result.append(" ");

            contador++;
            if (contador % 4 == 0) {
                result.append("\n");
            }

        }
        return result.toString();
    }

    // Cria um texto com base num roundkey (matriz de byte 4x4)
    public static String roundKeyToHex(byte[][] input) {

        byte[][] inputAux = new byte[4][4];
        byte[] aux = new byte[16];
        int contador = 0;

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                inputAux[j][i] = input[i][j];
            }
        }

        for (int i = 0; i < inputAux.length; i++) {
            for (int j = 0; j < inputAux[i].length; j++) {
                aux[contador] = inputAux[i][j];
                contador++;
            }
        }

        return bytesToHex(aux);
    }

    // Cria um array de byte com base num roundkey (matriz de byte 4x4)
    public static byte[] roundKeyToByte(byte[][] input, boolean inverse) {

        byte[][] inputAux = new byte[4][4];
        byte[] aux = new byte[16];
        int contador = 0;

        if (inverse) {
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[i].length; j++) {
                    inputAux[j][i] = input[i][j];
                }
            }
        } else {
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[i].length; j++) {
                    inputAux[i][j] = input[i][j];
                }
            }
        }

        for (int i = 0; i < inputAux.length; i++) {
            for (int j = 0; j < inputAux[i].length; j++) {
                aux[contador] = inputAux[i][j];
                contador++;
            }
        }

        return aux;
    }

    // Facilitador dos constants
    public static byte getConstant(int posicao) {
        byte[] constans = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x1B, 0x36};
        return constans[posicao];
    }

    public static byte[][] shiftRows(byte[] input) {

        byte[][] estado = new byte[4][4];

        int coluna = 0, linha = 0;
        for (int i = 0; i < input.length; i++) {
            if (i % 4 == 0 && i > 0) {
                coluna++;
                linha = 0;
            }
            estado[coluna][linha] = input[i];
            linha++;
        }

        byte[] row = new byte[4];
        int shift = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                row[j] = estado[i][j];
            }
            for (int j = 0; j < 4; j++) {
                estado[i][j] = row[(j+shift) % 4];
            }
            shift++;
        }

        return estado;
    }

    public static byte mul (int a, byte b) {
        int inda = (a < 0) ? (a + 256) : a;
        int indb = (b < 0) ? (b + 256) : b;

        if ((a != 0) && (b != 0)) {
            int index = (LogTable[inda] + LogTable[indb]);
            byte val = (byte)(AlogTable[ index % 255 ] );
            return val;
        } else {
            return 0;
        }
    }

    public static byte[][] mixColumn(byte[][] state, int c) {
        byte a[] = new byte[4];

        for (int i = 0; i < 4; i++) {
            a[i] = state[i][c];
        }

        state[0][c] = (byte)(mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
        state[1][c] = (byte)(mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
        state[2][c] = (byte)(mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
        state[3][c] = (byte)(mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));

        return state;
    }

    public static void main(String[] args) throws Exception {


        byte[] chave = new byte[16];

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

            final byte[] textoSimples = new byte[byteList.size()];
            AtomicInteger count = new AtomicInteger();
            count.set(0);
            byteList.forEach(aByte -> {
                textoSimples[count.get()] = aByte.byteValue();
                count.getAndIncrement();
            });

            String password = JOptionPane.showInputDialog("Informe uma chave com 16 bytes separados por virgula");
            String[] pedacos = password.split(",");

            try {
                for (int j = 0; j < pedacos.length; j++) {
                    chave[j] = (byte) Integer.parseInt(pedacos[j]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            keySchedule = new byte[11][4][4];
            matrizEstado = new byte[4][4];

            int coluna = 0, linha = 0;
            for (int k = 0; k < chave.length; k++) {
                if (k % 4 == 0 && k > 0) {
                    coluna++;
                    linha = 0;
                }
                keySchedule[0][coluna][linha] = chave[k];
                linha++;
            }

            System.out.println("Chave");
            System.out.println(bytesToHex(chave));

            System.out.println("RoundKey=0");
            System.out.println(roundKeyToHex(keySchedule[0]));

            byte[][] roundkey = new byte[4][];

            for (int l = 1; l <= 10; l++) {

                System.out.print("RoundKey=");
                System.out.println(l);

                byte[] parteFinal = keySchedule[l - 1][3];

                System.out.println(bytesToHex(parteFinal));

                byte[] rot = rotWord(parteFinal);

                System.out.println(bytesToHex(rot));

                byte[] sub = subWord(rot);

                System.out.println(bytesToHex(sub));

                byte[] roundConstant = {0x00, 0x00, 0x00, 0x00};
                roundConstant[0] = getConstant(l - 1);

                System.out.println(bytesToHex(roundConstant));

                byte[] xor = xorWord(sub, roundConstant);

                System.out.println(bytesToHex(xor));

                byte[] parteInicial = keySchedule[l - 1][0];

                System.out.println(bytesToHex(parteInicial));

                byte[] xor2 = xorWord(parteInicial, xor);

                System.out.println(bytesToHex(xor2));

                roundkey[0] = xor2;


                for (int j = 1; j < 4; j++) {
                    byte[] xor3 = xorWord(keySchedule[l - 1][j], roundkey[j - 1]);
                    roundkey[j] = xor3;
                }

                keySchedule[l] = roundkey.clone();
                System.out.println(roundKeyToHex(keySchedule[l]));

            }

            System.out.println("-----------------");

            roundkey = keySchedule[0];

            System.out.println("Texto Simples");
            System.out.println(bytesToHex(textoSimples));

            byte[] xorConteudo = xorWord(textoSimples, roundKeyToByte(roundkey, true));

            System.out.println("AddRoundKey-Round=0");
            System.out.println(bytesToHex(xorConteudo));

            //----------

            byte[] xorRound = null;
            byte[] subConteudo = null;
            for (int m = 1; m <= 9; m++) {

                if (xorRound == null) {
                    subConteudo = subWord(xorConteudo);
                } else {
                    subConteudo = subWord(xorRound);
                }

                System.out.println("SubBytes-Round=" + m);
                System.out.println(bytesToHex(subConteudo));

                matrizEstado = shiftRows(subConteudo);

                System.out.println("ShiftRows-Round=" + m);
                System.out.println(bytesToHex(roundKeyToByte(matrizEstado, false)));

                for (int j = 0; j < matrizEstado.length; j++) {
                    matrizEstado = mixColumn(matrizEstado, j);
                }

                System.out.println("MixedColumns-Round=" + m);
                System.out.println(bytesToHex(roundKeyToByte(matrizEstado, false)));

                roundkey = keySchedule[m];
                System.out.println("roundkey=" + m);
                System.out.println(roundKeyToHex(roundkey));

                xorRound = xorWord(roundKeyToByte(matrizEstado, false), roundKeyToByte(roundkey, true));

                System.out.println("addRoundKey-Round=" + m);
                System.out.println(bytesToHex(xorRound));

            }


            subConteudo = subWord(xorRound);

            System.out.println("SubBytes-Round=10");
            System.out.println(bytesToHex(subConteudo));

            matrizEstado = shiftRows(subConteudo);

            System.out.println("ShiftRows-Round=10");
            System.out.println(bytesToHex(roundKeyToByte(matrizEstado, false)));

            roundkey = keySchedule[10];

            xorRound = xorWord(roundKeyToByte(matrizEstado, false), roundKeyToByte(roundkey, true));

            System.out.println("addRoundKey-Round=10");
            System.out.println(bytesToHex(xorRound));

            //----------


            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showOpenDialog(null);

            String mm = JOptionPane.showInputDialog("Dê um nome seguido da extensão do arquivo de saída (arquivo.extensao)");

            OutputStream os = new FileOutputStream(chooser.getSelectedFile() + "\\"+ mm);
            os.write(xorRound);
            os.close();

        }
    }

}
