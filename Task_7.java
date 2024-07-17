import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

class Task_7 {

    // Convert a byte array to a hexadecimal string
    public static String StringToHexString(byte[] input, int length) {
        StringBuilder output = new StringBuilder();
        for (int j = 0; j < length; j++) {
            output.append(String.format("%02x", input[j]));
        }
        return output.toString();
    }

    // Convert a hexadecimal string to a byte array
    public static byte[] HexstrToString(String hexstr) {
        int len = hexstr.length();
        if (len % 2 != 0)
            return null;
        int final_len = len / 2;
        byte[] chrs = new byte[final_len];
        for (int i = 0, j = 0; j < final_len; i += 2, j++)
            chrs[j] = (byte) ((hexstr.charAt(i) % 32 + 9) % 25 * 16 + (hexstr.charAt(i + 1) % 32 + 9) % 25);
        return chrs;
    }

    // Fix the length of the key to be 16 bytes
    public static byte[] Check_lenght(byte[] key) {
        byte[] result = new byte[16];
        int i = 0;
        if (i < key.length - 1) {
            while (i < key.length && key[i] != '\n') {
                result[i] = key[i];
                i++;
            }
            for (int j = i; j < 16; j++)
                result[j] = '#';
        }
        return result;
    }

    // Encrypt a string using AES encryption with a fixed key and IV
    public static String Find_key(byte[] wordFromFile) {
        byte[] outbuf = new byte[1024];
        int outlen, tmplen;

        // Bogus key and IV: we'd normally set these from another source.
        byte[] key = Check_lenght(wordFromFile);
        byte[] iv = HexstrToString("aabbccddeeff00998877665544332211");
        String intext = "This is a top secret.";
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

            outlen = cipher.update(intext.getBytes(), 0, intext.length(), outbuf, 0);
            tmplen = cipher.doFinal(outbuf, outlen);
            outlen += tmplen;
            return StringToHexString(outbuf, outlen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        File textfile;
        String line;
        int match = 0;
        textfile = new File("words.txt");
        System.out.println(textfile.length());

        try {
            Scanner scanner = new Scanner(textfile);
            while (scanner.hasNextLine() && match == 0) {
                line = scanner.nextLine();
                System.out.println("Trying key " + line + " ");
                
                // Check if the key length is less than or equal to 16
                if (line.length() <= 16) {
                    // Check if the encrypted result matches the expected value
                    if ((Find_key(line.getBytes()).toString())
                            .equals("764aa26b55a4da654df6b19e4bce00f4ed05e09346fb0e762583cb7da2ac93a2")) {
                        match = 1;
                        System.out.println("Success: key is " + line);
                    } else
                        System.out.println("Failed");
                } else
                    System.out.println("Failed");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
