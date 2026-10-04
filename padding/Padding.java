package padding;

public interface Padding {
    byte [] addPadding(byte[] data);
    byte [] removePadding(byte [] data);
}
