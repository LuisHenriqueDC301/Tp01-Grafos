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

        long inicio = System.nanoTime();  // mede em nanos

        // --------------------------------------
        // ALGORITMO DE GONZALEZ (P-MEDIAN APPROX)
        // --------------------------------------
        int[] centros = new int[numCentros];

        // Passo 1: escolhe um centro inicial (você pode usar qualquer vértice, normalmente 1)
        centros[0] = 1;

        // Passo 2: escolher sempre o vértice mais distante do conjunto atual
        for (int i = 1; i < numCentros; i++) {
            int maisDistante = -1;
            int distMax = -1;

            for (int v = 1; v <= numVertices; v++) {

                // acha a menor distância até algum dos centros já escolhidos
                int menorDist = Integer.MAX_VALUE;
                for (int j = 0; j < i; j++) {
                    menorDist = Math.min(menorDist, matrizDist.getDistancia(v, centros[j]));
                }

                if (menorDist > distMax) {
                    distMax = menorDist;
                    maisDistante = v;
                }
            }

            centros[i] = maisDistante;
        }

        // Passo 3: calcula o raio final
        int raio = 0;
        for (int v = 1; v <= numVertices; v++) {
            int menorDist = Integer.MAX_VALUE;
            for (int c : centros) {
                menorDist = Math.min(menorDist, matrizDist.getDistancia(v, c));
            }
            raio = Math.max(raio, menorDist);
        }

        long fim = System.nanoTime();

        // Converte para milissegundos com casas decimais
        double tempoMs = (fim - inicio) / 1_000_000.0;

        return new Solucao("Aproximada (Gonzalez)", centros, raio, tempoMs);
    }
}
