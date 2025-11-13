# Solução Aproximada - Problema dos K-Centros

## 📋 Visão Geral

A solução aproximada utiliza o **Algoritmo de Gonzalez** (também conhecido como Farthest-First Traversal) para encontrar uma solução de qualidade razoável em tempo polinomial.

**Garantia:** O raio encontrado é no máximo **2× o raio ótimo** (aproximação 2-ótima).

## 🎯 Algoritmo de Gonzalez

### Ideia Principal

Escolher centros iterativamente, sempre selecionando o vértice que está **mais distante** de todos os centros já escolhidos.

### Pseudocódigo

```
1. Escolher primeiro centro arbitrariamente (ex: vértice 1)
2. Para i = 2 até K:
   a. Para cada vértice v:
      - Calcular distância de v ao centro mais próximo
   b. Escolher como novo centro o vértice com maior distância
   c. Atualizar distâncias mínimas
3. Calcular raio final
```

## 🔧 Implementação

```java
public static int[] solucaoAproximada(int[][] dist, int N, int K)
```

### Estruturas de Dados

- `centros[K]`: Array com os k centros escolhidos
- `distMinCentro[N+1]`: Para cada vértice, distância ao centro mais próximo

### Passo a Passo

#### 1. Inicialização

```java
centros[0] = 1;  // Primeiro centro arbitrário

for (int v = 1; v <= N; v++) {
    distMinCentro[v] = dist[v][1];  // Distância ao primeiro centro
}
```

**Por que vértice 1?** Qualquer vértice inicial funciona. A escolha não afeta a garantia de aproximação.

#### 2. Seleção Iterativa de Centros

```java
for (int i = 1; i < K; i++) {
    // Encontrar vértice mais distante
    int maxDist = -1;
    int novoCentro = -1;
    
    for (int v = 1; v <= N; v++) {
        if (distMinCentro[v] > maxDist) {
            maxDist = distMinCentro[v];
            novoCentro = v;
        }
    }
    
    centros[i] = novoCentro;
    
    // Atualizar distâncias
    for (int v = 1; v <= N; v++) {
        distMinCentro[v] = Math.min(distMinCentro[v], dist[v][novoCentro]);
    }
}
```

**Lógica:**
- Percorrer todos os vértices
- Encontrar aquele com maior `distMinCentro[v]` (mais distante dos centros atuais)
- Adicionar esse vértice como novo centro
- Atualizar distâncias: cada vértice pode estar mais próximo do novo centro

#### 3. Cálculo do Raio Final

```java
int raio = 0;
for (int v = 1; v <= N; v++) {
    raio = Math.max(raio, distMinCentro[v]);
}
```

O raio é a maior distância de qualquer vértice ao seu centro mais próximo.

## 📊 Exemplo Completo

### Grafo (N=6, K=3)

```
Matriz de Distâncias:
    1   2   3   4   5   6
1 [ 0  10  20  30  40  50]
2 [10   0  15  25  35  45]
3 [20  15   0  10  20  30]
4 [30  25  10   0  10  20]
5 [40  35  20  10   0  10]
6 [50  45  30  20  10   0]
```

### Execução do Algoritmo

**Iteração 0: Primeiro Centro**
- `centros = [1, -, -]`
- `distMinCentro = [0, 10, 20, 30, 40, 50]`

**Iteração 1: Segundo Centro**
- Vértice mais distante: 6 (distância 50)
- `centros = [1, 6, -]`
- Atualizar distâncias:
  - `distMinCentro[1] = min(0, 50) = 0`
  - `distMinCentro[2] = min(10, 45) = 10`
  - `distMinCentro[3] = min(20, 30) = 20`
  - `distMinCentro[4] = min(30, 20) = 20`
  - `distMinCentro[5] = min(40, 10) = 10`
  - `distMinCentro[6] = min(50, 0) = 0`
- `distMinCentro = [0, 10, 20, 20, 10, 0]`

**Iteração 2: Terceiro Centro**
- Vértices mais distantes: 3 ou 4 (distância 20)
- Escolher 3 (primeiro encontrado)
- `centros = [1, 6, 3]`
- Atualizar distâncias:
  - `distMinCentro[1] = min(0, 20) = 0`
  - `distMinCentro[2] = min(10, 15) = 10`
  - `distMinCentro[3] = min(20, 0) = 0`
  - `distMinCentro[4] = min(20, 10) = 10`
  - `distMinCentro[5] = min(10, 20) = 10`
  - `distMinCentro[6] = min(0, 30) = 0`
- `distMinCentro = [0, 10, 0, 10, 10, 0]`

**Raio Final:**
- `raio = max(0, 10, 0, 10, 10, 0) = 10`

**Solução:**
- Centros: {1, 6, 3}
- Raio: 10

## 📈 Análise de Complexidade

### Tempo de Execução

- **Inicialização:** O(N)
- **Loop principal:** K iterações
  - Encontrar vértice mais distante: O(N)
  - Atualizar distâncias: O(N)
- **Total:** O(K × N)

### Comparação com Solução Exata

| Aspecto | Solução Exata | Solução Aproximada |
|---------|---------------|-------------------|
| Complexidade | O(C(N,K) × N × K) | O(K × N) |
| N=100, K=5 | ~75 milhões ops | ~500 ops |
| N=600, K=10 | Inviável | ~6.000 ops |
| Qualidade | Ótima | ≤ 2× ótima |

### Escalabilidade

| N   | K   | Tempo Aproximado | Viável? |
|-----|-----|------------------|---------|
| 100 | 5   | < 1ms            | ✅      |
| 600 | 10  | < 10ms           | ✅      |
| 900 | 90  | < 100ms          | ✅      |

## 🎓 Garantia Teórica

### Teorema (Gonzalez, 1985)

O algoritmo Farthest-First Traversal produz uma solução com raio `r_aprox` tal que:

```
r_aprox ≤ 2 × r_ótimo
```

### Prova (Intuição)

1. Seja `r*` o raio ótimo
2. Quando escolhemos o (k+1)-ésimo centro, sua distância aos k centros atuais é pelo menos `2r*`
3. Isso porque, se todos os k+1 vértices estivessem a distância < `2r*` entre si, seria impossível cobrir todos com k centros de raio `r*`
4. Logo, o raio da solução com k centros é no máximo `2r*`

### Exemplo de Pior Caso

```
Grafo: k+1 vértices igualmente espaçados em um círculo
- Raio ótimo: distância entre vértices adjacentes
- Raio aproximado: 2× distância entre adjacentes
```

## 🔍 Vantagens e Desvantagens

### ✅ Vantagens

1. **Eficiência:** O(K × N) - viável para instâncias grandes
2. **Garantia:** Sempre ≤ 2× ótimo
3. **Simplicidade:** Fácil de implementar e entender
4. **Determinístico:** Sempre produz a mesma solução

### ❌ Desvantagens

1. **Não ótimo:** Pode ser até 2× pior que o ótimo
2. **Escolha inicial:** Vértice inicial pode afetar qualidade (mas não a garantia)
3. **Sem melhoria local:** Não tenta melhorar a solução após encontrá-la

## 🚀 Melhorias Possíveis

### 1. Múltiplas Execuções
```java
// Testar diferentes vértices iniciais
for (int inicio = 1; inicio <= N; inicio++) {
    int[] solucao = solucaoAproximadaComInicio(dist, N, K, inicio);
    // Guardar melhor solução
}
```

### 2. Busca Local
```java
// Após encontrar solução, tentar trocar centros
for (int i = 0; i < K; i++) {
    for (int v = 1; v <= N; v++) {
        // Testar trocar centros[i] por v
        // Se melhorar, aceitar troca
    }
}
```

### 3. Heurística Gulosa Modificada
```java
// Ao invés de escolher o mais distante,
// escolher vértice que minimiza o raio resultante
```

## 📊 Resultados Esperados

### Instâncias Pequenas (N ≤ 100)

| Instância | N   | K  | Raio Ótimo | Raio Aprox | Razão |
|-----------|-----|----|------------|------------|-------|
| pmed1     | 100 | 5  | 127        | ~150-200   | ~1.5  |
| pmed2     | 100 | 10 | 98         | ~120-150   | ~1.3  |
| pmed5     | 100 | 33 | 48         | ~60-80     | ~1.4  |

### Instâncias Grandes (N > 100)

| Instância | N   | K   | Raio Aprox | Tempo |
|-----------|-----|-----|------------|-------|
| pmed21    | 500 | 5   | ~50-60     | ~5ms  |
| pmed30    | 600 | 200 | ~12-15     | ~20ms |
| pmed40    | 900 | 90  | ~15-20     | ~30ms |

**Nota:** Raio ótimo desconhecido para instâncias grandes (inviável calcular).

## 🎯 Uso Prático

### Quando Usar Solução Aproximada?

- ✅ Instâncias grandes (N > 150)
- ✅ Tempo de execução crítico
- ✅ Solução "boa o suficiente" é aceitável
- ✅ Análise de escalabilidade

### Quando Usar Solução Exata?

- ✅ Instâncias pequenas (N ≤ 100)
- ✅ Solução ótima necessária
- ✅ Tempo não é crítico
- ✅ Validação da solução aproximada

## 📝 Observações Finais

1. **Qualidade na Prática:** Geralmente a solução aproximada é muito melhor que 2× ótimo (tipicamente 1.2-1.5× ótimo)

2. **Trade-off:** Sacrifica ~20-50% de qualidade para ganhar 1000-10000× em velocidade

3. **Complementaridade:** Use solução exata para validar a aproximada em instâncias pequenas

4. **Extensões:** O algoritmo pode ser adaptado para variantes do problema (k-centros com capacidades, k-centros métrico, etc.)
