package br.com.file.analytic.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Folder {
    static String HOME_PATH = System.getProperty("user.home");

    public static Path get(String ...path) throws IOException {
        Path folderPath = Paths.get(HOME_PATH, path);

        if(!Files.exists(folderPath))
            Files.createDirectories(folderPath);

        return folderPath;
    }

    public static Path concat(Path path, String string) {
        return Path.of(path.toString(), string);
    }
}
