package core;

public final class Permutation {
    public static long permute(long input, int [] table, int inputSize){
        long output = 0;

        for(int position: table){
            int shift = inputSize - position;
            long bit = (input >>> shift) & 1;
            output = (output<<1) | bit;
        }
        return output;
    }

    public static long permute(int input, int [] table, int inputSize){
        int output = 0;

        for(int position: table){
            int shift = inputSize - position;
            int bit = (shift >> shift) & 1;
            output = (output<<1) | bit;
        }
        return output;
    }

    private Permutation(){
    }

}
