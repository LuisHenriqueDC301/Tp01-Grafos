
import java.io.*;
import java.util.*;
// Trabalho Prático TGC N01 -
// Professor: Zenilton Kleber Gonçalves do Patrocínio Junior
// Alunos: 
// - Arthur Henrique Tristão Pinto
// - Luis Henrique Ferreira Costa
// - Samuel Correia Pedrosa

public class Fleury_Naive {

    static class TabelaBusca {
        public int TD;
        public int TT;
        public int pai;

        public TabelaBusca() {
            TD = TT = pai = 0;
        }
    }

    // Função pare leitura de arquivos
    public static File lerArquivo(String fileName) {
        // scanner não é fechado aqui para não conflitar com a Main
        Scanner scanner = new Scanner(System.in);
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

    public static int buscaEmProfundidadeIterativaNaive(TabelaBusca[] tabela, ArrayList<Integer>[] grafo, int vInicial,
            int N) {

        resetTabela(tabela, N);
        int t = 0;
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

    // Método Naive para ver se aresta {u,v} é ponte
    public static boolean ehPonteNaive(ArrayList<Integer>[] grafo, TabelaBusca[] tabela, int vInicial, int N, int u,
            int v) {
        boolean ehPonte = false;
        int alcancaveis = buscaEmProfundidadeIterativaNaive(tabela, grafo, vInicial, N);

        // remove, testa, restaura
        removerAresta(grafo, u, v);

        int alcancados = buscaEmProfundidadeIterativaNaive(tabela, grafo, vInicial, N);

        if (alcancados < alcancaveis) {
            ehPonte = true;
        }

        adicionarAresta(grafo, u, v);
        return ehPonte;

    }

    public static ArrayList<Integer> algoritmoFleury(ArrayList<Integer>[] grafoOriginal, TabelaBusca[] tabela, int N) {

        // 1. Verificação da Condição Euleriana (Passo 1 do seu algoritmo)
        int impares = contarGrausImpares(grafoOriginal, N);
        if (impares > 2) {
            System.out.println("Erro: O grafo possui " + impares
                    + " vértices de grau ímpar. Não há caminho ou circuito Euleriano.");
            return new ArrayList<>();
        }

        // 2. Inicializar grafo auxiliar G'
        ArrayList<Integer>[] G_prime = clonarGrafo(grafoOriginal, N);

        // 3. Selecionar vértice inicial v
        // Escolhe um ímpar, se houver, conforme a regra de Fleury.
        int v_atual = encontrarVerticeInicial(G_prime, N);
        if (v_atual == -1) {
            System.out.println("O grafo está vazio.");
            return new ArrayList<>();
        }

        ArrayList<Integer> caminho = new ArrayList<>();
        caminho.add(v_atual);

        // Contagem inicial de arestas (cada aresta é contada duas vezes na soma dos
        // graus)
        int arestas_restantes = 0;
        for (int i = 0; i < N; i++) {
            arestas_restantes += grau(G_prime, i);
        }
        arestas_restantes /= 2;

        // 4. enquanto E' ≠ ∅ efetuar
        while (arestas_restantes > 0) {
            int v = v_atual;
            int w_proximo = -1;

            // Copia a lista de vizinhos para iterar com segurança
            ArrayList<Integer> vizinhos_copia = new ArrayList<>(G_prime[v]);

            // a. se d(v) > 1
            if (grau(G_prime, v) > 1) {
                // Selecionar aresta {v, w} que não seja ponte em G'
                for (int w : vizinhos_copia) {
                    // Usando seu método de checagem de ponte no grafo atual G'

                    if (!ehPonteNaive(G_prime, tabela, v_atual, N, v, w)) {
                        w_proximo = w;
                        break; // Selecionada aresta não-ponte
                    }
                }

                // Se todas as arestas são pontes, é o último recurso.
                // Em um grafo Euleriano válido, isto só deveria acontecer se d(v)=1.
                // Para garantir o término, se w_proximo == -1, seleciona a primeira aresta
                // restante.
                if (w_proximo == -1) {
                    w_proximo = vizinhos_copia.get(0);
                }
            }
            // b. senão (d(v) == 1)
            else {
                if (grau(G_prime, v) == 1) {
                    w_proximo = vizinhos_copia.get(0);
                }
                // Selecionar a única aresta {v, w} disponível em G'

            }

            // Checagem de segurança
            if (w_proximo == -1) {
                // O loop terminou prematuramente (grafo desconectado, erro, etc.)
                break;
            }

            // c. v <- w; E' <- E' - {v, w}
            removerAresta(G_prime, v, w_proximo);
            caminho.add(w_proximo);
            v_atual = w_proximo;
            arestas_restantes--;
        }

        return caminho;
    }

    // Retorna o grau do vértice v no grafo atual
    private static int grau(ArrayList<Integer>[] grafo, int v) {
        return grafo[v].size();
    }

    // Encontra um vértice de partida que tem d(v) > 0.
    // Adaptação para o Fleury: Se o grafo tem vértices ímpares, deve retornar um
    // deles.
    private static int encontrarVerticeInicial(ArrayList<Integer>[] grafo, int N) {
        int v_impar = -1;
        int v_qualquer = -1;

        for (int i = 0; i < N; i++) {
            int d = grau(grafo, i);
            if (d > 0) {
                if (v_qualquer == -1)
                    v_qualquer = i;
                if (d % 2 != 0) {
                    v_impar = i;
                    break; // Prioridade: ímpar
                }
            }
        }

        if (v_impar != -1)
            return v_impar;
        if (v_qualquer != -1)
            return v_qualquer;
        return -1; // Grafo vazio ou sem arestas.
    }

    // Conta o número de vértices com grau ímpar
    private static int contarGrausImpares(ArrayList<Integer>[] grafo, int N) {
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (grau(grafo, i) % 2 != 0) {
                count++;
            }
        }
        return count;
    }

    // Clona o grafo para trabalhar no grafo auxiliar G'
    private static ArrayList<Integer>[] clonarGrafo(ArrayList<Integer>[] original, int N) {
        ArrayList<Integer>[] clone = new ArrayList[N];
        for (int i = 0; i < N; i++) {
            clone[i] = new ArrayList<>(original[i]);
        }
        return clone;
    }

    public static void executar(String fileName) throws FileNotFoundException {
        int N;
        Scanner scannerFile;
        File file;

        file = new File(fileName);

        scannerFile = new Scanner(file);
        // N = Vertices,
        N = scannerFile.nextInt();

        // Inicializa a tabela para fazer a busca.
        TabelaBusca[] tabela = new TabelaBusca[N];
        for (int i = 0; i < N; i++) {
            tabela[i] = new TabelaBusca();
        }

        // Inicializa a Lista de Adjacencia.
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] listaAdjacente = (ArrayList<Integer>[]) new ArrayList[N];
        for (int i = 0; i < N; i++) {
            listaAdjacente[i] = new ArrayList<Integer>();
        }

        // Monta o Grafo a partir do arquivo txt
        montarGrafo(listaAdjacente, scannerFile, N);
        ArrayList<Integer> caminhoEuleriano = algoritmoFleury(listaAdjacente, tabela, N);

        if (!caminhoEuleriano.isEmpty()) {
            System.out.println("\n--- Resultado do Algoritmo de Fleury-Naive");
            System.out.print("Caminho/Circuito Euleriano: ");

            // Formata a saída do caminho
            for (int i = 0; i < caminhoEuleriano.size(); i++) {
                System.out.print(caminhoEuleriano.get(i));
                if (i < caminhoEuleriano.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            scannerFile.close();
        }
    }

    public static void main(String[] args) throws Exception {

    }
}