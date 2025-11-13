# Especificação - Problema dos K-Centros

## 📋 Descrição do Problema

O problema dos k-centros é uma tarefa clássica de análise de dados relacionada às técnicas de clustering. 

**Definição:** Dado um grafo completo com custos nas arestas (respeitando a desigualdade triangular) e um inteiro positivo k, encontrar um conjunto de k vértices (centros) que minimize a maior distância de qualquer vértice ao conjunto de centros.

### Formulação Formal

**Problema dos k-centros:** Dada uma coleção de V pontos com distâncias definidas pela função `d : V × V → R+`, encontrar um conjunto `C ⊆ V` com `|C| ≤ k` que minimize a maior distância de qualquer ponto ao centro mais próximo.

### Aplicações

#### 🛒 Categorização de Consumidores
- Divisão automática em perfis baseada em padrões de compra
- Agrupamento homogêneo por similaridade

#### 🏥 Localização de Facilidades  
- Posicionamento ótimo de centros de tratamento
- Minimização do tempo máximo de transporte

#### 📊 Outras Aplicações
- Categorização de alunos
- Análise de usuários em redes sociais
- Agrupamento de deputados por votações

## 🎯 Descrição do Trabalho

### Implementações Requeridas

#### 1. Solução Exata
- Obtém solução ótima
- Viável para instâncias pequenas

#### 2. Solução Aproximada
- Soluções razoáveis para instâncias grandes
- Eficiente computacionalmente

### 📊 Dataset

**Fonte:** OR-Library - 40 instâncias do problema das p-medianas
- **URL:** http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files
- **Arquivos:** pmed1.txt até pmed40.txt

### 📈 Análise Comparativa

Avaliar diferenças de desempenho:
- **Eficácia:** qualidade da solução
- **Eficiência:** tempo de execução
- **Escalabilidade:** comportamento com aumento do tamanho

## 📦 Entrega

### Códigos
- Implementações das duas soluções
- Scripts de preparação das instâncias
- Código de análise experimental

### Relatório
- **Formato:** PDF (obrigatório LaTeX)
- **Conteúdo:** 
  - Detalhes das implementações
  - Metodologia experimental
  - Resultados e análises
- **Entrega:** Código-fonte LaTeX incluído

### ⚠️ Observações Importantes
- Grupos de até 3 alunos
- Cópias serão zeradas
- Entrega individual por grupo
- Fontes LaTeX obrigatórios

## 📊 Instâncias de Teste

| Instância | \|V\| | k | Raio | Instância | \|V\| | k | Raio |
|-----------|-------|---|------|-----------|-------|---|------|
| 01 | 100 | 5 | 127 | 21 | 500 | 5 | 40 |
| 02 | 100 | 10 | 98 | 22 | 500 | 10 | 38 |
| 03 | 100 | 10 | 93 | 23 | 500 | 50 | 22 |
| 04 | 100 | 20 | 74 | 24 | 500 | 100 | 15 |
| 05 | 100 | 33 | 48 | 25 | 500 | 167 | 11 |
| 06 | 200 | 5 | 84 | 26 | 600 | 5 | 38 |
| 07 | 200 | 10 | 64 | 27 | 600 | 10 | 32 |
| 08 | 200 | 20 | 55 | 28 | 600 | 60 | 18 |
| 09 | 200 | 40 | 37 | 29 | 600 | 120 | 13 |
| 10 | 200 | 67 | 20 | 30 | 600 | 200 | 9 |
| 11 | 300 | 5 | 59 | 31 | 700 | 5 | 30 |
| 12 | 300 | 10 | 51 | 32 | 700 | 10 | 29 |
| 13 | 300 | 30 | 35 | 33 | 700 | 70 | 15 |
| 14 | 300 | 60 | 26 | 34 | 700 | 140 | 11 |
| 15 | 300 | 100 | 18 | 35 | 800 | 5 | 30 |
| 16 | 400 | 5 | 47 | 36 | 800 | 10 | 27 |
| 17 | 400 | 10 | 39 | 37 | 800 | 80 | 15 |
| 18 | 400 | 40 | 28 | 38 | 900 | 5 | 29 |
| 19 | 400 | 80 | 18 | 39 | 900 | 10 | 23 |
| 20 | 400 | 133 | 13 | 40 | 900 | 90 | 13 |
