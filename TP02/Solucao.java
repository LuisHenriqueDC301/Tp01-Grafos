public class Solucao {
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

    public int[] getCentros() {
        return centros;
    }

    public String getTipo() {
        return tipo;
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
