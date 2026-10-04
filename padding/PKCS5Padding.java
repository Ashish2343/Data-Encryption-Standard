package padding;

public class PKCS5Padding implements Padding {

    private static final int BLOCK_SIZE = 8;

    @Override
    public byte[] addPadding(byte[] data) {
       int paddingLength = BLOCK_SIZE - (data.length % BLOCK_SIZE);

       byte [] result = new byte[data.length + paddingLength];

       System.arraycopy(
               data, 0,
               result, 0,
               data.length
       );

       for(int i=data.length; i<result.length; i++){
           result[i] = (byte) paddingLength;
       }
       return result;
    }

    @Override
    public byte[] removePadding(byte[] data) {
        int paddingLength = data[data.length - 1] & 0xFF;

        if (paddingLength < 1 || paddingLength > BLOCK_SIZE) {
            throw new IllegalArgumentException(
                    "Invalid padding"
            );
        }

        for(int i=data.length-paddingLength; i<data.length; i++){
            if((data[i] & 0xFF) != paddingLength){
                throw new IllegalArgumentException(
                        "Invalid Padding"
                );
            }
        }

        byte [] result = new byte[data.length - paddingLength];

        System.arraycopy(
                data, 0,
                result, 0,
                result.length
        );

        return result;
    }
}
