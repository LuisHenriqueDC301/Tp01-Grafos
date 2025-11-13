public class SolverExato {
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
