
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {


            int N = 10000;

            String metodo = "naive";
            Path pasta = Paths.get("C:\\Users\\BT Gaming Store\\Documents\\PUC MINAS\\4-PERIODO\\GRAFOS\\TP01\\Tp01-Grafos\\GeradorDeGrafos\\Grafos_" + N);
            Path relatorios = Paths.get("C:\\Users\\BT Gaming Store\\Documents\\PUC MINAS\\4-PERIODO\\GRAFOS\\TP01\\Tp01-Grafos\\Relatorios\\");
            if (!Files.isDirectory(pasta)) {
                System.err.println("❌ Pasta não encontrada: " + pasta.toAbsolutePath());
                return;
            }

            // pega só .txt; sua pasta tem 12 arquivos (4 de cada tipo)
            List<Path> arquivos = Files.list(pasta)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .sorted()
                    .collect(Collectors.toList());

            if (arquivos.isEmpty()) {
                System.err.println("❌ Nenhum .txt em " + pasta.toAbsolutePath());
                return;
            }

            Path relatorio = relatorios.resolve("relatório_" + metodo + "_" + N + ".txt");
            long somaMs = 0;
            int cont = 0;

            try (BufferedWriter w = Files.newBufferedWriter(relatorio)) {
                w.write("Relatório " + metodo.toUpperCase() + " | N=" + N);
                w.newLine();

                for (Path arq : arquivos) {
                    long t0 = System.nanoTime();
                    try {
                       
                            // chama seu Naive
                            Fleury_Naive.executar(arq.toString());
                        
                    } catch (FileNotFoundException e) {
                        System.err.println("Arquivo não encontrado: " + arq.getFileName());
                        continue;
                    } catch (Exception e) {
                        System.err.println("Falha ao executar em " + arq.getFileName() + ": " + e.getMessage());
                        continue;
                    }
                    long t1 = System.nanoTime();
                    long ms = (t1 - t0) / 1_000_000L;

                    somaMs += ms;
                    cont++;

                    w.write(arq.getFileName().toString() + " - " + ms + " ms");
                    w.newLine();
                }

                double media = (cont > 0) ? (somaMs * 1.0 / cont) : 0.0;
                w.write("Média total: " + String.format(Locale.US, "%.2f", media) + " ms");
                w.newLine();
            }

            System.out.println("✅ Relatório gerado: " + relatorio.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro de I/O: " + e.getMessage());
        }
    }


}
