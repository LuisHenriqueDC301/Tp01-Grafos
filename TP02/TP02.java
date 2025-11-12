import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TP02 {

    private static class Aresta {
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

    // Função para montar Lista de Adjacência para Direcionado e Ponderado a partir
    // do arquivo
    // txt

    public static void montarGrafo(ArrayList<Aresta>[] listaAdjacente, Scanner scannerFile, int N) {
        int origem, destino, peso;

        while (scannerFile.hasNextInt()) {
            origem = scannerFile.nextInt();
            destino = scannerFile.nextInt();
            peso = scannerFile.nextInt();
            listaAdjacente[origem].add(new Aresta(destino, peso));

        }
    }

    // Seleciona o vértice imaturo com a menor distância.
    // private static int selecionarImaturoComMenorDist(long[] dist, boolean[] maduro) {
    //     int indice = -1;
    //     long menorDist = Long.MAX_VALUE;
    //     for (int v = 0; v < dist.length; v++) {
    //         if (!maduro[v] && dist[v] < menorDist) {
    //             menorDist = dist[v];
    //             indice = v;
    //         }
    //     }
    //     return indice;
    // }

    // Relaxa todas as arestas que saem do vértice v.
    // private static void relaxarSucessores(
    //         int origemAtual,
    //         ArrayList<Aresta>[] listaAdjacencia,
    //         long[] distancia,
    //         int[] predecessor,
    //         int[] arestasUsadas) {

    //     long distanciaOrigemAtual = distancia[origemAtual];
    //     if (distanciaOrigemAtual == Long.MAX_VALUE)
    //         return; // vértice inalcançável: evita overflow

    //     for (Aresta aresta : listaAdjacencia[origemAtual]) {
    //         int destino = aresta.getDestino();
    //         long novaDistancia = distanciaOrigemAtual + (long) aresta.getPeso();
    //         int novasArestasUsadas = arestasUsadas[origemAtual] + 1;

    //         // Caso 1: encontramos caminho com menor custo
    //         if (novaDistancia < distancia[destino]) {
    //             distancia[destino] = novaDistancia;
    //             predecessor[destino] = origemAtual;
    //             arestasUsadas[destino] = novasArestasUsadas;
    //         }
    //         // Caso 2: mesmo custo, mas caminho com menos arestas
    //         else if (novaDistancia == distancia[destino]
    //                 && novasArestasUsadas < arestasUsadas[destino]) {
    //             predecessor[destino] = origemAtual;
    //             arestasUsadas[destino] = novasArestasUsadas;
    //         }
    //     }
    // }

    // public static void imprimirCaminho(int destino, int[] pred) {
    //     if (destino < 0) {
    //         System.out.print("Caminho inexistente.");
    //         return;
    //     }
    //     if (pred[destino] == -1) {
    //         System.out.print(destino);
    //     } else {
    //         imprimirCaminho(pred[destino], pred);
    //         System.out.print(" -> " + destino);
    //     }
    // }

    public static void imprimirGrafo(ArrayList<Aresta>[] listaAdj) {
        for (int i = 1; i < listaAdj.length; i++) {
            System.out.print(i + ": ");
            for (Aresta aresta : listaAdj[i]) {
                System.out.print(" -> (" + aresta.getDestino() + ", " + aresta.getPeso() + ") Peso:");
            }
            System.out.println();
        }
    }

    public static void executar(String nomeArquivo) throws FileNotFoundException {
        File file = new File(nomeArquivo);
        if (!file.exists()) {
            System.out.println("Arquivo não encontrado: " + nomeArquivo);
            return;
        }

        Scanner scannerFile = new Scanner(file);
        int N = scannerFile.nextInt();
        int distMax = scannerFile.nextInt();
        int K = scannerFile.nextInt();

        @SuppressWarnings("unchecked")
        ArrayList<Aresta>[] listaAdj = new ArrayList[N+1];
        for (int i = 1; i <= N; i++) {
            listaAdj[i] = new ArrayList<>();
        }


        montarGrafo(listaAdj, scannerFile, N);
        imprimirGrafo(listaAdj);
        

        scannerFile.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo: ");
        // String nomeArquivo = sc.nextLine().trim();
        String nomeArquivo;
        // for(int i =1; i<=40 ;i++){
        //     nomeArquivo = "g/pmed"+i+".txt";
        //     executar(nomeArquivo);
        // }
    }

}
