package br.com.file.analytic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.file.analytic.util.Folder;

class FileAnalyticTestApplicationTests {

    private Service service = new Service();
    private static Path inputFolderPath;
    private static Path outputFolderPath;

    @BeforeAll
    public static void setup() throws IOException {
        inputFolderPath = Folder.get("test", "fileanalytic", "in");
        outputFolderPath = Folder.get("test", "fileanalytic", "out");
    }

    @Test
    @DisplayName("Should be delete a file from the out path when a file is deleted from the in path")
    public void fileDelete() throws IOException {
        String filename = "testDelete.txt";
        Path filePathIn = Folder.concat(inputFolderPath, filename);
        Path filePathOut = Folder.concat(outputFolderPath, filename);
        
        Files.createFile(filePathOut);

        service.delete(filePathIn, filePathOut);

        assertTrue(!filePathOut.toFile().exists());
    }

    @Test
    @DisplayName("Should be analyze a file when creating")
    public void analytic() throws IOException, InterruptedException {
        String filename = "testAnalytic.txt";
        Path resource = Paths.get("src", "test", "java", "br", "com", "file", "analytic", "resources", filename);
        Path filePathIn = Folder.concat(inputFolderPath, filename);
        Path filePathOut = Folder.concat(outputFolderPath, filename);

        Files.copy(resource, filePathIn, StandardCopyOption.REPLACE_EXISTING);

        service.create(filePathIn, filePathOut);

        Path expectedFile = Paths.get("src", "test", "java", "br", "com", "file", "analytic", "resources", "out.txt");

        assertEquals(Files.readAllLines(expectedFile), Files.readAllLines(filePathOut));
    }
}
