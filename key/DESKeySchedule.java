package key;

import core.DESConstants;
import core.Permutation;

import static core.Permutation.permute;

public class DESKeySchedule {
    public long applyPC1(long key){
       return Permutation.permute(key, DESConstants.PERMUTATION_CHOICE_1, 64);
    }

    // 56 -> 28
    public int getC0(long key56){
        return (int) (key56 >> 28);
    }

    public int getD0(long key56){
        return ((int) key56) & 0x0FFFFFFF;
    }

    public int leftRotate28(long value, int shifts){
        value = value & 0x0FFFFFFF;

        long bits = value >>> (28-shifts);
        long leftshift  = (value << shifts) & 0x0FFFFFFF;
        return (int) (leftshift | bits);
    }

    public int rightRotate28(int value, int shifts){
        value = value & 0x0FFFFFFF;

        int bits = value << (28 - shifts) & 0x0FFFFFFF;
        int rightshift =  (value >>> shifts);
        return rightshift | bits;
    }

    public long combineCD(int C, int D){
        long upperHalf = Integer.toUnsignedLong(C & 0x0FFFFFFF) << 28;
        long lowerHalf = Integer.toUnsignedLong(D & 0x0FFFFFFF);
        return upperHalf | lowerHalf;
    }

    public long applyPC2(long combined56){
        return Permutation.permute(combined56, DESConstants.CONTRACTION_PERMUTATION, 56);
    }

    public long[] generateSubKey(long key64){
        long key56 = applyPC1(key64);

        int C = getC0(key56);
        int D = getD0(key56);

        long [] subKeys = new long[16];

        for(int round = 0; round< 16; round++){
            int shift = DESConstants.SHIFT_SCHEDULE[round];
            C = leftRotate28(C, shift);
            D = leftRotate28(D, shift);

            long combined = combineCD(C, D);

            subKeys[round] = applyPC2(combined);
        }

        return subKeys;
    }



}
