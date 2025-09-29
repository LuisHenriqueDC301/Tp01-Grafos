import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

// Implementacao 02 TGC - Base 1
// Aluno: Arthur Henrique Tristao Pinto
// Adaptado para indices a partir de 1
//Busca Iterativa baseada no código de Robert Sedgewick e Kevin Wayne.
//Link do código base: https://algs4.cs.princeton.edu/41graph/NonrecursiveDFS.java.html

class TabelaBusca {
    public int TD;
    public int TT;
    public int pai;

    public TabelaBusca() {
        TD = TT = pai = 0;
    }
}

public class ImplementacaoBuscaProfundidade {
    // contador global
    static int t = 0;

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

    public static void buscaEmProfundidade(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int N) throws IOException {

        // Loop para garantir que todos os vértices sejam visitados
        for (int v = 1; v <= N; v++) {
            if (tabela[v].TD == 0) {
                buscaEmProfundidadeIterativa(tabela, grafo, v, N);
            }
        }
    }

    public static void buscaEmProfundidadeIterativa(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int vInicial,int N) throws IOException {
        
        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        Iterator<Integer>[] adjIterators = new Iterator[N + 1];
        for (int i = 1; i <= N; i++) {
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

    //Confere se o intervalo do w está no intrevalo de v
    public static boolean contem(TabelaBusca[] tabela, int v, int w) {
        return (tabela[v].TD < tabela[w].TD && tabela[v].TT > tabela[w].TT);
    }

    public static void classificarSucessores(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int v) {
        for (Integer w : grafo[v]) {
            if (tabela[w].pai == v) {
                System.out.println("Aresta de árvore:" + v + "-" + w);
            } else if (contem(tabela, v, w)) {
                System.out.println("Aresta de avanço:" + v + "-" + w);
            } else if (contem(tabela, w, v)) {
                System.out.println("Aresta de retorno:" + v + "-" + w);
            } else {
                System.out.println("Aresta de cruzamento:" + v + "-" + w);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int N;
        Scanner scannerInput, scannerFile;
        File file;

        file = lerArquivo();

        scannerFile = new Scanner(file);
        // N = Vertices, 
        N = scannerFile.nextInt();
        // Ler o nº de arestas
        scannerFile.nextInt();

        // Inicializa a tabela para fazer a busca.
        TabelaBusca[] tabela = new TabelaBusca[N + 1];
        for (int i = 1; i <= N; i++) {
            tabela[i] = new TabelaBusca();
        }

        // Inicializa a Lista de Adjacencia.
        // Tirar aviso do compilador
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            listaAdjacente[i] = new ArrayList<Integer>();
        }
        int vertice1, vertice2;
        // Preenche a lista com os pares de vertices
        while (scannerFile.hasNextInt()) {
            vertice1 = scannerFile.nextInt();
            vertice2 = scannerFile.nextInt();
            listaAdjacente[vertice1].add(vertice2);
        }
        // Ordena pra ordem lexografica
        for (int i = 1; i <= N; i++) {
            Collections.sort(listaAdjacente[i]);
        }
        scannerFile.close();

        buscaEmProfundidade(tabela, listaAdjacente, N);
        System.out.println("BUSCA FINALIZADA");

        scannerInput = new Scanner(System.in);
        int verticeEscolhido;

        while (true) {
            System.out.println("\nDigite o número do Vertice ou 0 para sair");

            // Verifica se a próxima entrada é um número inteiro
            if (scannerInput.hasNextInt()) {
                verticeEscolhido = scannerInput.nextInt();

                if (verticeEscolhido == 0) {
                    break;
                }

                // Valida o vértice
                if (verticeEscolhido >= 1 && verticeEscolhido <= N) {
                    classificarSucessores(tabela, listaAdjacente, verticeEscolhido);
                } else {
                    System.out.println("Vertice Inválido. Digite um número entre 1 e " + N + ".");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scannerInput.next();
            }
        }
        scannerInput.close();
    }
}