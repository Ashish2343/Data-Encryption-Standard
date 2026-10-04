package thread;

import block.BlockProcessor;
import core.DES;
import core.TripleDES;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelBlockProcessor {
    private final ExecutorService executorService;

    public ParallelBlockProcessor(int threadCount){
        if(threadCount<=0) throw new IllegalArgumentException("Thread Count must be positive");
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public byte[] encrypt(byte[] data, long key) throws InterruptedException, ExecutionException{
        validateData(data);
        List<Future<byte[]>> futures = new ArrayList<>();

        for(int offset =0; offset<data.length; offset+=8){
            final int start = offset;

            Callable<byte[]> task = () ->{
                long block = BlockProcessor.byteToLong(data, start);

                long encrypted = DES.encryptBlock(block, key);

                return BlockProcessor.longToByte(encrypted);
            };
            Future<byte[]> future = executorService.submit(task);
            futures.add(future);
        }
        return  collectResult(futures, data.length);
    }

    public byte[] encrypt3DES(byte[] data, TripleDES tripleDES) throws InterruptedException, ExecutionException {
        validateData(data);
        List<Future<byte[]>> futures = new ArrayList<>();

        for(int offset = 0; offset<data.length; offset+=8){
            final int start = offset;

            Callable<byte[]> task = () -> {

                long block = BlockProcessor.byteToLong(data, start);

                long encrypted = tripleDES.encryptionBlock(block);

                return BlockProcessor.longToByte(encrypted);
            };

            Future<byte []> future = executorService.submit(task);
            futures.add(future);
        }
        return collectResult(futures, data.length);
    }

    public byte[] decrypt(byte[] data, long key) throws ExecutionException, InterruptedException {
        validateData(data);
        List<Future<byte[]>> futures = new ArrayList<>();

        for(int offset=0; offset<data.length; offset+=8){
            final int start = offset;

            Callable<byte[]> task = () ->{
                long block = BlockProcessor.byteToLong(data, start);

                long decrypted = DES.decryptBlock(block, key);

                return BlockProcessor.longToByte(decrypted);
            };

            Future<byte[]> future = executorService.submit(task);

            futures.add(future);
        }
        return collectResult(futures, data.length);
    }

    public byte [] decrypt3DES(byte[] data, TripleDES tripleDES) throws ExecutionException, InterruptedException {
        validateData(data);
        List<Future<byte[]>> futures = new ArrayList<>();

        for(int offset=0; offset<data.length; offset+=8){
            final int start = offset;

            Callable<byte[]> task = () -> {
                long block = BlockProcessor.byteToLong(data, start);

                long decrypted = tripleDES.decryptionBlock(block);

                return BlockProcessor.longToByte(decrypted);
            };

            Future<byte[]> future = executorService.submit(task);
            futures.add(future);
        }
        return collectResult(futures, data.length);
    }

    private byte[] collectResult(List<Future<byte[]>> futures,  int length) throws ExecutionException, InterruptedException {
        byte[] result = new byte[length];

        for(int i=0; i<futures.size(); i++){
            byte[] block = futures.get(i).get();

            System.arraycopy(
                    block, 0,
                    result, i*8,
                    8
            );
        }
        return result;
    }

    private void validateData(byte[] data){
        if(data==null) throw new IllegalArgumentException("Data cannot be null");

        if(data.length % 8 !=0) throw new IllegalArgumentException("Data length must be multiple of 8");
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
