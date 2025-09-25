package GraphGenerator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphChecker {

    public static void main(String[] args) {
        String filePath = "non_eulerian_graph_1000.txt"; // Altere para o caminho do seu arquivo

        try {
            checkGraphType(filePath);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static void checkGraphType(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Lê o número de vértices na primeira linha
            long n = Long.parseLong(reader.readLine().trim());
            
            // Array para armazenar o grau de cada vértice
            long[] degrees = new long[(int) n];
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(" ");
                    long u = Long.parseLong(parts[0]);
                    long v = Long.parseLong(parts[1]);
                    
                    degrees[(int) u]++;
                    degrees[(int) v]++;
                }
            }

            int oddDegreeCount = 0;
            for (long degree : degrees) {
                if (degree % 2 != 0) {
                    oddDegreeCount++;
                }
            }

            // Classificação do grafo
            if (oddDegreeCount == 0) {
                System.out.println("O grafo é Euleriano.");
            } else if (oddDegreeCount == 2) {
                System.out.println("O grafo é Semi-Euleriano.");
            } else {
                System.out.println("O grafo não é Euleriano.");
            }
        }
    }
}