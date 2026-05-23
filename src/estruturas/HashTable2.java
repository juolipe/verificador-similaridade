package estruturas;

public class HashTable2 {

    private static final int CAPACIDADE_INICIAL = 11;

    private BucketAVL[] tabela;
    private int numElementos;

    public HashTable2() {
        this(CAPACIDADE_INICIAL);
    }

    public HashTable2(int capacidade) {
        if (capacidade < 2)
            throw new IllegalArgumentException("Capacidade mínima: 2");
        tabela = new BucketAVL[capacidade];
        for (int i = 0; i < capacidade; i++) {
            tabela[i] = new BucketAVL();
        }
        numElementos = 0;
    }

    /**
     * Hash sdbm: h = c + (h << 6) + (h << 16) - h
     * Fonte: http://www.cse.yorku.ca/~oz/hash.html
     */
    private int hash(String chave) {
        int h = 0;
        for (int i = 0; i < chave.length(); i++) {
            int c = chave.charAt(i);
            h = c + (h << 6) + (h << 16) - h;
        }
        return Math.abs(h) % tabela.length;
    }

    public void put(String chave, int valor) {
        if (chave == null)
            throw new IllegalArgumentException("Chave não pode ser null");
        int bucket = hash(chave);
        boolean eraNova = !tabela[bucket].contains(chave);
        tabela[bucket].inserir(chave, valor);
        if (eraNova) numElementos++;
    }

    public int get(String chave) {
        int bucket = hash(chave);
        return tabela[bucket].get(chave);
    }

    public boolean contains(String chave) {
        int bucket = hash(chave);
        return tabela[bucket].contains(chave);
    }

    public int remove(String chave) {
        int bucket = hash(chave);
        int valor = tabela[bucket].remover(chave);
        if (valor != 0) numElementos--;
        return valor;
    }

    public int size() { return numElementos; }
    public int capacity() { return tabela.length; }

    public void imprimirTabela() {
        System.out.println("\n── HashTable2 Encadeamento AVL sdbm (cap=" + tabela.length
                + ", elem=" + numElementos + ") ──");
        for (int i = 0; i < tabela.length; i++) {
            System.out.print("[" + i + "] ");
            if (tabela[i].tamanho() == 0) System.out.println("VAZIO");
            else System.out.println(tabela[i].toString());
        }
    }

    private static class BucketAVL {

        private No raiz;
        private int tamanho;

        private static class No {
            String chave;
            int valor;
            int hashChave;
            int altura;
            No esquerda, direita;

            No(String chave, int valor) {
                this.chave = chave;
                this.valor = valor;
                this.hashChave = chave.hashCode();
                this.altura = 1;
            }
        }

        private int altura(No n) { return n == null ? 0 : n.altura; }

        private void atualizarAltura(No n) {
            n.altura = 1 + Math.max(altura(n.esquerda), altura(n.direita));
        }

        private int fatorBalanceamento(No n) {
            return n == null ? 0 : altura(n.esquerda) - altura(n.direita);
        }

        private No rotacaoDireita(No y) {
            No x = y.esquerda;
            No t = x.direita;
            x.direita = y;
            y.esquerda = t;
            atualizarAltura(y);
            atualizarAltura(x);
            return x;
        }

        private No rotacaoEsquerda(No x) {
            No y = x.direita;
            No t = y.esquerda;
            y.esquerda = x;
            x.direita = t;
            atualizarAltura(x);
            atualizarAltura(y);
            return y;
        }

        private No balancear(No n) {
            atualizarAltura(n);
            int fb = fatorBalanceamento(n);

            if (fb > 1) {
                if (fatorBalanceamento(n.esquerda) < 0)
                    n.esquerda = rotacaoEsquerda(n.esquerda);
                return rotacaoDireita(n);
            }
            if (fb < -1) {
                if (fatorBalanceamento(n.direita) > 0)
                    n.direita = rotacaoDireita(n.direita);
                return rotacaoEsquerda(n);
            }
            return n;
        }

        public void inserir(String chave, int valor) {
            raiz = inserirRecursivo(raiz, chave, valor);
        }

        private No inserirRecursivo(No n, String chave, int valor) {
            if (n == null) {
                tamanho++;
                return new No(chave, valor);
            }

            int cmp = Integer.compare(chave.hashCode(), n.hashChave);

            if (cmp < 0) {
                n.esquerda = inserirRecursivo(n.esquerda, chave, valor);
            } else if (cmp > 0) {
                n.direita = inserirRecursivo(n.direita, chave, valor);
            } else {
                if (n.chave.equals(chave)) {
                    n.valor = valor;
                    return n;
                }
                n.direita = inserirRecursivo(n.direita, chave, valor);
            }

            return balancear(n);
        }

        public int get(String chave) {
            No n = buscarNo(raiz, chave);
            return n != null ? n.valor : 0;
        }

        public boolean contains(String chave) {
            return buscarNo(raiz, chave) != null;
        }

        private No buscarNo(No n, String chave) {
            if (n == null) return null;
            int cmp = Integer.compare(chave.hashCode(), n.hashChave);
            if (cmp < 0) return buscarNo(n.esquerda, chave);
            if (cmp > 0) return buscarNo(n.direita, chave);
            if (n.chave.equals(chave)) return n;
            return buscarNo(n.direita, chave);
        }

        public int remover(String chave) {
            No[] removido = new No[1];
            raiz = removerRecursivo(raiz, chave, removido);
            if (removido[0] != null) {
                tamanho--;
                return removido[0].valor;
            }
            return 0;
        }

        private No removerRecursivo(No n, String chave, No[] removido) {
            if (n == null) return null;
            int cmp = Integer.compare(chave.hashCode(), n.hashChave);

            if (cmp < 0) {
                n.esquerda = removerRecursivo(n.esquerda, chave, removido);
            } else if (cmp > 0) {
                n.direita = removerRecursivo(n.direita, chave, removido);
            } else {
                if (n.chave.equals(chave)) {
                    removido[0] = n;
                    if (n.esquerda == null) return n.direita;
                    if (n.direita == null) return n.esquerda;
                    No sucessor = minimo(n.direita);
                    n.chave = sucessor.chave;
                    n.valor = sucessor.valor;
                    n.hashChave = sucessor.hashChave;
                    n.direita = removerRecursivo(n.direita, sucessor.chave, new No[1]);
                } else {
                    n.direita = removerRecursivo(n.direita, chave, removido);
                }
            }
            return balancear(n);
        }

        private No minimo(No n) {
            while (n.esquerda != null) n = n.esquerda;
            return n;
        }

        public int tamanho() { return tamanho; }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            emOrdem(raiz, sb);
            return sb.toString();
        }

        private void emOrdem(No n, StringBuilder sb) {
            if (n == null) return;
            emOrdem(n.esquerda, sb);
            sb.append("(\"").append(n.chave).append("\", ").append(n.valor).append(") ");
            emOrdem(n.direita, sb);
        }
    }
}