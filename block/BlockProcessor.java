package block;

import core.DES;
import core.TripleDES;

public class BlockProcessor {
    public static  long byteToLong(byte [] data, int offset){
        long value = 0;
        for(int i=0; i<8; i++){
            value  = (value<<8) | (data[offset+i] & 0xFFL);
        }
        return value;
    }

    public static byte[] longToByte(long data){
        byte[] result = new byte[8];

        for(int i=7; i>=0; i--){
            result[i] = (byte) (data & 0xFFL);
            data >>>= 8;
        }
        return result;
    }

    public static byte[] encryptBlock(byte[] data, long key){
        byte [] result = new byte[data.length];

        for(int offset=0; offset<data.length; offset+=8){
            long block = byteToLong(data, offset);
            long encrypted = DES.encryptBlock(block, key);
            byte [] encryptedByte = longToByte(encrypted);
            System.arraycopy(
                    encryptedByte, 0,
                    result, offset,
                    8
            );
        }
        return  result;
    }

    public static byte[] decryptBlock(byte [] data, long key){
        byte [] result = new byte[data.length];

        for(int offset=0; offset<data.length; offset+=8){
            long block = byteToLong(data, offset);
            long decrypted = DES.decryptBlock(block, key);
            byte [] decryptedByte = longToByte(decrypted);
            System.arraycopy(
                    decryptedByte, 0,
                    result, offset,
                    8
            );
        }
        return result;
    }

    public static byte[] encryptBlock3DES(byte[] data, TripleDES tripleDES){
        byte [] result = new byte[data.length];

        for(int offset=0; offset<data.length; offset+=8){
            long block = byteToLong(data, offset);
            long encrypted = tripleDES.encryptionBlock(block);
            byte [] encryptedByte = longToByte(encrypted);
            System.arraycopy(
                    encryptedByte, 0,
                    result, offset,
                    8
            );
        }
        return result;
    }

    public static byte [] decryptBlock3DES(byte[] data, TripleDES tripleDES){
        byte [] result = new byte[data.length];

        for(int offset=0; offset<data.length; offset+=8){
            long block = byteToLong(data, offset);
            long decrypted = tripleDES.decryptionBlock(block);
            byte [] decryptedByte = longToByte(decrypted);
            System.arraycopy(
                    decryptedByte, 0,
                    result, offset,
                    8
            );
        }
        return result;
    }
}
