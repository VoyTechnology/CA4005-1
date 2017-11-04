import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// Temporary debugging imports.
import java.io.ByteArrayOutputStream;

class RandGen {
  static byte[] generate() {
    //TODO: Replace with actual random generation.
    return new byte[]{0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x0};
  }
}


public class Assignment1 {
  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("Password and file to encrypt must be provided");
      System.exit(1);
    }

    byte[] salt = RandGen.generate();
    System.out.println("Salt:" + toHex(salt));

    byte[] key = hash(appendBytes(salt, args[0].getBytes()));

    try {
      encrypt(args[1], key);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }

  static String toHex(final byte[] bytes) {
    StringBuffer sb = new StringBuffer();
    for(byte b: bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  static byte[] appendBytes(byte[] a, byte[] b) {
    byte[] res = new byte[a.length + b.length];
    System.arraycopy(a, 0, res, 0, a.length);
    System.arraycopy(b, 0, res, a.length, b.length);
    return res;
  }

  static byte[] hash(byte[] in) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      for(int i = 0; i < 200; i++) {
        in = digest.digest(in);
      }
      return in;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  static void encrypt(String file, byte[] keyBytes) throws Exception {
    System.out.println("Key: " + toHex(keyBytes));
    Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec iv = new IvParameterSpec(RandGen.generate());
    System.out.println("IV: "+toHex(iv.getIV()));

    c.init(Cipher.ENCRYPT_MODE, key, iv);

    InputStream in = new FileInputStream(file);
    //TODO: Swap around after debugging.
    // OutputStream out = new FileOutputStream(file+".encrypted");
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    while(true){
      byte[] buf = new byte[16];
      int n = in.read(buf, 0, 16);
      System.out.println("Managed to read in " + n + " bytes");
      if(n == 16){
        out.write(c.update(buf));
        System.out.println("Block:" + toHex(buf));
        continue;
      }
      if(n == -1) {
        buf = new byte[]{(byte)0x80, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        System.out.println("Block:" + toHex(buf));
        out.write(c.update(buf));
        break;
      }
      buf[n] = (byte)0x80;
      while (n < 15) {
        n++;
        buf[n] = 0x00;
      }
      System.out.println("Block:" + toHex(buf));
      out.write(c.update(buf));
      break;
    }

    in.close();
    //TODO: Remove after debugging
    System.out.println("OUTPUT: " + toHex(out.toByteArray()));
    out.flush();
    out.close();
  }
}
