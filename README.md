# Trabalho Prático 1 - Teoria dos Grafos e Computabilidade (TGC)

**Professor:** Zenilton Kleber Gonçalves do Patrocínio Júnior  
**Disciplina:** Teoria dos Grafos e Computabilidade  
**Instituição:** Pontifícia Universidade Católica de Minas Gerais (PUC Minas)  

**Alunos:**
- Arthur Henrique Tristão Pinto  
- Luis Henrique Ferreira Costa  
- Samuel Correia Pedrosa  

---

## 📘 Descrição do Projeto

Este trabalho prático tem como objetivo **identificar pontes em grafos não direcionados** e analisar o impacto do método de detecção de pontes no desempenho do **algoritmo de Fleury**, utilizado para encontrar **caminhos eulerianos**.

Foram implementadas duas versões do algoritmo:
- **Fleury_Naive:** realiza a verificação de pontes removendo cada aresta e testando a conectividade com uma busca em profundidade (DFS).  
- **Fleury_Tarjan:** utiliza o algoritmo de Tarjan (1974) para detectar pontes em tempo linear, otimizando a execução.

Além disso, um **gerador de grafos aleatórios** foi desenvolvido para criar instâncias de teste (Eulerianos, Semi-Eulerianos e Não-Eulerianos).

---

## 📁 Estrutura do Projeto

### **Fleury/**
Contém as implementações do algoritmo de Fleury:
- **Naive/** → Versão simples (verificação de pontes por DFS).  
- **Tarjan/** → Versão otimizada (usa o algoritmo de Tarjan).  

Cada subpasta possui:
- `Main.java` — executa automaticamente o algoritmo para **todos os grafos** de uma pasta específica (ex: `Grafos_100`, `Grafos_1000`, etc.) e gera um relatório com o tempo médio de execução.  
- `Fleury_[versão].java` — executa o algoritmo de Fleury para **um único arquivo de grafo informado pelo usuário** (via entrada manual ou argumento).  

---

### **GeradorDeGrafos/**
Contém o programa responsável por gerar grafos aleatórios conectados:
- `GeradorGrafos.java` — código principal.  
- Subpastas (`Grafos_100`, `Grafos_1000`, `Grafos_10000`, `Grafos_100000`) armazenam os grafos gerados em formato `.txt`.

---

### **Relatorios/**
Guarda os resultados dos testes de desempenho (em milissegundos) para cada tamanho de grafo:
- `relatorio_naive_[N].txt`  
- `relatorio_tarjan_[N].txt`

---

## ⚙️ Utilização

- Compile os códigos com `javac`.  
- Execute `GeradorGrafos.java` para criar os grafos de teste.  
- Rode `Main.java` direcionando a pasta com os grafos ou rode o `Fleury_[versão].java`  dentro das pastas `Naive` ou `Tarjan` para gerar os relatórios de desempenho.

