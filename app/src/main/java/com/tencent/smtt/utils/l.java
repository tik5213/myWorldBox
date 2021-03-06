package com.tencent.smtt.utils;

import android.util.Base64;
import com.MCWorld.framework.base.utils.UtilsRSA;

import java.security.KeyFactory;
import java.security.Provider;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;
import javax.crypto.Cipher;

public class l {
    private static final char[] a = "0123456789abcdef".toCharArray();
    private static l b;
    private String c;
    private String d;
    private String e = String.valueOf(new Random().nextInt(89999999) + b.bpf);

    private l() {
        int nextInt = new Random().nextInt(89999999) + b.bpf;
        this.c = this.e + String.valueOf(nextInt);
    }

    public static synchronized l a() {
        l lVar;
        synchronized (l.class) {
            if (b == null) {
                b = new l();
            }
            lVar = b;
        }
        return lVar;
    }

    private String b(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            cArr[i * 2] = a[i2 >>> 4];
            cArr[(i * 2) + 1] = a[i2 & 15];
        }
        return new String(cArr);
    }

    public String a(String str) {
        byte[] bytes = str.getBytes();
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        } catch (Exception e) {
            try {
                b();
                cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        cipher.init(1, KeyFactory.getInstance(UtilsRSA.KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode("MCwwDQYJKoZIhvcNAQEBBQADGwAwGAIRAMRB/Q0hTCD+XtnQhpQJefUCAwEAAQ==".getBytes(), 0))));
        return b(cipher.doFinal(bytes));
    }

    public byte[] a(byte[] bArr) {
        return a.a(this.e.getBytes(), bArr, 1);
    }

    public void b() {
        Security.addProvider((Provider) Class.forName("com.android.org.bouncycastle.jce.provider.BouncyCastleProvider", true, ClassLoader.getSystemClassLoader()).newInstance());
    }

    public String c() {
        if (this.d == null) {
            byte[] bytes = this.c.getBytes();
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            } catch (Exception e) {
                try {
                    b();
                    cipher = Cipher.getInstance("RSA/ECB/NoPadding");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            cipher.init(1, KeyFactory.getInstance(UtilsRSA.KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode("MCwwDQYJKoZIhvcNAQEBBQADGwAwGAIRAMRB/Q0hTCD+XtnQhpQJefUCAwEAAQ==".getBytes(), 0))));
            this.d = b(cipher.doFinal(bytes));
        }
        return this.d;
    }
}
