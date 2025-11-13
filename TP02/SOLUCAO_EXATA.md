# Solução Exata - Problema dos K-Centros

## 📋 Visão Geral

A solução exata encontra o conjunto ótimo de k centros que minimiza o **raio** da solução, onde o raio é a maior distância de qualquer vértice ao seu centro mais próximo.

## 🔧 Implementação

### 1. Floyd-Warshall - Cálculo de Distâncias Mínimas

```java
public static int[][] floydWarshall(ArrayList<Aresta>[] listaAdj, int N)
```

**Objetivo:** Calcular a menor distância entre todos os pares de vértices.

**Algoritmo:**

1. **Inicialização:**
   - `dist[i][i] = 0` (distância de um vértice para ele mesmo)
   - `dist[i][j] = INF` (infinito = 999999999) para os demais

2. **Arestas Diretas:**
   - Para cada aresta (i, j) com peso w: `dist[i][j] = w`

3. **Relaxamento:**
   - Para cada vértice k como intermediário:
     - Para cada par (i, j):
       - Se `dist[i][j] > dist[i][k] + dist[k][j]`
       - Então `dist[i][j] = dist[i][k] + dist[k][j]`

**Complexidade:** O(N³)

**Por que INF = 999999999?**
- Evita overflow ao somar: `INF + INF` não ultrapassa `Integer.MAX_VALUE`
- Grande o suficiente para representar "sem caminho direto"

**Grafo Não-Direcionado:**
- Ao ler aresta (i, j, peso), adicionamos:
  - `listaAdj[i] → (j, peso)`
  - `listaAdj[j] → (i, peso)`

---

### 2. Cálculo do Raio

```java
private static int calcularRaio(int[][] dist, int[] centros, int N)
```

**Objetivo:** Dado um conjunto de centros, calcular o raio da solução.

**Algoritmo:**

1. Para cada vértice v (1 até N):
   - Encontrar a menor distância de v até qualquer centro
   - `menorDist = min(dist[v][c1], dist[v][c2], ..., dist[v][ck])`

2. O raio é a **maior** dessas menores distâncias:
   - `raio = max(menorDist de todos os vértices)`

**Exemplo:**
- Centros = {10, 50, 80}
- Vértice 25: distâncias aos centros = [15, 25, 55] → menor = 15
- Vértice 90: distâncias aos centros = [80, 40, 10] → menor = 10
- Vértice 1: distâncias aos centros = [9, 49, 79] → menor = 9
- Se o vértice mais distante tem menorDist = 127
- Então **raio = 127**

**Complexidade:** O(N × K)

---

### 3. Geração de Combinações

```java
private static void gerarCombinacoes(int[][] dist, int N, int K, 
                                     int[] centros, int pos, int inicio, 
                                     int[] melhorSolucao, int[] melhorRaio)
```

**Objetivo:** Gerar todas as combinações de k vértices dentre N vértices.

**Algoritmo (Backtracking):**

1. **Caso Base:** Se `pos == K` (escolhemos k centros):
   - Calcular raio da combinação atual
   - Se raio < melhorRaio, atualizar melhor solução

2. **Caso Recursivo:**
   - Para cada vértice i de `inicio` até `N - (K - pos) + 1`:
     - Escolher vértice i como centro na posição `pos`
     - Recursão: `gerarCombinacoes(..., pos+1, i+1, ...)`

**Poda:** `N - (K - pos) + 1` garante que ainda há vértices suficientes para completar k centros.

**Exemplo (N=5, K=2):**
```
Combinações geradas:
{1,2}, {1,3}, {1,4}, {1,5}
{2,3}, {2,4}, {2,5}
{3,4}, {3,5}
{4,5}
Total: C(5,2) = 10 combinações
```

**Complexidade:** O(C(N,K)) onde C(N,K) = N!/(K!(N-K)!)

---

### 4. Solução Exata Principal

```java
public static int[] solucaoExata(int[][] dist, int N, int K)
```

**Objetivo:** Encontrar o conjunto ótimo de k centros.

**Algoritmo:**

1. Inicializar:
   - `melhorRaio = ∞`
   - `melhorSolucao = array vazio`

2. Chamar `gerarCombinacoes()` para testar todas as combinações

3. Retornar `melhorSolucao` (conjunto de k centros com menor raio)

**Saída:**
```
Solução Exata - Raio: 127
Centros: 10 35 60 85 95
```

---

## 📊 Complexidade Total

- **Floyd-Warshall:** O(N³)
- **Testar todas combinações:** O(C(N,K) × N × K)
- **Total:** O(N³ + C(N,K) × N × K)

### Viabilidade:

| N   | K  | C(N,K)        | Viável? |
|-----|----|---------------|---------|
| 100 | 5  | ~75 milhões   | Lento   |
| 200 | 5  | ~2.5 bilhões  | Inviável|
| 300 | 5  | ~190 bilhões  | Inviável|

**Conclusão:** Solução exata é viável apenas para instâncias pequenas (N ≤ 100-150).

---

## 🎯 Fluxo de Execução

```
1. Ler arquivo (N, K, arestas)
2. Montar grafo não-direcionado
3. Floyd-Warshall → matriz dist[N+1][N+1]
4. Solução Exata:
   a. Gerar todas C(N,K) combinações de k centros
   b. Para cada combinação:
      - Calcular raio
      - Atualizar melhor solução se raio < melhorRaio
   c. Retornar melhor solução
5. Imprimir raio e centros
```

---

## 🔍 Exemplo Completo (N=5, K=2)

**Grafo:**
```
1 -- 10 -- 2
|          |
20        15
|          |
3 -- 5  -- 4
     |
     25
     |
     5
```

**Matriz de Distâncias (após Floyd-Warshall):**
```
    1   2   3   4   5
1 [ 0  10  20  25  45]
2 [10   0  25  15  40]
3 [20  25   0   5  30]
4 [25  15   5   0  25]
5 [45  40  30  25   0]
```

**Testando Combinações (K=2):**

- **{1,2}:** raio = max(0, 0, 20, 15, 40) = 40
- **{1,3}:** raio = max(0, 10, 0, 5, 30) = 30
- **{1,4}:** raio = max(0, 10, 5, 0, 25) = 25
- **{1,5}:** raio = max(0, 10, 20, 25, 0) = 25
- **{2,3}:** raio = max(10, 0, 0, 5, 30) = 30
- **{2,4}:** raio = max(10, 0, 5, 0, 25) = 25
- **{2,5}:** raio = max(10, 0, 25, 25, 0) = 25
- **{3,4}:** raio = max(20, 15, 0, 0, 25) = 25
- **{3,5}:** raio = max(20, 25, 0, 25, 0) = 25
- **{4,5}:** raio = max(25, 15, 5, 0, 0) = 25

**Solução Ótima:** Qualquer combinação com raio = 25 (ex: {1,4}, {2,4}, {3,4}, etc.)

---

## 📝 Observações Importantes

1. **Problema dos K-Centros ≠ Problema das P-Medianas**
   - K-Centros: minimiza o **raio** (max distância)
   - P-Medianas: minimiza a **soma** das distâncias
   - O arquivo `pmedopt.txt` contém soluções para p-medianas, não k-centros

2. **Valores Esperados (K-Centros):**
   - pmed1 (N=100, K=5): Raio = 127
   - pmed2 (N=100, K=10): Raio = 98
   - Valores da tabela na especificação

3. **Limitações:**
   - Inviável para N > 150
   - Necessário implementar solução aproximada para instâncias grandes
