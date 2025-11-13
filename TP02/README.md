# Trabalho Prático 2 - Teoria dos Grafos e Computabilidade

**Professor:** Zenilton Kleber Gonçalves do Patrocínio Júnior  
**Disciplina:** Teoria dos Grafos e Computabilidade  
**Instituição:** Pontifícia Universidade Católica de Minas Gerais (PUC Minas)

## 👥 Equipe

- Arthur Henrique Tristão Pinto
- Luis Henrique Ferreira Costa
- Samuel Correia Pedrosa

## 📘 Descrição do Projeto

Este trabalho implementa e compara duas abordagens para o **problema dos k-centros**.

### Problema dos K-Centros

Dado um grafo completo com custos nas arestas e um inteiro k, encontrar um conjunto de k vértices (centros) que minimize a maior distância de qualquer vértice ao seu centro mais próximo. Essa medida é o **raio da solução**.

### Implementações

#### 🎯 Solução Exata
- Obtém a solução ótima
- Viável para instâncias pequenas (N ≤ 100)
- Algoritmo de força bruta testando todas as combinações

#### ⚡ Solução Aproximada  
- Algoritmo de Gonzalez (Farthest-First Traversal)
- Garantia de aproximação 2-ótima
- Viável para instâncias grandes (N > 100)

### Objetivo

Análise comparativa avaliando:
- **Eficácia:** qualidade da solução
- **Eficiência:** tempo de execução

Conforme o tamanho das instâncias aumenta.

---

## 🚀 Como Compilar e Executar

### Compilação

**Opção 1: Compilar todos os arquivos**
```bash
javac *.java
```

**Opção 2: Compilar apenas o Main (compila dependências automaticamente)**
```bash
javac Main.java
```

### Execução

```bash
java Main
```

O programa solicitará o caminho do arquivo de entrada. Exemplo:
```
Digite o caminho do arquivo: pmed_files/pmed1.txt
```

---

## 📁 Estrutura do Projeto

```
TP02/
├── Main.java                  # Programa principal
├── Grafo.java                 # Estrutura do grafo
├── Instancia.java             # Carregamento de arquivos
├── MatrizDistancias.java      # Algoritmo Floyd-Warshall
├── Solucao.java               # Representação de resultados
├── SolverExato.java           # Solução exata (força bruta)
├── SolverAproximado.java      # Solução aproximada (Gonzalez)
├── SOLUCAO_EXATA.md           # Documentação da solução exata
├── SOLUCAO_APROXIMADA.md      # Documentação da solução aproximada
├── especifica.md              # Especificação do trabalho
└── pmed_files/                # 40 instâncias de teste
    ├── pmed1.txt - pmed40.txt
    └── pmedopt.txt            # Soluções ótimas (p-medianas)
```

---

## 📊 Instâncias de Teste

O projeto inclui 40 instâncias do problema das p-medianas adaptadas para k-centros:

| Tamanho | Instâncias | Vértices (N) | Centros (K) |
|---------|------------|--------------|-------------|
| Pequeno | pmed1-5    | 100          | 5-33        |
| Médio   | pmed6-20   | 200-400      | 5-133       |
| Grande  | pmed21-40  | 500-900      | 5-200       |

**Recomendações:**
- Solução Exata: Use instâncias pmed1-5 (N=100)
- Solução Aproximada: Todas as instâncias

---

## 🎯 Exemplo de Uso

```bash
$ javac Main.java
$ java Main
╔════════════════════════════════════════════════════════╗
║        PROBLEMA DOS K-CENTROS - TP02 Grafos          ║
║    Arthur Pinto | Luis Costa | Samuel Pedrosa        ║
╚════════════════════════════════════════════════════════╝

Digite o caminho do arquivo: pmed_files/pmed1.txt

Carregando instância...
Arquivo: pmed_files/pmed1.txt
Vértices (N): 100
Centros (K): 5

Calculando distâncias (Floyd-Warshall)...
Tempo: 45ms

Executando solução aproximada...
╔════════════════════════════════════════════════════════╗
║  Solução Aproximada (Gonzalez)                        ║
╠════════════════════════════════════════════════════════╣
║  Raio: 150                                            ║
║  Centros: 1 100 50 25 75                              ║
║  Tempo: 2ms                                           ║
╚════════════════════════════════════════════════════════╝

Executando solução exata...
╔════════════════════════════════════════════════════════╗
║  Solução Exata                                        ║
╠════════════════════════════════════════════════════════╣
║  Raio: 127                                            ║
║  Centros: 10 35 60 85 95                              ║
║  Tempo: 15234ms                                       ║
╚════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════╗
║                      Comparação                       ║
╠════════════════════════════════════════════════════════╣
║  Razão Aprox/Exato: 1.1811                            ║
║  Speedup: 7617.00x                                    ║
╚════════════════════════════════════════════════════════╝
```

---

## 📚 Documentação Adicional

- **SOLUCAO_EXATA.md**: Explicação detalhada do algoritmo exato, complexidade e exemplos
- **SOLUCAO_APROXIMADA.md**: Explicação do algoritmo de Gonzalez, garantias teóricas e análise
- **especifica.md**: Especificação completa do trabalho

---

## ⚙️ Requisitos

- Java 8 ou superior
- Sistema operacional: Windows, Linux ou macOS

---

## 📝 Observações

- O arquivo `pmedopt.txt` contém soluções para o problema das **p-medianas**, não k-centros
- Os valores ótimos para k-centros estão na tabela do arquivo `especifica.md`
- A solução exata pode demorar muito para instâncias com N > 100