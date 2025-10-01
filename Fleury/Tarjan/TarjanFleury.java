import java.io.*;
import java.util.*;

// Trabalho Prático TGC N01 -
// Professor: Zenilton Kleber Gonçalves do Patrocínio Junior
// Alunos:
// - Arthur Henrique Tristão Pinto
// - Samuel Correia Pedrosa
// - 

public class TarjanFleury {

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

    // Função para leitura de arquivo
    public static File lerArquivo() {
        try (// scanner não é fechado aqui para não conflitar com a Main
        Scanner scanner = new Scanner(System.in)) {
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

    //Função que garante que todos os vértices sejam visitados (não usada diretamente)
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

    // --- Tarjan iterativo para pontes (mantive sua versão original) ---
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
                        //println opcional: System.out.println("Ponte encontrada: " + p + " - " + u);
                    }
                }
            }
        }
    }

    //remover/restaurar arestas
    public static void removerAresta(ArrayList<Integer>[] grafo, int u, int v) {
        grafo[u].remove((Integer) v);
        grafo[v].remove((Integer) u);
    }

    public static void restaurarAresta(ArrayList<Integer>[] grafo, int u, int v) {
        grafo[u].add(v);
        grafo[v].add(u);
        // manter ordem lexicográfica 
        Collections.sort(grafo[u]);
        Collections.sort(grafo[v]);
    }

    // Conta arestas (cada aresta contada duas vezes nas listas)
    public static int contarArestas(ArrayList<Integer>[] grafo, int N) {
        int soma = 0;
        for (int i = 0; i <= N; i++) {
            soma += grafo[i].size();
        }
        return soma / 2;
    }

    //Testa se aresta (u,v) é ponte recriando a tabela e rodando Tarjan a partir de u 
    public static boolean ehPonte(ArrayList<Integer>[] grafo, int u, int v, int N) throws IOException {
        // Remove temporariamente
        removerAresta(grafo, u, v);

        // Cria nova tabela limpadinha
        TabelaBusca[] tabelaTemp = new TabelaBusca[N + 1];
        for (int i = 0; i <= N; i++) {
            tabelaTemp[i] = new TabelaBusca();
        }

        // Roda Tarjan a partir de u
        dfsIterativaPontesTarjan(tabelaTemp, grafo, u, N);

        // Se v NÃO foi visitado, a remoção desconectou v -> era ponte
        boolean isPonte = !tabelaTemp[v].visitado;

        // Restaura a aresta
        restaurarAresta(grafo, u, v);

        return isPonte;
    }

    public static boolean temCicloEuleriano(ArrayList<Integer>[] grafo, int N) throws IOException{
                for (int i = 0; i < N; i++) {
                    if (grafo[i].size() % 2 != 0) { //se os todos os graus forem pares então há um ciclo euleriano
                        System.out.print("Não tem ciclo Euleriano");
                        return false;
                     }
                 }
                 return true;
    }


    // --- Algoritmo de Fleury que inicia sempre em 0 e imprime a trilha/ciclo Euleriano ---
    public static void fleury(ArrayList<Integer>[] grafo, int N) throws IOException {

        int totalArestas = contarArestas(grafo, N);
        if (totalArestas == 0) {
            System.out.println("Grafo sem arestas.");
            return;
        }
        
        int u = 0; // começa sempre no vértice 0 conforme pedido
        System.out.print("Ciclo/Trilha Euleriana: " + u);
        
              
            

        while (totalArestas > 0) {
            if (grafo[u].isEmpty()) {
                // Caso improvável em grafo conectado inicial (mas para segurança),
                // procura próximo vértice com alguma aresta para continuar (não deveria ocorrer em grafos Eulerianos válidos)
                boolean found = false;
                for (int i = 0; i <= N; i++) {
                    if (!grafo[i].isEmpty()) {
                        u = i;
                        found = true;
                        break;
                    }
                }
                if (!found) break; // acabou as arestas
            }
            
            // Escolhe um vizinho: preferimos um que NÃO seja ponte se possível
            int escolhido = -1;

            // Tenta primeiro encontrar um vizinho que não seja ponte (se houver mais de 1 vizinho)
            if (grafo[u].size() == 1) {
                escolhido = grafo[u].get(0);
            } else {
                // há >= 2 vizinhos: tentar achar um que não seja ponte
                for (int v : new ArrayList<>(grafo[u])) { // cópia para evitar ConcurrentModification
                    if (!ehPonte(grafo, u, v, N)) {
                        escolhido = v;
                        break;
                    }
                }
                if (escolhido == -1) {
                    // todos são pontes (ou não encontramos não-ponte) -> pega o primeiro
                    escolhido = grafo[u].get(0);
                }
            }

            // Usa a aresta u-escolhido
            System.out.print(" -> " + escolhido);
            removerAresta(grafo, u, escolhido);
            totalArestas--;
            u = escolhido;
        }
        System.out.println();
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
        if(temCicloEuleriano(listaAdjacente, N)== true){ //Verifica primeiro se tem um Ciclo Euleriano
            fleury(listaAdjacente, N);                   //Se há um ciclo, então encontrar com fleury
        }
        // Executa Fleury (inicia no vértice 0)

        scannerFile.close();
    }

}

