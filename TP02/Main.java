import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
            imprimirCabecalho();
            
            System.out.print("Digite o caminho do arquivo: ");
            String nomeArquivo = sc.nextLine().trim();
            
            executar(nomeArquivo);
            
        } catch (FileNotFoundException e) {
            System.err.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
    
    private static void executar(String nomeArquivo) throws FileNotFoundException {
        // Carregar instância
        System.out.println("\nCarregando instância...");
        Instancia instancia = new Instancia(nomeArquivo);
        
        imprimirInfoInstancia(instancia);
        
        // Calcular matriz de distâncias
        System.out.println("Calculando distâncias (Floyd-Warshall)...");
        long inicioFloyd = System.currentTimeMillis();
        MatrizDistancias matrizDist = new MatrizDistancias(instancia.getGrafo());
        long tempoFloyd = System.currentTimeMillis() - inicioFloyd;
        System.out.println("Tempo: " + tempoFloyd + "ms");
        System.out.println();
        
        // Solução Aproximada
        System.out.println("Executando solução aproximada...");
        SolverAproximado solverAprox = new SolverAproximado(
            matrizDist, 
            instancia.getNumVertices(), 
            instancia.getNumCentros()
        );
        Solucao solucaoAprox = solverAprox.resolver();
        solucaoAprox.imprimir();
        
        // Solução Exata (apenas para instâncias pequenas)
        if (instancia.getNumVertices() <= 100) {
            System.out.println("Executando solução exata...");
            SolverExato solverExato = new SolverExato(
                matrizDist, 
                instancia.getNumVertices(), 
                instancia.getNumCentros()
            );
            Solucao solucaoExata = solverExato.resolver();
            solucaoExata.imprimir();
            
            imprimirComparacao(solucaoAprox, solucaoExata);
        } else {
            System.out.println("⚠ Solução Exata: Inviável para N=" + 
                instancia.getNumVertices() + " (muito grande)");
        }
    }
    
    private static void imprimirCabecalho() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║        PROBLEMA DOS K-CENTROS - TP02 Grafos          ║");
        System.out.println("║    Arthur Pinto | Luis Costa | Samuel Pedrosa        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void imprimirInfoInstancia(Instancia instancia) {
        System.out.println("Arquivo: " + instancia.getNomeArquivo());
        System.out.println("Vértices (N): " + instancia.getNumVertices());
        System.out.println("Centros (K): " + instancia.getNumCentros());
        System.out.println();
    }
    
    private static void imprimirComparacao(Solucao solucaoAprox, Solucao solucaoExata) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║                      Comparação                       ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        
        double razao = (double) solucaoAprox.getRaio() / solucaoExata.getRaio();
        System.out.println("║  Razão Aprox/Exato: " + 
            String.format("%-35s", String.format("%.4f", razao)) + "║");
        
        double speedup = (double) solucaoExata.getTempoExecucao() / 
                        solucaoAprox.getTempoExecucao();
        System.out.println("║  Speedup: " + 
            String.format("%-44s", String.format("%.2fx", speedup)) + "║");
        
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}
