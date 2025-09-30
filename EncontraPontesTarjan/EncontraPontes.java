import java.io.*;
import java.util.*;

public class EncontraPontes {

    static List<List<Integer>> grafo;
    static int time = 0;
    static int[] disc, low, pai;
    static boolean[] visitado;

    public static void main(String[] args) {
        
        String filePath = "E:\\Documentos2\\Grafos\\Trabalho\\Ponte100.txt"; 
        try {
            encontrarPontes(filePath); // chamada 
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static void encontrarPontes(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
// -----------------------------------------------"Montando" grafo,Colocando Arestas----------------------------------------------
        int n = Integer.parseInt(reader.readLine().trim());
        grafo = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            grafo.add(new ArrayList<>());
        }

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.trim().split(" ");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);

                // não-direcionado
                grafo.get(u).add(v);
                grafo.get(v).add(u);
            }
        }
        reader.close();
// ---------------------------------------------Metodo em Si------------------------------------------
        disc = new int[n]; //TD da DFS
        low = new int[n]; //menor tempo alcançável a partir do vértice (retornos ou descendentes) coisa do Tarjan
        pai = new int[n]; // pai na DFS
        visitado = new boolean[n]; // se foi visitado
        Arrays.fill(pai, -1); // preenchendo o array de pais

        System.out.println("Pontes encontradas:");
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                dfs(i);
            }
        }
    }

    static void dfs(int u) { //Metodo Recursivo da DFS em java
        visitado[u] = true;
        disc[u] = low[u] = ++time;

        for (int v : grafo.get(u)) {
            if (!visitado[v]) {
                pai[v] = u;
                dfs(v);
                                                    //Importante notar que aqui saiu da Recursão, logo a DFS do vertice atual terminou 
                low[u] = Math.min(low[u], low[v]);  // atualizando o LOW de acordo com o filho e  pai

                if (low[v] > disc[u]) {             // Se o LOW for maior que a descoberta significa que não há outro caminho para vertice,logo PONTE
                    System.out.println(u + " - " + v);
                }
            } else if (v != pai[u]) {            // Se não, significa que há outro caminho que pode ter um low menor, então primeiro garantir que v não é pai direto de u...
                low[u] = Math.min(low[u], disc[v]); // ...Depois atualizar o low para refletir o menor entre os caminhos
            }
        }
    }
}
