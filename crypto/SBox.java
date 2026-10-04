package crypto;

import core.DESConstants;

public class SBox {
    public static int substitute(long input48){
        int output = 0;
        for(int cycle = 0; cycle<8; cycle++){

            int shift = 42 - (cycle * 6);
            long sixBits =  (input48 >> shift) & 0x3F;

            int row = getRow(sixBits);
            int col = getCol(sixBits);

            int value = DESConstants.S_BOXES[cycle][row][col];

            output = (output << 4) | value;
        }
        return  output;
    }

    public static int getRow(long sixBits){
        int rowMSB = ((int) sixBits >> 5) & 1;
        int rowLSB = (int) sixBits & 1;

        return (rowMSB<<1) | rowLSB;
    }

    public static int getCol(long sixBits){
        int middleBits = ((int) sixBits & 30);

        return middleBits>>1;
    }

}
