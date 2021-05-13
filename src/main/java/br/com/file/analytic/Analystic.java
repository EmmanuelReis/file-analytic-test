package br.com.file.analytic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analystic implements Runnable {
    private Path filePathIn;
    private Path filePathOut;
    
    public Analystic(Path filePathIn, Path filePathOut) {
        this.filePathIn = filePathIn;
        this.filePathOut = filePathOut;
    }

    @Override
    public void run() {
        System.out.printf("Analyzing: %s%n", filePathIn);

        FileInputStream inputStream = null;
        Scanner scanner = null;
        Pattern pattern = Pattern.compile("^(?<code>[0-9]{3})ç(?<id>[0-9]+)ç(?<data>.+)ç(?<info>.+)");
        Pattern patternSale = Pattern.compile("((?<iid>[0-9]+)-(?<qtd>[0-9]+)-(?<value>.*?)[,|\\]])");
        
        try {
            Integer clients = 0;
            Float mostExpensiveSaleValue = 0f;
            String mostExpensiveSale = "teste";
            Map<String, Float> totalSalesValueBySalesman = new HashMap<String, Float>();

            inputStream = new FileInputStream(filePathIn.toString());
            scanner = new Scanner(inputStream, "UTF-8");
            
            while(scanner.hasNextLine()) {
                Matcher matcher = pattern.matcher(scanner.nextLine());
                
                if(matcher.matches()) {
                    if(matcher.group("code").equals("001"))
                        totalSalesValueBySalesman.put(matcher.group("data"), totalSalesValueBySalesman.getOrDefault(matcher.group("id"), 0f));
                    else if(matcher.group("code").equals("002"))
                        clients++;
                    else if(matcher.group("code").equals("003")) {
                        Matcher saleMatcher = patternSale.matcher(matcher.group("data"));
                        
                        Float value = 0f;

                        while(saleMatcher.find()) {
                            value += Float.parseFloat(saleMatcher.group("value")) * Integer.parseInt(saleMatcher.group("qtd"));
                        }
                        
                        if(value > mostExpensiveSaleValue) {
                            mostExpensiveSale = matcher.group("id");
                            mostExpensiveSaleValue = value;
                        }

                        totalSalesValueBySalesman.put(matcher.group("info"), totalSalesValueBySalesman.getOrDefault(matcher.group("id"), 0f) + value);
                    }
                }
            }
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("Quantidade de clientes: " + clients).append(System.lineSeparator())
            .append("Quantidade de vendedores: " + totalSalesValueBySalesman.size()).append(System.lineSeparator())
            .append("Id da venda mais cara: " + mostExpensiveSale + " de valor " + mostExpensiveSaleValue).append(System.lineSeparator())
            .append("Pior vendedor: " + totalSalesValueBySalesman.entrySet().stream()
                                                                    .min(Comparator.comparing(Map.Entry::getValue))
                                                                    .get().getKey());
            
            Files.writeString(filePathOut, sb.toString(), StandardOpenOption.WRITE);
            
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }

            System.out.println("Analyzed!");
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(scanner != null) {
                scanner.close();
            }
        }
    }
}
