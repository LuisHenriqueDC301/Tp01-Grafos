public class MatrizDistancias {
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
                dist[i][aresta.getDestino()] = aresta.getPeso();
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
