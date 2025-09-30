import java.io.*;
import java.util.*;
// Trabalho Prático TGC N01 -
// Professor: Zenilton Kleber Gonçalves do Patrocínio Junior
// Alunos: 
// - Arthur Henrique Tristão Pinto
// - 
// - 

//Testando Método Naive

public class Naive {

    static class TabelaBusca {
        public int TD;
        public int TT;
        public int pai;

        public TabelaBusca() {
            TD = TT = pai = 0;
        }
    }

    // Função pare leitura de arquivos
    public static File lerArquivo() {
        // scanner não é fechado aqui para não conflitar com a Main
        Scanner scanner = new Scanner(System.in);
        String fileName;
        File file;
        System.out.println("Digite o nome do arquivo");
        fileName = scanner.nextLine();
        file = new File(fileName);
        while (!file.exists()) {
            System.out.println("Arquivo nao encontrado!");
            System.out.println("Digite o nome do arquivo");
            fileName = scanner.nextLine();
            file = new File(fileName);
        }

        return file;
    }

    // Função para montar Lista de Adjacência a partir do arquivo txt
    public static void montarGrafo(ArrayList<Integer>[] listaAdjacente, Scanner scannerFile, int N) {
        int vertice1, vertice2;

        while (scannerFile.hasNextInt()) {
            vertice1 = scannerFile.nextInt();
            vertice2 = scannerFile.nextInt();
            listaAdjacente[vertice1].add(vertice2);
            listaAdjacente[vertice2].add(vertice1);
        }

    }

    // Função para mostrar arestas do grafo
    public static void mostrarArestas(ArrayList<Integer>[] listaAdjacente, int N) {
        for (int i = 0; i < N; i++) {
            for (int vizinho : listaAdjacente[i]) {
                System.out.println(i + " -- " + vizinho);
            }
        }
    }

    public static int buscaEmProfundidadeIterativaNaive(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int vInicial,
            int N) {

        int t = 1;
        // Começa em 1 para contar vertice inicial
        int encontrados = 1;

        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        Iterator<Integer>[] adjIterators = new Iterator[N];
        for (int i = 0; i < N; i++) {
            adjIterators[i] = grafo[i].iterator();
        }

        Stack<Integer> pilha = new Stack<>();

        t++;
        tabela[vInicial].TD = t;
        pilha.push(vInicial);

        while (!pilha.isEmpty()) {
            int v = pilha.peek();

            if (adjIterators[v].hasNext()) {
                int w = adjIterators[v].next();

                if (tabela[w].TD == 0) {
                    encontrados++;
                    t++;
                    tabela[w].TD = t;
                    tabela[w].pai = v;

                    pilha.push(w);
                }
            } else {
                pilha.pop();
                t++;
                tabela[v].TT = t;
            }
        }
        return encontrados;
    }

    // Remove aresta não dirigida (u,v) das listas
    public static void removerAresta(ArrayList<Integer>[] adj, int u, int v) {
        adj[u].remove(Integer.valueOf(v));
        adj[v].remove(Integer.valueOf(u));
    }

    // Restaura aresta não dirigida (u,v) nas listas
    public static void adicionarAresta(ArrayList<Integer>[] adj, int u, int v) {
        adj[u].add(v);
        adj[v].add(u);
    }

    // Restaura a Tabela para fazer Busca
    public static void resetTabela(TabelaBusca[] tabela, int N) {
        for (int i = 0; i < N; i++) {
            tabela[i].TD = 0;
            tabela[i].TT = 0;
            tabela[i].pai = 0;
        }
    }

    // Lista arestas únicas (u < v) para não testar duplicado
    public static ArrayList<int[]> listarArestasUnicas(ArrayList<Integer>[] g, int N) {
        ArrayList<int[]> E = new ArrayList<>();
        for (int u = 0; u < N; u++) {
            for (int v : g[u]) {
                if (u < v)
                    E.add(new int[] { u, v });
            }
        }
        return E;
    }

    // Pega um vértice de partida que d(v) >0
    static int escolherStart(ArrayList<Integer>[] g, int N) {
        for (int i = 0; i < N; i++) {
            if (!g[i].isEmpty())
                return i;
        }
        return 0; // fallback
    }

    // Método para encontrar pontes utilizando busca iterativa
    public static ArrayList<int[]> pontesNaive(ArrayList<Integer>[] grafo, TabelaBusca[] tabela, int N) {
        ArrayList<int[]> pontes = new ArrayList<>();
        ArrayList<int[]> arestas = listarArestasUnicas(grafo, N);

        int start = escolherStart(grafo, N);
        resetTabela(tabela, N);
        int verticesEncontraveis = buscaEmProfundidadeIterativaNaive(tabela, grafo, start, N);

        for (int[] e : arestas) {
            int u = e[0], v = e[1];
            resetTabela(tabela, N);
            // remove, testa, restaura
            removerAresta(grafo, u, v);

            int alcancados = buscaEmProfundidadeIterativaNaive(tabela, grafo, start, N);
            
            if (alcancados < verticesEncontraveis) {
                pontes.add(new int[] { u, v });
            }

            adicionarAresta(grafo, u, v);
        }
        return pontes;
    }

    public static void main(String[] args) throws Exception {
        int N;
        Scanner scannerFile;
        File file;

        file = lerArquivo();

        scannerFile = new Scanner(file);
        // N = Vertices,
        N = scannerFile.nextInt();

        // Inicializa a tabela para fazer a busca.
        TabelaBusca[] tabela = new TabelaBusca[N];
        for (int i = 0; i < N; i++) {
            tabela[i] = new TabelaBusca();
        }

        // Inicializa a Lista de Adjacencia.
        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N];
        for (int i = 0; i < N; i++) {
            listaAdjacente[i] = new ArrayList<Integer>();
        }

        // Monta o Grafo a partir do arquivo txt
        montarGrafo(listaAdjacente, scannerFile, N);

        // Encontrar as pontes do grafo
        ArrayList<int[]> pontes = pontesNaive(listaAdjacente, tabela, N);

        System.out.println("Pontes encontradas:");
        for (int[] e : pontes) {
            System.out.println(e[0] + " -- " + e[1]);
        }

        System.out.println("O número de pontes é " + pontes.size());

        scannerFile.close();

    }

}