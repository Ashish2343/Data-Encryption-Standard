package service;

import block.BlockProcessor;
import core.TripleDES;
import padding.PKCS5Padding;
import padding.Padding;
import thread.ParallelBlockProcessor;

import java.util.concurrent.ExecutionException;

public class TripleDESService {
    private final TripleDES tripleDES;
    private final Padding padding;
    private final ParallelBlockProcessor processor;

    public TripleDESService(long key1, long key2, long key3){
        this(key1, key2, key3, Runtime.getRuntime().availableProcessors());
    }

    public TripleDESService(long key1, long key2, long key3, int threadCount){
        this.tripleDES = new TripleDES(key1, key2, key3);
        this.padding = new PKCS5Padding();
        this.processor = new ParallelBlockProcessor(threadCount);
    }

    public byte [] encrypt(byte [] data){
        byte [] padded = padding.addPadding(data);
        return BlockProcessor.encryptBlock3DES(padded, tripleDES);
    }

    public byte [] decrypt(byte [] data){
        byte [] decrypt = BlockProcessor.decryptBlock3DES(data, tripleDES);
        return padding.removePadding(decrypt);
    }

    public byte [] asynchronousEncrypt(byte [] data) throws ExecutionException, InterruptedException {
        byte [] padded = padding.addPadding(data);
        return processor.encrypt3DES(padded, tripleDES);
    }

    public byte []  asynchronousDecrypt(byte [] data) throws ExecutionException, InterruptedException {
        byte [] decrypt = processor.decrypt3DES(data, tripleDES);
        return padding.removePadding(decrypt);
    }

    public void shutdown(){
        processor.shutdown();
    }
}
