# Verificador de Similaridade de Textos

**Aluno:** Júlia de Oliveira Pestana  
**Disciplina:** Estrutura de Dados II   
**Semestre:** 2026/1  

## Como o projeto funciona

O sistema lê arquivos de texto, normaliza o conteúdo (letras minúsculas, sem pontuação e sem acentos), remove stopwords (palavras comuns como "a", "de", "para" que não contribuem para a análise) e tokeniza o texto, quebrando-o em palavras individuais. Em seguida, compara os tokens entre os documentos: quanto mais tokens em comum, maior a similaridade. Uma similaridade alta pode indicar plágio.

## Como compilar

```bash
cd src
javac **/*.java
```

## Como executar

```bash
# Lista todos os pares com similaridade acima do limiar
java main.Main documentos 0.5 lista

# Exibe os 3 pares mais similares
java main.Main documentos 0.0 topK 3

# Compara dois arquivos específicos
java main.Main documentos 0.0 busca doc1.txt doc2.txt
```

Os resultados são impressos no terminal e salvos automaticamente em `resultado.txt`.


## Estrutura do projeto

src/
├── comparador/   # cálculo de similaridade (Jaccard)
├── documentos/   # arquivos .txt usados nos testes
├── estruturas/   # HashTable, HashTable2 e AVLTree
├── main/         # ponto de entrada
├── model/        # Documento e Resultado
└── resultados/   # saída gerada


## Decisões técnicas

**Hash 1 — polinomial (base 31):**  
Escolhida pela simplicidade e boa distribuição em texto. É a mesma função usada internamente pelo Java para `String.hashCode()`.

**Hash 2 — sdbm:**  
Escolhida por ter origem e comportamento distintos da polinomial, permitindo a comparação de distribuição de colisões exigida pelo trabalho.  
Fonte: http://www.cse.yorku.ca/~oz/hash.html

**Similaridade — Jaccard:**  
Escolhida por ser intuitiva e de fácil implementação. Calcula a proporção de palavras em comum entre dois documentos sem necessidade de vetores ou produto escalar.

**Stopwords:**  
Lista de palavras comuns do português removidas antes da comparação para evitar falsos positivos de similaridade.  
Fonte: https://blog.trifenol.com/lista-completa-das-stop-words-em-portugues/


## Referências

CORMEN, T. H. et al. *Algoritmos: Teoria e Prática*. 3. ed. Rio de Janeiro: Elsevier, 2012.

MANNING, C. D.; RAGHAVAN, P.; SCHÜTZE, H. *Introduction to Information Retrieval*. Cambridge University Press, 2008.

OZAN, S. Hash Functions. Disponível em: http://www.cse.yorku.ca/~oz/hash.html. Acesso em: maio 2026.

TRIFENOL. Lista completa das stop words em português. Disponível em: https://blog.trifenol.com/lista-completa-das-stop-words-em-portugues/. Acesso em: maio 2026.
