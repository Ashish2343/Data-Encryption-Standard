package crypto;

import core.DESConstants;
import core.Permutation;

public class FeistelFunction {
    public static long expand(int right32){
        return Permutation.permute(Integer.toUnsignedLong(right32), DESConstants.EXPANSION_PERMUTATION, 32 );
    }

    public static long xorWithKey(long expand, long roundKey){
        return  expand ^ roundKey;
    }

    public static int applySBox(long input48){
        return SBox.substitute(input48);
    }

    public static int applyP(int input32){
        return (int) Permutation.permute(Integer.toUnsignedLong(input32), DESConstants.PERMUTATION_P, 32);
    }

    public static int calculate(int input32, long roundKey){
        long expand = expand(input32);

        long xor = xorWithKey(expand, roundKey);

        int SBoxOutput = applySBox(xor);

        return applyP(SBoxOutput);
    }

    private FeistelFunction() {
    }

}
