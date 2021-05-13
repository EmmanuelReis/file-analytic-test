package br.com.file.analytic;

import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;
import java.util.stream.Stream;

import br.com.file.analytic.util.Folder;

public class App {
    public static void main(String[] args) {
        try {
            Service service = new Service();
            Path inputFolderPath = Folder.get("data", "in");
            Path outputFolderPath = Folder.get("data", "out");
            
            WatchService watchService = FileSystems.getDefault().newWatchService();

            WatchKey key = inputFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, 
                                                            StandardWatchEventKinds.ENTRY_DELETE, 
                                                            StandardWatchEventKinds.ENTRY_MODIFY);

            while(true) {
                for(WatchEvent<?> watchEvent : key.pollEvents()) {
                    String methodName = watchEvent.kind().toString().replace("ENTRY_", "").toLowerCase();
                    String filename = watchEvent.context().toString();
    
                    Optional<Method> method = Stream.of(Service.class.getDeclaredMethods())
                            .filter(item -> item.getName().equals(methodName))
                            .findFirst();
    
                    if(method.isPresent())
                        method.get().invoke(service, Folder.concat(inputFolderPath, filename), Folder.concat(outputFolderPath, filename));
                }
            }
        }
        catch(Exception error) {
            System.out.println(error);
        }
    }
}
