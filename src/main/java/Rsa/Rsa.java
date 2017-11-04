// package main.java.Rsa;

import java.math.BigInteger;

public class Rsa {
  static final String PUBMOD = "c406136c12640a665900a9df4df63a84fc855927b729a3a106fb3f379e8e4190ebba442f67b93402e535b18a5777e6490e67dbee954bb02175e43b6481e7563d3f9ff338f07950d1553ee6c343d3f8148f71b4d2df8da7efb39f846ac07c865201fbb35ea4d71dc5f858d9d41aaa856d50dc2d2732582f80e7d38c32aba87ba9";

  public static void main(String[] args) {
    if(args.length < 1) {
      System.out.println("Expected password");
      System.exit(1);
    }
    BigInteger m = new BigInteger(args[0].getBytes());
    BigInteger e = new BigInteger("65537");
    BigInteger n = new BigInteger(PUBMOD, 16);

    BigInteger c = modExp(m, e, n);

    System.out.println(c.toString(16));
  }

  static BigInteger modinv(BigInteger a, BigInteger b) {
    return a.modInverse(b);
  }

  static BigInteger modExp(BigInteger m, BigInteger e, BigInteger n) {
    // BigInteger y = BigInteger.ONE;
    // for(i = 0; n.n.subtract(BigInteger.ONE); i++) {
    //
    // }
    return squareMul(m, e).mod(n);
  }

  static BigInteger squareMul(BigInteger x, BigInteger n) {
    if(n.equals(BigInteger.ONE)) {
      return x;
    }
    if(n.mod(new BigInteger("2")).equals(BigInteger.ZERO)){
      return squareMul(x.pow(2), n.divide(new BigInteger("2")));
    }
    return x.multiply(squareMul(x.pow(2), n.subtract(BigInteger.ONE).divide(new BigInteger("2"))));
  }
}
