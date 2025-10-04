import java.io.*;
import java.util.*;

// Algoritmo de Fleury usando Tarjan para checar pontes
public class TarjanFleury {

    static class TabelaBusca {
        public int TD, TT, pai;
        public TabelaBusca() { TD = TT = pai = 0; }
    }

    public static File lerArquivo() {
        Scanner scanner = new Scanner(System.in);
        String fileName;
        File file;
        System.out.println("Digite o nome do arquivo:");
        fileName = scanner.nextLine();
        file = new File(fileName);
        while (!file.exists()) {
            System.out.println("Arquivo nao encontrado! Digite novamente:");
            fileName = scanner.nextLine();
            file = new File(fileName);
        }
        return file;
    }

    // Monta grafo a partir do arquivo
    public static void montarGrafo(ArrayList<Integer>[] listaAdjacente, Scanner scannerFile, int N) {
        int v1, v2;
        while (scannerFile.hasNextInt()) {
            v1 = scannerFile.nextInt();
            v2 = scannerFile.nextInt();
            listaAdjacente[v1].add(v2);
            listaAdjacente[v2].add(v1);
        }
    }

    public static void removerAresta(ArrayList<Integer>[] adj, int u, int v) {
        adj[u].remove(Integer.valueOf(v));
        adj[v].remove(Integer.valueOf(u));
    }

    public static void adicionarAresta(ArrayList<Integer>[] adj, int u, int v) {
        adj[u].add(v);
        adj[v].add(u);
    }

    // Tarjan para verificar pontes
    static int tempo;
    static boolean[] visitado;
    static int[] tin, low;

    private static void dfsTarjan(int v, int p, ArrayList<Integer>[] g) {
        visitado[v] = true;
        tin[v] = low[v] = ++tempo;
        for (int to : g[v]) {
            if (to == p) continue;
            if (visitado[to]) {
                low[v] = Math.min(low[v], tin[to]);
            } else {
                dfsTarjan(to, v, g);
                low[v] = Math.min(low[v], low[to]);
            }
        }
    }

    private static boolean ehConexo(ArrayList<Integer>[] g, int N) {
        Arrays.fill(visitado, false);
        tempo = 0;
        for (int i = 0; i < N; i++) {
            if (!g[i].isEmpty()) {
                dfsTarjan(i, -1, g);
                break;
            }
        }
        for (int i = 0; i < N; i++) {
            if (!g[i].isEmpty() && !visitado[i]) return false;
        }
        return true;
    }

    public static boolean ehPonte(ArrayList<Integer>[] g, int u, int v, int N) {
        removerAresta(g, u, v);
        boolean conexo = ehConexo(g, N);
        adicionarAresta(g, u, v);
        return !conexo;
    }

    // Encontra o vértice inicial como no Naive
    private static int encontrarVerticeInicial(ArrayList<Integer>[] grafo, int N) {
        int vImpar = -1, vQualquer = -1;
        for (int i = 0; i < N; i++) {
            int d = grafo[i].size();
            if (d > 0) {
                if (vQualquer == -1) vQualquer = i;
                if (d % 2 != 0) { vImpar = i; break; }
            }
        }
        return (vImpar != -1) ? vImpar : vQualquer;
    }

    // Conta vértices ímpares
    private static int contarGrausImpares(ArrayList<Integer>[] grafo, int N) {
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (grafo[i].size() % 2 != 0) count++;
        }
        return count;
    }

    // Clona o grafo
    private static ArrayList<Integer>[] clonarGrafo(ArrayList<Integer>[] original, int N) {
        ArrayList<Integer>[] clone = new ArrayList[N];
        for (int i = 0; i < N; i++) {
            clone[i] = new ArrayList<>(original[i]);
        }
        return clone;
    }

    // Algoritmo de Fleury usando Tarjan
    public static ArrayList<Integer> algoritmoFleury(ArrayList<Integer>[] grafoOriginal, int N) {
        int impares = contarGrausImpares(grafoOriginal, N);
        if (impares > 2) {
            System.out.println("Erro: O grafo possui " + impares + " vértices de grau ímpar. Não há caminho ou circuito Euleriano.");
            return new ArrayList<>();
        }

        ArrayList<Integer>[] G = clonarGrafo(grafoOriginal, N);
        int vAtual = encontrarVerticeInicial(G, N);
        if (vAtual == -1) {
            System.out.println("O grafo está vazio.");
            return new ArrayList<>();
        }

        ArrayList<Integer> caminho = new ArrayList<>();
        caminho.add(vAtual);

        int arestasRestantes = 0;
        for (int i = 0; i < N; i++) arestasRestantes += G[i].size();
        arestasRestantes /= 2;

        while (arestasRestantes > 0) {
            int v = vAtual, wProximo = -1;
            ArrayList<Integer> vizinhos = new ArrayList<>(G[v]);

            if (G[v].size() > 1) {
                for (int w : vizinhos) {
                    if (!ehPonte(G, v, w, N)) {
                        wProximo = w;
                        break;
                    }
                }
                if (wProximo == -1) wProximo = vizinhos.get(0);
            } else {
                wProximo = vizinhos.get(0);
            }

            removerAresta(G, v, wProximo);
            caminho.add(wProximo);
            vAtual = wProximo;
            arestasRestantes--;
        }
        return caminho;
    }

    public static void main(String[] args) throws Exception {
        int N;
        File file = lerArquivo();
        Scanner scannerFile = new Scanner(file);

        N = scannerFile.nextInt();

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N];
        for (int i = 0; i < N; i++) listaAdjacente[i] = new ArrayList<>();

        montarGrafo(listaAdjacente, scannerFile, N);

        visitado = new boolean[N];
        tin = new int[N];
        low = new int[N];

        ArrayList<Integer> caminhoEuleriano = algoritmoFleury(listaAdjacente, N);

        if (!caminhoEuleriano.isEmpty()) {
            System.out.println("\n--- Resultado do Algoritmo de Fleury (Tarjan) ---");
            for (int i = 0; i < caminhoEuleriano.size(); i++) {
                System.out.print(caminhoEuleriano.get(i));
                if (i < caminhoEuleriano.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }

        scannerFile.close();
    }
}
