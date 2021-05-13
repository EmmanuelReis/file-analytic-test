package br.com.file.analytic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service{
    private static ExecutorService executor = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() / .5));

    public void create(Path filePathIn, Path filePathOut) throws IOException {
        System.out.printf("Creating file in %s%n", filePathOut);
        
        if(!filePathOut.toFile().exists())
            Files.createFile(filePathOut);

        System.out.println("File created!");

        CompletableFuture.runAsync(analyze(filePathIn, filePathOut), executor);
    }

    public void modify(Path filePathIn, Path filePathOut) throws IOException {
        System.out.printf("Modifying %s%n", filePathOut);

        // delete(filePathIn, filePathOut);
        
        CompletableFuture.runAsync(analyze(filePathIn, filePathOut), executor);

        System.out.println("Modified!");
    }

    public void delete(Path filePathIn, Path filePathOut) throws IOException {
        Files.deleteIfExists(filePathOut);

        System.out.printf("%s deleted%n", filePathOut);
    }

    private Runnable analyze(Path filePathIn, Path filePathOut) {
        return new Analystic(filePathIn, filePathOut);
    }
}
