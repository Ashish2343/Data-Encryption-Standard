package core;

public class TripleDES {
    private final long key1;
    private final long key2;
    private final long key3;

    public TripleDES(long key1, long key2, long key3){
        this.key1 =key1;
        this.key2 =key2;
        this.key3 = key3;
    }

    public long encryptionBlock(long plaintext){
        long step1 = DES.encryptBlock(plaintext, key1);
        long step2 = DES.encryptBlock(step1, key2);
        return DES.encryptBlock(step2, key3);
    }

    public long decryptionBlock(long ciphertext){
        long step1 = DES.decryptBlock(ciphertext, key3);
        long step2 = DES.decryptBlock(step1, key2);
        return DES.decryptBlock(step2, key1);
    }

}
