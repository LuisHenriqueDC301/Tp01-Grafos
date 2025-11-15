public class Solucao {
    private String tipo;
    private int[] centros;
    private int raio;
    private long tempoExecucao; // em milissegundos

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

    public int[] getCentros() {
        return centros;
    }

    public String getTipo() {
        return tipo;
    }

    public void imprimir() {
        double tempoMs = tempoExecucao * 1.0; // converte pra double pra ter casas decimais

        System.out.println("Solução: " + tipo);
        System.out.println("Raio: " + raio);

        System.out.print("Centros: ");
        StringBuilder centrosStr = new StringBuilder();
        for (int c : centros) {
            centrosStr.append(c).append(" ");
        }
        System.out.println(centrosStr.toString().trim());

        System.out.println("Tempo: " + String.format("%.3f ms", tempoMs));
        System.out.println();
    }
}
