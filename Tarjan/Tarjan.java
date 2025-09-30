import java.io.*;
import java.util.*;

public class Tarjan {

    static List<List<Integer>> grafo;
    static int time = 0;
    static int[] disc, low, pai;
    static boolean[] visitado;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            File arquivo = lerArquivo(scanner);  
            try {
                encontrarPontes(arquivo.getPath()); //chamada
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            }
        } 
    }


    public static File lerArquivo(Scanner scanner) {
        String fileName;
        File file;

        System.out.println("Digite o nome do arquivo:");
        fileName = scanner.nextLine();
        file = new File(fileName);

        while (!file.exists()) {
            System.out.println("Arquivo não encontrado!");
            System.out.println("Digite o nome do arquivo:");
            fileName = scanner.nextLine();
            file = new File(fileName);
        }

        return file;
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
                String[] parts = line.trim().split("\\s+");
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
        Arrays.fill(pai, -1); // preenchendo tudo no começo

        time = 0;

        System.out.println("Pontes encontradas:");
            for (int i = 0; i < n; i++) {
                if (!visitado[i]) {
                    dfs(i); // iterativo
                    }
}
    }

static void dfs(int start) {
    int n = grafo.size();
    Stack<Integer> stack = new Stack<>();
    int[] index = new int[n];       // índice de onde estamos explorando cada vizinho
    Arrays.fill(index, 0);
    
    stack.push(start);
    visitado[start] = true;
    disc[start] = low[start] = ++time;

    while (!stack.isEmpty()) {
        int u = stack.peek();

        if (index[u] < grafo.get(u).size()) {
            int v = grafo.get(u).get(index[u]);
            index[u]++; // próximo vizinho na próxima iteração

            if (!visitado[v]) {
                visitado[v] = true;
                pai[v] = u;
                disc[v] = low[v] = ++time;
                stack.push(v);
            } else if (v != pai[u]) {
                // atualiza low de u caso low seja menos que o TD 
                low[u] = Math.min(low[u], disc[v]);
            }
        } else {
            
            stack.pop();
            if (pai[u] != -1) {
                low[pai[u]] = Math.min(low[pai[u]], low[u]);
                if (low[u] > disc[pai[u]]) {
                    System.out.println(pai[u] + " - " + u);
                    }
                }
            }
        }
    }
}

