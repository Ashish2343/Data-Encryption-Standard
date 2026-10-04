package core;

import crypto.FeistelFunction;
import key.DESKeySchedule;

public class DES {
    // initial permutation
    private static long  InitialPermutation(long block){
        return Permutation.permute(block, DESConstants.INITIAL_PERMUTATION, 64);
    }

    // get Left 32
    private static int left32(long block){
        return (int) (block >>> 32);
    }

    // get Right 32
    private static int right32(long block){
        return ((int) block);
    }

    private static int[] round(int left, int right, long roundKey){
        int newRight = left ^ FeistelFunction.calculate(right, roundKey);

        return  new int [] {right, newRight};
    }

    private static long combine(int left, int right){
        long leftHalf = Integer.toUnsignedLong(left) << 32;
        long rightHalf = Integer.toUnsignedLong(right);
        return  leftHalf | rightHalf;
    }

    private static long FinalPermutation(long block){
        return Permutation.permute(block, DESConstants.FINAL_PERMUTATION, 64);
    }

    public static long encryptBlock(long plaintext, long key){

        // generate all 16 keys
        DESKeySchedule keySchedule = new DESKeySchedule();
        long [] subKeys = keySchedule.generateSubKey(key);

        // initial permutation
        long InitialPermuted = InitialPermutation(plaintext);

        // split into 32 - bit halves
        int left = left32(InitialPermuted);
        int right = right32(InitialPermuted);

        for(int i=0; i<16; i++){

            int [] result =  round(left, right, subKeys[i]);

            left = result[0];
            right = result[1];


        }

        long combined = combine(right, left);

        return Permutation.permute(combined, DESConstants.FINAL_PERMUTATION, 64);

    }

    public static long decryptBlock(long ciphertext, long key) {

        DESKeySchedule keySchedule = new DESKeySchedule();
        long[] subKeys = keySchedule.generateSubKey(key);

        long initialPermuted = InitialPermutation(ciphertext);

        int left = left32(initialPermuted);
        int right = right32(initialPermuted);

        for (int i = 15; i >= 0; i--) {

            int[] result = round(left, right, subKeys[i]);

            left = result[0];
            right = result[1];
        }

        long combined = combine(right, left);

        return FinalPermutation(combined);
    }
}
