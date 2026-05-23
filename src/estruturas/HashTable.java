package estruturas;

public class HashTable {

    private static final int CAPACIDADE_INICIAL = 11;
    private static final double FATOR_CARGA_MAX = 0.75;

    private Entry[] tabela;
    private int numElementos;

    static class Entry {
        String chave;
        int valor;
        int status;

        Entry(String chave, int valor) {
            this.chave = chave;
            this.valor = valor;
            this.status = 1;
        }
    }

    public HashTable() {
        this(CAPACIDADE_INICIAL);
    }

    public HashTable(int capacidade) {
        if (capacidade < 2)
            throw new IllegalArgumentException("Capacidade mínima: 2");
        this.tabela = new Entry[capacidade];
        this.numElementos = 0;
    }

    /**
     * Hash polinomial: h = (h * 31 + c) % capacidade
     * Fonte: algoritmo clássico usado internamente pelo Java para String.hashCode()
     */
    private int hash(String chave) {
        int h = 0;
        for (int i = 0; i < chave.length(); i++) {
            h = (h * 31 + chave.charAt(i)) % tabela.length;
        }
        return Math.abs(h);
    }

    private int procurarPosicao(String chave) {
        int h = hash(chave);
        int capacidade = tabela.length;
        int primeiraCandidata = -1;

        for (int i = 0; i < capacidade; i++) {
            int pos = (h + i) % capacidade;

            if (tabela[pos] == null) {
                return primeiraCandidata != -1 ? primeiraCandidata : pos;
            }
            if (tabela[pos].status == 1 && tabela[pos].chave.equals(chave)) {
                return pos;
            }
            if (tabela[pos].status == -1 && primeiraCandidata == -1) {
                primeiraCandidata = pos;
            }
        }

        return primeiraCandidata;
    }

    public void put(String chave, int valor) {
        if (chave == null)
            throw new IllegalArgumentException("Chave não pode ser null");

        if ((double) (numElementos + 1) / tabela.length > FATOR_CARGA_MAX) {
            rehash();
        }

        int i = procurarPosicao(chave);

        if (tabela[i] != null && tabela[i].status == 1 && tabela[i].chave.equals(chave)) {
            tabela[i].valor = valor;
        } else {
            tabela[i] = new Entry(chave, valor);
            numElementos++;
        }
    }

    public int get(String chave) {
        int i = procurarPosicao(chave);
        if (tabela[i] != null && tabela[i].status == 1 && tabela[i].chave.equals(chave)) {
            return tabela[i].valor;
        }
        return 0;
    }

    public boolean contains(String chave) {
        int i = procurarPosicao(chave);
        return tabela[i] != null && tabela[i].status == 1 && tabela[i].chave.equals(chave);
    }

    public int remove(String chave) {
        int i = procurarPosicao(chave);
        if (tabela[i] != null && tabela[i].status == 1 && tabela[i].chave.equals(chave)) {
            int valor = tabela[i].valor;
            tabela[i].status = -1;
            numElementos--;
            return valor;
        }
        return 0;
    }

    public String[] getChaves() {
        String[] chaves = new String[numElementos];
        int idx = 0;
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null && tabela[i].status == 1) {
                chaves[idx++] = tabela[i].chave;
            }
        }
        return chaves;
    }

    private void rehash() {
        Entry[] velhaTabela = tabela;
        int novaCapacidade = proximoPrimo(velhaTabela.length * 2);
        tabela = new Entry[novaCapacidade];
        numElementos = 0;

        for (Entry entry : velhaTabela) {
            if (entry != null && entry.status == 1) {
                put(entry.chave, entry.valor);
            }
        }
    }

    private int proximoPrimo(int n) {
        if (n <= 2) return 2;
        if (n % 2 == 0) n++;
        while (!ehPrimo(n)) n += 2;
        return n;
    }

    private boolean ehPrimo(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public int size() { return numElementos; }
    public int capacity() { return tabela.length; }
    public double getFatorCarga() { return (double) numElementos / tabela.length; }
    public Entry[] getTabela() { return tabela; }

    public void imprimirTabela() {
        System.out.println("\n── HashTable Sondagem Linear (cap=" + tabela.length
                + ", elem=" + numElementos + ", fc=" + String.format("%.2f", getFatorCarga()) + ") ──");
        for (int i = 0; i < tabela.length; i++) {
            System.out.print("[" + i + "] ");
            if (tabela[i] == null) System.out.println("VAZIO");
            else if (tabela[i].status == -1) System.out.println("REMOVIDO");
            else System.out.println("(\"" + tabela[i].chave + "\", " + tabela[i].valor + ")");
        }
    }
}