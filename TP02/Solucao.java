public class Solucao {
    private String tipo;
    private int[] centros;
    private int raio;
    private double tempoExecucao; // em milissegundos (pode ter casa decimal)

    public Solucao(String tipo, int[] centros, int raio, double tempoExecucao) {
        this.tipo = tipo;
        this.centros = centros;
        this.raio = raio;
        this.tempoExecucao = tempoExecucao;
    }

    public int getRaio() {
        return raio;
    }

    public double getTempoExecucao() {
        return tempoExecucao;
    }

    public int[] getCentros() {
        return centros;
    }

    public String getTipo() {
        return tipo;
    }

    public void imprimir() {
        System.out.println("Solução: " + tipo);
        System.out.println("Raio: " + raio);

        System.out.print("Centros: ");
        StringBuilder centrosStr = new StringBuilder();
        for (int c : centros) {
            centrosStr.append(c).append(" ");
        }
        System.out.println(centrosStr.toString().trim());

        System.out.println("Tempo: " + String.format("%.3f ms", tempoExecucao));
        System.out.println();
    }
}
