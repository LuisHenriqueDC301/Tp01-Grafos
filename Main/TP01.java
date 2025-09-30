import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.*;
// Trabalho Prático TGC N01 -
// Professor: Zenilton Kleber Gonçalves do Patrocínio Junior
// Alunos: 
// - Arthur Henrique Tristão Pinto
// - 
// - 

public class TP01 {

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

    // Função para montar Lista de Adjacência para N-Direcionado a partir do arquivo
    // txt
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

    // Função que garante que todos os vértices sejam visitados
    public static void buscaEmProfundidade(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int N) throws IOException {

        // Loop para garantir que todos os vértices sejam visitados
        for (int v = 0; v <= N; v++) {
            if (tabela[v].TD == 0) {
                buscaEmProfundidadeIterativa(tabela, grafo, v, N);
            }
        }
    }

    public static void buscaEmProfundidadeIterativa(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int vInicial,
            int N) throws IOException {
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

    public static void main(String[] args) throws Exception {
        int N;
        Scanner scannerFile;
        File file;

        file = lerArquivo();

        scannerFile = new Scanner(file);
        // N = Vertices,
        N = scannerFile.nextInt();

        // Inicializa a tabela para fazer a busca.
        TabelaBusca[] tabela = new TabelaBusca[N + 1];
        for (int i = 0; i <= N; i++) {
            tabela[i] = new TabelaBusca();
        }

        // Inicializa a Lista de Adjacencia.
        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N + 1];
        for (int i = 0; i <= N; i++) {
            listaAdjacente[i] = new ArrayList<Integer>();
        }

        // Monta o Grafo a partir do arquivo txt
        montarGrafo(listaAdjacente, scannerFile, N);
        mostrarArestas(listaAdjacente, N);
        buscaEmProfundidade(tabela, listaAdjacente, N);

        scannerFile.close();

    }

}