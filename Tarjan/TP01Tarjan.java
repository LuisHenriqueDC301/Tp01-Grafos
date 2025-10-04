package TP1;

import java.io.*;
import java.util.*;

// Trabalho Prático TGC N01 -
// Professor: Zenilton Kleber Gonçalves do Patrocínio Junior
// Alunos: 
// - Arthur Henrique Tristão Pinto
// - 
// - 

public class TP01Tarjan {

    static class TabelaBusca {
        public int TD;
        public int TT;
        public int pai;
        public int low;
        public boolean visitado;

        public TabelaBusca() {
            TD = TT = pai = low = 0;
            visitado = false;
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
        // Ordena pra ordem lexografica
        for (int i = 0; i <= N; i++) {
            Collections.sort(listaAdjacente[i]);
        }
    }

    // Função para mostrar arestas do grafo
    public static void mostrarArestas(ArrayList<Integer>[] listaAdjacente, int N) {
        for (int i = 0; i <= N; i++) {
            for (int vizinho : listaAdjacente[i]) {
                System.out.println(i + " -- " + vizinho);
            }
        }
    }

    //Função que garante que todos os vértices sejam visitados
    public static void buscaEmProfundidade(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int N) throws IOException {

        // Loop para garantir que todos os vértices sejam visitados
        for (int v = 0; v <= N; v++) {
            if (tabela[v].TD == 0) {
                buscaEmProfundidadeIterativa(tabela, grafo, v, N);
            }
        }
    }

    
    public static void buscaEmProfundidadeIterativa(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int vInicial,int N) throws IOException {
        int t = 1;

        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        Iterator<Integer>[] adjIterators = new Iterator[N + 1];
        for (int i = 0; i <= N; i++) {
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
                    t++;
                    tabela[w].TD = t;
                    tabela[w].pai = v;

                    System.out.println("Aresta de arvore: " + v + "-" + w);

                    pilha.push(w);
                }
            } else {
                pilha.pop();
                t++;
                tabela[v].TT = t;
            }
        }
    }


    public static void dfsIterativaPontesTarjan(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int vInicial, int N) throws IOException {
    int t = 0;

    Stack<Integer> stack = new Stack<>();
    int[] index = new int[N + 1];       // índice do próximo vizinho a explorar para cada vértice
    Arrays.fill(index, 0);

    stack.push(vInicial);
    tabela[vInicial].visitado = true;
    tabela[vInicial].TD = tabela[vInicial].low = ++t;
    tabela[vInicial].pai = -1;  // raiz não tem pai

    while (!stack.isEmpty()) {
        int u = stack.peek();

        if (index[u] < grafo[u].size()) {
            int v = grafo[u].get(index[u]);
            index[u]++;

            if (!tabela[v].visitado) {
                // vizinho não visitado: aresta de árvore
                tabela[v].visitado = true;
                tabela[v].pai = u;
                tabela[v].TD = tabela[v].low = ++t;
                stack.push(v);
            } else if (v != tabela[u].pai) {
                // aresta de retorno
                tabela[u].low = Math.min(tabela[u].low, tabela[v].TD);
            }
        } else {
            // todos os vizinhos explorados, volta para o pai
            stack.pop();
            if (tabela[u].pai != -1) {
                int p = tabela[u].pai;
                tabela[p].low = Math.min(tabela[p].low, tabela[u].low);

                // condição para ponte
                if (tabela[u].low > tabela[p].TD) {
                    System.out.println("Ponte encontrada: " + p + " - " + u);
                }
            }
        }
    }
}



    public static void main(String[] args) throws Exception {
        int N;
        Scanner scannerFile;
        File file;

        file = lerArquivo();

        scannerFile = new Scanner(file);
        // N = Vertices,
        N = scannerFile.nextInt();

        // inicializa a tabela para fazer a busca.
        TabelaBusca[] tabela = new TabelaBusca[N + 1];
        for (int i = 0; i <= N; i++) {
            tabela[i] = new TabelaBusca();
        }

        // inicializa a Lista de Adjacencia.
        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N + 1];
        for (int i = 0; i <= N; i++) {
            listaAdjacente[i] = new ArrayList<Integer>();
        }

        // monta o Grafo a partir do arquivo txt
        montarGrafo(listaAdjacente, scannerFile, N);
        // mostrarArestas(listaAdjacente, N);
        // buscaEmProfundidade(tabela, listaAdjacente, N);
        dfsIterativaPontesTarjan(tabela, listaAdjacente, 0, N);

        scannerFile.close();

    }

}
