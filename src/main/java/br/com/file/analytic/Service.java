package br.com.file.analytic;

import java.nio.file.Path;

public class Service{
    public void create(Path filePathIn, Path filePathOut) {
        System.out.printf("Creating file in %s%n", filePathOut);
        System.out.println("File created!");

        analyze(filePathIn, filePathOut);
    }

    public void modify(Path filePathIn, Path filePathOut) {
        System.out.printf("Modifying %s%n", filePathOut);

        analyze(filePathIn, filePathOut);

        System.out.println("Modified!");
    }

    public void delete(Path filePathIn, Path filePathOut) {
        System.out.printf("%s deleted%n", filePathOut);
    }

    private void analyze(Path filePathIn, Path filePathOut) {
        System.out.printf("Analizing: %s%n", filePathIn);
        System.out.println("Analized!");
    }
}
