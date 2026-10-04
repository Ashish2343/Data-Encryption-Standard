package file;

import service.TripleDESService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class FileEncryptionService {
    private final TripleDESService tripleDESService;

    public FileEncryptionService(long key1, long key2, long key3){
        this.tripleDESService =new TripleDESService(key1, key2, key3);
    }

    public void encryptFile(String inputPath, String outputPath) throws IOException, ExecutionException, InterruptedException {

        byte [] data = Files.readAllBytes(Path.of(inputPath));

        byte [] encrypted = tripleDESService.asynchronousEncrypt(data);

        Files.write(Path.of(outputPath), encrypted);
    }

    public void decryptFile(String inputPath, String outputPath) throws IOException, ExecutionException, InterruptedException {

        byte [] data = Files.readAllBytes(Path.of(inputPath));

        byte [] decrypted = tripleDESService.asynchronousDecrypt(data);

        Files.write(Path.of(outputPath), decrypted);
    }

    public void shutdown(){
        tripleDESService.shutdown();
    }
}
