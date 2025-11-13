import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TP02 {

    private static class Aresta {
        int destino;
        int peso;

        Aresta(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }

        public int getDestino() {
            return destino;
        }

        public int getPeso() {
            return peso;
        }
    }

    public static void montarGrafo(ArrayList<Aresta>[] listaAdjacente, Scanner scannerFile, int N) {
        int origem, destino, peso;

        while (scannerFile.hasNextInt()) {
            origem = scannerFile.nextInt();
            destino = scannerFile.nextInt();
            peso = scannerFile.nextInt();
            listaAdjacente[origem].add(new Aresta(destino, peso));
            listaAdjacente[destino].add(new Aresta(origem, peso)); // Grafo não-direcionado
        }
    }

    public static void imprimirGrafo(ArrayList<Aresta>[] listaAdj) {
        for (int i = 1; i < listaAdj.length; i++) {
            System.out.print(i + ": ");
            for (Aresta aresta : listaAdj[i]) {
                System.out.print(" -> (" + aresta.getDestino() + ", " + aresta.getPeso() + ") Peso:");
            }
            System.out.println();
        }
    }

    public static int[][] floydWarshall(ArrayList<Aresta>[] listaAdj, int N) {
        int[][] dist = new int[N + 1][N + 1];
        final int INF = 999999999;

        // 1. Inicializar distâncias
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = INF;
                }
            }
        }

        // 2. Para toda aresta (i, j) ∈ E(G) faça dist[i, j] ← dij
        for (int i = 1; i <= N; i++) {
            for (Aresta aresta : listaAdj[i]) {
                dist[i][aresta.getDestino()] = aresta.getPeso();
            }
        }

        // 3. Para cada possível intermediário k
        for (int k = 1; k <= N; k++) {
            for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    // Calcula o raio de um conjunto de centros
    private static int calcularRaio(int[][] dist, int[] centros, int N) {
        int raio = 0;
        for (int v = 1; v <= N; v++) {
            int menorDist = Integer.MAX_VALUE;
            for (int centro : centros) {
                menorDist = Math.min(menorDist, dist[v][centro]);
            }
            raio = Math.max(raio, menorDist);
        }
        return raio;
    }

    // Gera combinações recursivamente
    private static void gerarCombinacoes(int[][] dist, int N, int K, int[] centros, int pos, int inicio, int[] melhorSolucao, int[] melhorRaio) {
        if (pos == K) {
            int raio = calcularRaio(dist, centros, N);
            if (raio < melhorRaio[0]) {
                melhorRaio[0] = raio;
                System.arraycopy(centros, 0, melhorSolucao, 0, K);
            }
            return;
        }

        for (int i = inicio; i <= N - (K - pos) + 1; i++) {
            centros[pos] = i;
            gerarCombinacoes(dist, N, K, centros, pos + 1, i + 1, melhorSolucao, melhorRaio);
        }
    }

    // Solução Exata: testa todas as combinações de k centros
    public static int[] solucaoExata(int[][] dist, int N, int K) {
        int[] melhorSolucao = new int[K];
        int[] melhorRaio = {Integer.MAX_VALUE};
        int[] centros = new int[K];

        gerarCombinacoes(dist, N, K, centros, 0, 1, melhorSolucao, melhorRaio);

        System.out.println("Solução Exata - Raio: " + melhorRaio[0]);
        System.out.print("Centros: ");
        for (int c : melhorSolucao) System.out.print(c + " ");
        System.out.println();

        return melhorSolucao;
    }

    public static void executar(String nomeArquivo) throws FileNotFoundException {
        File file = new File(nomeArquivo);
        if (!file.exists()) {
            System.out.println("Arquivo não encontrado: " + nomeArquivo);
            return;
        }

        Scanner scannerFile = new Scanner(file);
        int N = scannerFile.nextInt();
        int distMax = scannerFile.nextInt();
        int K = scannerFile.nextInt();

        @SuppressWarnings("unchecked")
        ArrayList<Aresta>[] listaAdj = new ArrayList[N+1];
        for (int i = 1; i <= N; i++) {
            listaAdj[i] = new ArrayList<>();
        }

        montarGrafo(listaAdj, scannerFile, N);
        scannerFile.close();

        System.out.println("N=" + N + ", K=" + K);
        
        int[][] dist = floydWarshall(listaAdj, N);
        solucaoExata(dist, N, K);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo: ");
        String nomeArquivo = sc.nextLine().trim();
        executar(nomeArquivo);
        sc.close();
    }

}
