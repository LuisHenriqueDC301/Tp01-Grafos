import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Instancia {
    private String nomeArquivo;
    private int numVertices;
    private int numCentros;
    private Grafo grafo;

    public Instancia(String nomeArquivo) throws FileNotFoundException {
        this.nomeArquivo = nomeArquivo;
        carregar();
    }

    private void carregar() throws FileNotFoundException {
        File file = new File(nomeArquivo);
        if (!file.exists()) {
            throw new FileNotFoundException("Arquivo não encontrado: " + nomeArquivo);
        }

        Scanner scanner = new Scanner(file);
        this.numVertices = scanner.nextInt();
        int numArestas = scanner.nextInt();
        this.numCentros = scanner.nextInt();

        this.grafo = new Grafo(numVertices);

        while (scanner.hasNextInt()) {
            int origem = scanner.nextInt();
            int destino = scanner.nextInt();
            int peso = scanner.nextInt();
            grafo.adicionarAresta(origem, destino, peso);
        }

        scanner.close();
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumCentros() {
        return numCentros;
    }

    public Grafo getGrafo() {
        return grafo;
    }
}
