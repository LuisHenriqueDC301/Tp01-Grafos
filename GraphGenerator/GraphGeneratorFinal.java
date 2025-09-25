import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGeneratorFinal {

    public static void main(String[] args) {
        long n = 10000; // Tamanho do grafo
        int numEdges = 50000; // Número de arestas

        generateEulerianGraph(n, numEdges);
        generateNonEulerianGraph(n, numEdges);
        generateSemiEulerianGraph(n, numEdges);

        System.out.println("Arquivos de grafo gerados com sucesso!");
    }

    /**
     * Gera um grafo euleriano com n vértices, garantindo que não haja excesso de arestas paralelas.
     * @param n O número de vértices.
     * @param numEdges O número de arestas.
     */
    public static void generateEulerianGraph(long n, int numEdges) {
        try (FileWriter writer = new FileWriter("eulerian_graph_" + n + ".txt")) {
            writer.write(n + "\n");
            
            Random random = new Random();
            long[] degrees = new long[(int) n];
            int edgesCount = 0;

            // Gera arestas aleatoriamente e conta o grau de cada vértice
            while (edgesCount < numEdges) {
                long u = random.nextLong(n);
                long v = random.nextLong(n);
                
                if (u == v) continue;
                
                writer.write(u + " " + v + "\n");
                degrees[(int) u]++;
                degrees[(int) v]++;
                edgesCount++;
            }

            // Lista para armazenar vértices com grau ímpar
            List<Long> oddDegreeVertices = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (degrees[i] % 2 != 0) {
                    oddDegreeVertices.add((long) i);
                }
            }

            // Se houver vértices com grau ímpar, pareamos e adicionamos arestas
            for (int i = 0; i < oddDegreeVertices.size() - 1; i += 2) {
                long u = oddDegreeVertices.get(i);
                long v = oddDegreeVertices.get(i + 1);
                writer.write(u + " " + v + "\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
    
    /**
     * Gera um grafo não euleriano com n vértices.
     * @param n O número de vértices.
     * @param numEdges O número de arestas.
     */
    public static void generateNonEulerianGraph(long n, int numEdges) {
        try (FileWriter writer = new FileWriter("non_eulerian_graph_" + n + ".txt")) {
            writer.write(n + "\n");
            
            Random random = new Random();
            for (int i = 0; i < numEdges; i++) {
                long u = random.nextLong(n);
                long v = random.nextLong(n);
                
                if (u == v) continue;
                
                writer.write(u + " " + v + "\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
    
    /**
     * Gera um grafo semi-euleriano com n vértices.
     * @param n O número de vértices.
     * @param numEdges O número de arestas.
     */
    public static void generateSemiEulerianGraph(long n, int numEdges) {
        try (FileWriter writer = new FileWriter("semi_eulerian_graph_" + n + ".txt")) {
            writer.write(n + "\n");
            
            Random random = new Random();
            long[] degrees = new long[(int) n];
            int edgesCount = 0;

            // Gera arestas aleatoriamente e conta o grau de cada vértice
            while (edgesCount < numEdges) {
                long u = random.nextLong(n);
                long v = random.nextLong(n);
                
                if (u == v) continue;
                
                writer.write(u + " " + v + "\n");
                degrees[(int) u]++;
                degrees[(int) v]++;
                edgesCount++;
            }

            // Lista para armazenar vértices com grau ímpar
            List<Long> oddDegreeVertices = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (degrees[i] % 2 != 0) {
                    oddDegreeVertices.add((long) i);
                }
            }

            // Remove dois vértices com grau ímpar da lista
            if (oddDegreeVertices.size() > 2) {
                oddDegreeVertices.remove(oddDegreeVertices.size() - 1);
                oddDegreeVertices.remove(oddDegreeVertices.size() - 1);
            }
            
            // Adiciona arestas para ajustar o grau, deixando apenas dois ímpares
            for (int i = 0; i < oddDegreeVertices.size() - 1; i += 2) {
                long u = oddDegreeVertices.get(i);
                long v = oddDegreeVertices.get(i + 1);
                writer.write(u + " " + v + "\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}