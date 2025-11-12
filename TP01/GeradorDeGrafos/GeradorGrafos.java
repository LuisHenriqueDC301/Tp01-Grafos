
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GeradorGrafos {

    public static void main(String[] args) {
        int n = 100000; // número de vértices
        int m = 150000; // número de arestas

        int qtdEuleriano = 4; // quantos arquivos eulerianos gerar
        int qtdSemiEuleriano = 4; // quantos arquivos semi gerar
        int qtdNaoEuleriano = 4; // quantos arquivos não gerar

        for (int i = 1; i <= qtdEuleriano; i++) {
            gerarGrafoEuleriano(n, m, i);
        }
        for (int i = 1; i <= qtdSemiEuleriano; i++) {
            gerarGrafoSemiEuleriano(n, m, i);
        }
        for (int i = 1; i <= qtdNaoEuleriano; i++) {
            gerarGrafoNaoEuleriano(n, m, i);
        }

        System.out.println("Arquivos de grafo gerados com sucesso!");
    }

    // Gera um esqueleto base do grafo conexo para que seja possível achar todos
    // vertices na busca
    private static List<int[]> gerarGrafoBase(int n, int m) {
        Random rand = new Random();
        boolean[][] hasEdge = new boolean[n][n];
        int[] graus = new int[n];
        List<int[]> arestas = new ArrayList<>();

        // 1) Árvore base (conectividade)
        for (int i = 1; i < n; i++) {
            int u = i;
            int v = rand.nextInt(i); // conecta com alguém anterior
            arestas.add(new int[] { u, v });
            hasEdge[u][v] = hasEdge[v][u] = true;
            graus[u]++;
            graus[v]++;
        }

        // 2) Adiciona arestas
        while (arestas.size() < m) {
            int u = rand.nextInt(n);
            int v = rand.nextInt(n);
            if (u == v || hasEdge[u][v])
                continue;
            arestas.add(new int[] { u, v });
            hasEdge[u][v] = hasEdge[v][u] = true;
            graus[u]++;
            graus[v]++;
        }

        return arestas;
    }

    // Gerar Euleriano
    public static void gerarGrafoEuleriano(int n, int m, int indice) {
        List<int[]> arestas = gerarGrafoBase(n, m);
        int[] graus = calcular_Graus(n, arestas);

        List<Integer> impares = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (graus[i] % 2 != 0)
                impares.add(i);

        // Conecta ímpares fazendo seu grau virar par
        for (int i = 0; i + 1 < impares.size(); i += 2) {
            arestas.add(new int[] { impares.get(i), impares.get(i + 1) });
        }

        escreverGrafoNoArquivo("eulerian_" + n + "_" + indice + ".txt", n, arestas);
    }

    // Gerar Semi-Euleriano
    public static void gerarGrafoSemiEuleriano(int n, int m, int indice) {
        List<int[]> arestas = gerarGrafoBase(n, m);
        int[] graus = calcular_Graus(n, arestas);

        List<Integer> impares = new ArrayList<>();
        List<Integer> pares = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (graus[i] % 2 != 0) {
                impares.add(i);
            } else {
                pares.add(i);
            }
        }

        if (impares.size() == 0 && pares.size() >= 2) {
            // cria 2 ímpares
            arestas.add(new int[] { pares.get(0), pares.get(1) });
        } else {
            // ajusta até restar 2 ímpares
            while (impares.size() > 2) {
                int u = impares.remove(impares.size() - 1);
                int v = impares.remove(impares.size() - 1);
                arestas.add(new int[] { u, v });
            }
        }

        escreverGrafoNoArquivo("semi_eulerian_" + n + "_" + indice + ".txt", n, arestas);
    }

    // Gerar Não-Euleriano
    public static void gerarGrafoNaoEuleriano(int n, int m, int indice) {
        List<int[]> arestas = gerarGrafoBase(n, m);
        int[] graus = calcular_Graus(n, arestas);

        List<Integer> impares = new ArrayList<>();
        List<Integer> pares = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (graus[i] % 2 != 0) {
                impares.add(i);
            } else {
                pares.add(i);
            }
        }

        if (impares.size() == 0 && pares.size() >= 4) {
            // força 4 ímpares usando 2 pares de vértices
            arestas.add(new int[] { pares.get(0), pares.get(1) });
            arestas.add(new int[] { pares.get(2), pares.get(3) });
        } else if (impares.size() == 2 && pares.size() >= 2) {
            // passa de 2 para 4 ímpares
            arestas.add(new int[] { pares.get(0), pares.get(1) });
        }
        // se já for > 2, já é não-euleriano → não faz nada

        escreverGrafoNoArquivo("non_eulerian_" + n + "_" + indice + ".txt", n, arestas);
    }

    // Função para calcular o grau de todos os vertices
    private static int[] calcular_Graus(int n, List<int[]> arestas) {
        int[] graus = new int[n];
        for (int[] e : arestas) {
            graus[e[0]]++;
            graus[e[1]]++;
        }
        return graus;
    }

    private static void escreverGrafoNoArquivo(String filename, int n, List<int[]> arestas) {
        try (FileWriter w = new FileWriter(filename)) {
            w.write(n + "\n");
            for (int[] e : arestas) {
                w.write(e[0] + " " + e[1] + "\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }
}
