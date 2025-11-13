import java.util.ArrayList;

public class Grafo {
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

        public int getDestino() {
            return destino;
        }

        public int getPeso() {
            return peso;
        }
    }
}
