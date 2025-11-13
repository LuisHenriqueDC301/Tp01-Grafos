public class SolverAproximado {
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

        // Primeiro centro (arbitrário)
        centros[0] = 1;
        for (int v = 1; v <= numVertices; v++) {
            distMinCentro[v] = matrizDist.getDistancia(v, centros[0]);
        }

        // Centros restantes - Algoritmo de Gonzalez
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

        // Calcular raio final
        int raio = 0;
        for (int v = 1; v <= numVertices; v++) {
            raio = Math.max(raio, distMinCentro[v]);
        }

        long tempo = System.currentTimeMillis() - inicio;
        return new Solucao("Aproximada (Gonzalez)", centros, raio, tempo);
    }
}
