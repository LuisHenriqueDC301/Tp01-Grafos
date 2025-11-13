import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TP02 {

    // ==================== CLASSE GRAFO ====================

    static class Grafo {
        private int numVertices;
        private ArrayList<Aresta>[] adjacencias;

        @SuppressWarnings("unchecked")
        public Grafo(int numVertices) {
            this.numVertices = numVertices;
            this.adjacencias = new ArrayList[numVertices + 1];
            for (int i = 1; i <= numVertices; i++) {
                adjacencias[i] = new ArrayList<>();
            }
        }

        public void adicionarAresta(int origem, int destino, int peso) {
            adjacencias[origem].add(new Aresta(destino, peso));
            adjacencias[destino].add(new Aresta(origem, peso));
        }

        public int getNumVertices() {
            return numVertices;
        }

        public ArrayList<Aresta> getAdjacencias(int vertice) {
            return adjacencias[vertice];
        }

        static class Aresta {
            int destino;
            int peso;

            Aresta(int destino, int peso) {
                this.destino = destino;
                this.peso = peso;
            }
        }
    }

    // ==================== CLASSE INSTÂNCIA ====================

    static class Instancia {
        private String nomeArquivo;
        private int numVertices;
        private int numCentros;
        private Grafo grafo;

        public Instancia(String nomeArquivo) throws FileNotFoundException {
            this.nomeArquivo = nomeArquivo;
            carregar();
        }

        private void carregar() throws FileNotFoundException {
            File file = new File(nomeArquivo);
            if (!file.exists()) {
                throw new FileNotFoundException("Arquivo não encontrado: " + nomeArquivo);
            }

            Scanner scanner = new Scanner(file);
            this.numVertices = scanner.nextInt();
            int numArestas = scanner.nextInt();
            this.numCentros = scanner.nextInt();

            this.grafo = new Grafo(numVertices);

            while (scanner.hasNextInt()) {
                int origem = scanner.nextInt();
                int destino = scanner.nextInt();
                int peso = scanner.nextInt();
                grafo.adicionarAresta(origem, destino, peso);
            }

            scanner.close();
        }

        public String getNomeArquivo() {
            return nomeArquivo;
        }

        public int getNumVertices() {
            return numVertices;
        }

        public int getNumCentros() {
            return numCentros;
        }

        public Grafo getGrafo() {
            return grafo;
        }
    }

    // ==================== CLASSE MATRIZ DE DISTÂNCIAS ====================

    static class MatrizDistancias {
        private static final int INF = 999999999;
        private int[][] dist;
        private int numVertices;

        public MatrizDistancias(Grafo grafo) {
            this.numVertices = grafo.getNumVertices();
            this.dist = new int[numVertices + 1][numVertices + 1];
            calcularFloydWarshall(grafo);
        }

        private void calcularFloydWarshall(Grafo grafo) {
            // Inicializar
            for (int i = 1; i <= numVertices; i++) {
                for (int j = 1; j <= numVertices; j++) {
                    dist[i][j] = (i == j) ? 0 : INF;
                }
            }

            // Arestas diretas
            for (int i = 1; i <= numVertices; i++) {
                for (Grafo.Aresta aresta : grafo.getAdjacencias(i)) {
                    dist[i][aresta.destino] = aresta.peso;
                }
            }

            // Floyd-Warshall
            for (int k = 1; k <= numVertices; k++) {
                for (int i = 1; i <= numVertices; i++) {
                    for (int j = 1; j <= numVertices; j++) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }

        public int getDistancia(int i, int j) {
            return dist[i][j];
        }

        public int[][] getMatriz() {
            return dist;
        }
    }

    // ==================== CLASSE SOLUÇÃO ====================

    static class Solucao {
        private String tipo;
        private int[] centros;
        private int raio;
        private long tempoExecucao;

        public Solucao(String tipo, int[] centros, int raio, long tempoExecucao) {
            this.tipo = tipo;
            this.centros = centros;
            this.raio = raio;
            this.tempoExecucao = tempoExecucao;
        }

        public int getRaio() {
            return raio;
        }

        public long getTempoExecucao() {
            return tempoExecucao;
        }

        public void imprimir() {
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║  Solução " + String.format("%-44s", tipo) + "║");
            System.out.println("╠════════════════════════════════════════════════════════╣");
            System.out.println("║  Raio: " + String.format("%-47d", raio) + "║");
            System.out.print("║  Centros: ");
            StringBuilder centrosStr = new StringBuilder();
            for (int c : centros) {
                centrosStr.append(c).append(" ");
            }
            System.out.println(String.format("%-42s", centrosStr.toString()) + "║");
            System.out.println("║  Tempo: " + String.format("%-46s", tempoExecucao + "ms") + "║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
        }
    }

    // ==================== CLASSE SOLVER EXATO ====================

    static class SolverExato {
        private MatrizDistancias matrizDist;
        private int numVertices;
        private int numCentros;

        public SolverExato(MatrizDistancias matrizDist, int numVertices, int numCentros) {
            this.matrizDist = matrizDist;
            this.numVertices = numVertices;
            this.numCentros = numCentros;
        }

        public Solucao resolver() {
            int[] melhorSolucao = new int[numCentros];
            int[] melhorRaio = {Integer.MAX_VALUE};
            int[] centros = new int[numCentros];

            long inicio = System.currentTimeMillis();
            gerarCombinacoes(centros, 0, 1, melhorSolucao, melhorRaio);
            long tempo = System.currentTimeMillis() - inicio;

            return new Solucao("Exata", melhorSolucao, melhorRaio[0], tempo);
        }

        private void gerarCombinacoes(int[] centros, int pos, int inicio, 
                                      int[] melhorSolucao, int[] melhorRaio) {
            if (pos == numCentros) {
                int raio = calcularRaio(centros);
                if (raio < melhorRaio[0]) {
                    melhorRaio[0] = raio;
                    System.arraycopy(centros, 0, melhorSolucao, 0, numCentros);
                }
                return;
            }

            for (int i = inicio; i <= numVertices - (numCentros - pos) + 1; i++) {
                centros[pos] = i;
                gerarCombinacoes(centros, pos + 1, i + 1, melhorSolucao, melhorRaio);
            }
        }

        private int calcularRaio(int[] centros) {
            int raio = 0;
            for (int v = 1; v <= numVertices; v++) {
                int menorDist = Integer.MAX_VALUE;
                for (int centro : centros) {
                    menorDist = Math.min(menorDist, matrizDist.getDistancia(v, centro));
                }
                raio = Math.max(raio, menorDist);
            }
            return raio;
        }
    }

    // ==================== CLASSE SOLVER APROXIMADO ====================

    static class SolverAproximado {
        private MatrizDistancias matrizDist;
        private int numVertices;
        private int numCentros;

        public SolverAproximado(MatrizDistancias matrizDist, int numVertices, int numCentros) {
            this.matrizDist = matrizDist;
            this.numVertices = numVertices;
            this.numCentros = numCentros;
        }

        public Solucao resolver() {
            int[] centros = new int[numCentros];
            int[] distMinCentro = new int[numVertices + 1];

            long inicio = System.currentTimeMillis();

            // Primeiro centro
            centros[0] = 1;
            for (int v = 1; v <= numVertices; v++) {
                distMinCentro[v] = matrizDist.getDistancia(v, centros[0]);
            }

            // Centros restantes (algoritmo de Gonzalez)
            for (int i = 1; i < numCentros; i++) {
                int maxDist = -1;
                int novoCentro = -1;

                for (int v = 1; v <= numVertices; v++) {
                    if (distMinCentro[v] > maxDist) {
                        maxDist = distMinCentro[v];
                        novoCentro = v;
                    }
                }

                centros[i] = novoCentro;

                for (int v = 1; v <= numVertices; v++) {
                    distMinCentro[v] = Math.min(distMinCentro[v], 
                                                matrizDist.getDistancia(v, novoCentro));
                }
            }

            // Calcular raio
            int raio = 0;
            for (int v = 1; v <= numVertices; v++) {
                raio = Math.max(raio, distMinCentro[v]);
            }

            long tempo = System.currentTimeMillis() - inicio;
            return new Solucao("Aproximada (Gonzalez)", centros, raio, tempo);
        }
    }

    // ==================== CLASSE PRINCIPAL ====================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║        PROBLEMA DOS K-CENTROS - TP02 Grafos          ║");
            System.out.println("║    Arthur Pinto | Luis Costa | Samuel Pedrosa        ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            
            System.out.print("Digite o caminho do arquivo: ");
            String nomeArquivo = sc.nextLine().trim();
            
            // Carregar instância
            System.out.println("\nCarregando instância...");
            Instancia instancia = new Instancia(nomeArquivo);
            
            System.out.println("Arquivo: " + instancia.getNomeArquivo());
            System.out.println("Vértices (N): " + instancia.getNumVertices());
            System.out.println("Centros (K): " + instancia.getNumCentros());
            System.out.println();
            
            // Calcular matriz de distâncias
            System.out.println("Calculando distâncias (Floyd-Warshall)...");
            long inicioFloyd = System.currentTimeMillis();
            MatrizDistancias matrizDist = new MatrizDistancias(instancia.getGrafo());
            long tempoFloyd = System.currentTimeMillis() - inicioFloyd;
            System.out.println("Tempo: " + tempoFloyd + "ms");
            System.out.println();
            
            // Solução Aproximada
            System.out.println("Executando solução aproximada...");
            SolverAproximado solverAprox = new SolverAproximado(
                matrizDist, 
                instancia.getNumVertices(), 
                instancia.getNumCentros()
            );
            Solucao solucaoAprox = solverAprox.resolver();
            solucaoAprox.imprimir();
            
            // Solução Exata (apenas para instâncias pequenas)
            if (instancia.getNumVertices() <= 100) {
                System.out.println("Executando solução exata...");
                SolverExato solverExato = new SolverExato(
                    matrizDist, 
                    instancia.getNumVertices(), 
                    instancia.getNumCentros()
                );
                Solucao solucaoExata = solverExato.resolver();
                solucaoExata.imprimir();
                
                // Comparação
                System.out.println("╔════════════════════════════════════════════════════════╗");
                System.out.println("║                      Comparação                       ║");
                System.out.println("╠════════════════════════════════════════════════════════╣");
                
                double razao = (double) solucaoAprox.getRaio() / solucaoExata.getRaio();
                System.out.println("║  Razão Aprox/Exato: " + 
                    String.format("%-35s", String.format("%.4f", razao)) + "║");
                
                double speedup = (double) solucaoExata.getTempoExecucao() / 
                                solucaoAprox.getTempoExecucao();
                System.out.println("║  Speedup: " + 
                    String.format("%-44s", String.format("%.2fx", speedup)) + "║");
                
                System.out.println("╚════════════════════════════════════════════════════════╝");
            } else {
                System.out.println("⚠ Solução Exata: Inviável para N=" + 
                    instancia.getNumVertices() + " (muito grande)");
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
