package service;
import block.BlockProcessor;
import padding.PKCS5Padding;
import padding.Padding;
import thread.ParallelBlockProcessor;

import java.util.concurrent.ExecutionException;

public class DESService {
    private final long key;
    private final Padding padding;
    private final ParallelBlockProcessor processor;

    public DESService(long key){
        this(key, Runtime.getRuntime().availableProcessors());
    }

    public DESService(long key, int threadCount){
        this.key = key;
        this.padding = new PKCS5Padding();
        this.processor = new ParallelBlockProcessor(threadCount);
    }

    public byte [] encrypt(byte [] data){
        byte [] padded = padding.addPadding(data);

        return BlockProcessor.encryptBlock(padded, key);
    }

    public byte [] decrypt(byte [] data){
        byte [] decrypted = BlockProcessor.decryptBlock(data, key);

        return padding.removePadding(decrypted);
    }

    public byte [] asynchronousEncrypt(byte [] data) throws ExecutionException, InterruptedException {
        byte [] padded = padding.addPadding(data);

        return processor.encrypt(padded, key);
    }

    public byte [] asynchronousDecrypt(byte [] data) throws ExecutionException, InterruptedException {
        byte [] decrypt = processor.decrypt(data, key);

        return padding.removePadding(decrypt);
    }


    public void shutdown() {
        processor.shutdown();
    }
}
