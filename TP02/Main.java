import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {

            // Pergunta o caminho da pasta
            System.out.print("Informe o caminho da pasta com os arquivos pmed: ");
            String caminhoPasta = sc.nextLine().trim();

            // Pergunta o tipo de resolução
            System.out.print("Tipo de resolução (1 = aproximada, 0 = exata): ");
            int opcao = sc.nextInt();
            boolean aproximada = (opcao == 1);

            String tipoStr = aproximada ? "aproximada" : "exata";

            // Arquivo único de saída
            PrintWriter writer = new PrintWriter(new FileWriter("resultado_" + tipoStr + ".txt"));

            imprimirCabecalho(writer);

            // Loop 1 a 40
            for (int i = 1; i <= 40; i++) {

                String nomeArquivo = caminhoPasta + "\\pmed" + i + ".txt";

                log(writer, "Processando arquivo: pmed" + i + ".txt");
                log(writer, "");

                try {
                    executar(nomeArquivo, aproximada, writer);
                } catch (Exception e) {
                    log(writer, "Erro ao processar pmed" + i + ": " + e.getMessage());
                }

                log(writer, "------------------------------");
                log(writer, "");
            }

            log(writer, "Processamento concluído.");
            writer.close();

        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    private static void executar(String nomeArquivo, boolean aproximada,
                                 PrintWriter out) throws FileNotFoundException {

        Instancia instancia = new Instancia(nomeArquivo);

        imprimirInfoInstancia(instancia, out);

        // Floyd-Warshall
        log(out, "Calculando distâncias (Floyd-Warshall)...");
        long ini = System.nanoTime();
        MatrizDistancias matriz = new MatrizDistancias(instancia.getGrafo());
        long fim = System.nanoTime();
        double tempoMs = (fim - ini) / 1_000_000.0;
        log(out, "Tempo: " + String.format("%.3f ms", tempoMs));
        log(out, "");

        if (aproximada) {
            SolverAproximado solver = new SolverAproximado(
                    matriz, instancia.getNumVertices(), instancia.getNumCentros());
            Solucao sol = solver.resolver();
            imprimirSolucao(sol, out);
        } else {

            if (instancia.getNumVertices() <= 100) {
                SolverExato solver = new SolverExato(
                        matriz, instancia.getNumVertices(), instancia.getNumCentros());
                Solucao sol = solver.resolver();
                imprimirSolucao(sol, out);
            } else {
                log(out, "⚠ Solução exata inviável (N muito grande).");
                log(out, "");
            }
        }
    }

    // -------------------- Funções auxiliares --------------------

    private static void log(PrintWriter out, String msg) {
        System.out.println(msg);
        out.println(msg);
    }

    private static void imprimirCabecalho(PrintWriter out) {
        log(out, "╔════════════════════════════════════════════════════════╗");
        log(out, "║        PROBLEMA DOS K-CENTROS - TP02 Grafos            ║");
        log(out, "║    Arthur Pinto | Luis Costa | Samuel Pedrosa          ║");
        log(out, "╚════════════════════════════════════════════════════════╝");
        log(out, "");
    }

    private static void imprimirInfoInstancia(Instancia instancia, PrintWriter out) {
        log(out, "Arquivo: " + instancia.getNomeArquivo());
        log(out, "Vértices (N): " + instancia.getNumVertices());
        log(out, "Centros (K): " + instancia.getNumCentros());
        log(out, "");
    }

    private static void imprimirSolucao(Solucao sol, PrintWriter out) {
        log(out, "Solução: " + sol.getTipo());
        log(out, "Raio: " + sol.getRaio());

        StringBuilder sb = new StringBuilder("Centros: ");
        for (int c : sol.getCentros()) sb.append(c).append(" ");
        log(out, sb.toString().trim());

        log(out, "Tempo: " + String.format("%.3f ms", sol.getTempoExecucao()));
        log(out, "");
    }
}
