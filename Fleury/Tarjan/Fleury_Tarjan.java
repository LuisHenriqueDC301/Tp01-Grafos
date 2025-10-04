import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class Fleury_Tarjan {

    static class TabelaBusca {
        public int TD, TT, pai, low;

        public TabelaBusca() {
            TD = TT = pai = low = 0;
        }
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

    // v1 e v2 representam a aresta em analise
    public static boolean buscaEmProfundidadeIterativaTarjan(TabelaBusca[] tabela, ArrayList<Integer>[] grafo,
        int vInicial, int N, int v1, int v2) {
        boolean ehPonte = false;
        int t = 0;

        // Limpar a tabela de busca
        for (int i = 0; i < N; i++) {
            tabela[i].TD = tabela[i].TT = tabela[i].low = 0;
            tabela[i].pai = -1;
        }

        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        Iterator<Integer>[] adjIterators = new Iterator[N];
        for (int i = 0; i < N; i++) {
            adjIterators[i] = grafo[i].iterator();
        }

        Stack<Integer> pilha = new Stack<>();

        t++;
        tabela[vInicial].TD = t;
        tabela[vInicial].low = tabela[vInicial].TD;
        tabela[vInicial].pai = -1;
        pilha.push(vInicial);

        while (!pilha.isEmpty()) {
            int v = pilha.peek();

            if (adjIterators[v].hasNext()) {
                int w = adjIterators[v].next();
                // Aresta de arvore
                if (tabela[w].TD == 0) {
                    // Visita o w
                    tabela[w].pai = v;
                    tabela[w].TD = ++t;
                    tabela[w].low = tabela[w].TD;
                    pilha.push(w);
                } else if (tabela[w].TT == 0 && w != tabela[v].pai) {
                    // Aresta de retorno (v -> w): atualiza low[v] com TD[w]
                    tabela[v].low = Math.min(tabela[v].low, tabela[w].TD);
                }

            } else {
                // Acabou de processar o v; Atualiza sem low
                pilha.pop();
                t++;
                tabela[v].TT = t;
                int pai = tabela[v].pai;
                if (pai != -1) {
                    tabela[pai].low = Math.min(tabela[pai].low, tabela[v].low);
                }

            }
        }

        if (tabela[v2].pai == v1) {
            ehPonte = (tabela[v2].low > tabela[v1].TD);
        } else if (tabela[v1].pai == v2) {
            ehPonte = (tabela[v1].low > tabela[v2].TD);
        }

        return ehPonte;
    }

    public static void main(String[] args) throws Exception {
        int N;
        File file = lerArquivo();
        Scanner scannerFile = new Scanner(file);

        N = scannerFile.nextInt();

        // Inicializa a tabela para fazer a busca.
        TabelaBusca[] tabela = new TabelaBusca[N];
        for (int i = 0; i < N; i++) {
            tabela[i] = new TabelaBusca();
        }

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N];
        for (int i = 0; i < N; i++)
            listaAdjacente[i] = new ArrayList<>();

        montarGrafo(listaAdjacente, scannerFile, N);

        scannerFile.close();
    }

}
