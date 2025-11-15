import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainSimples {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Informe o caminho da pasta com os arquivos pmed: ");
            String pasta = sc.nextLine().trim();

            // Arquivo de saída
            PrintWriter writer = new PrintWriter(new FileWriter("resultado_simples.txt"));

            System.out.println("\n===========================================================\n");
            writer.println("===========================================================");

            // ----------- RAIO ESPERADOS (40 instâncias) -----------
            int[] raiosEsperados = {
                    127, 98, 93, 74, 48,
                    84, 64, 55, 37, 20,
                    59, 51, 35, 26, 18,
                    47, 39, 28, 18, 13,
                    40, 38, 22, 15, 11,
                    38, 32, 18, 13, 9,
                    30, 29, 15, 11, 30,
                    27, 15, 29, 23, 13
            };

            for (int i = 1; i <= 40; i++) {

                String nome = pasta + "\\pmed" + i + ".txt";

                try {
                    Instancia inst = new Instancia(nome);

                    // Floyd-Warshall
                    MatrizDistancias matriz = new MatrizDistancias(inst.getGrafo());

                    // Aproximada
                    SolverAproximado solverA = new SolverAproximado(
                            matriz, inst.getNumVertices(), inst.getNumCentros());
                    Solucao solA = solverA.resolver();

                    int esperado = raiosEsperados[i - 1];
                    int encontrado = solA.getRaio();

                    // Erro relativo em porcentagem
                    double erro = ((double) (encontrado - esperado) / esperado) * 100.0;

                    String linha = String.format(
                            "pmed%d.txt  -> N=%d  K=%d  RaioEsperado=%d  RaioEncontrado=%d  Tempo=%.3f ms  Erro=%.3f%%",
                            i,
                            inst.getNumVertices(),
                            inst.getNumCentros(),
                            esperado,
                            encontrado,
                            solA.getTempoExecucao(),
                            erro);

                    System.out.println(linha);
                    writer.println(linha);

                } catch (Exception e) {
                    String erroLinha = String.format(
                            "pmed%d.txt  -> Erro ao processar (%s)",
                            i, e.getMessage());
                    System.out.println(erroLinha);
                    writer.println(erroLinha);
                }
            }

            System.out.println("\n===========================================================\n");
            writer.println("===========================================================");
            writer.close();

            System.out.println("Arquivo salvo como: resultado_simples.txt");

        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
