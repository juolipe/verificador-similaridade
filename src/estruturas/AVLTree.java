package estruturas;

import model.Resultado;
import java.util.ArrayList;
import java.util.List;

public class AVLTree {

    private No raiz;

    private static class No {
        double similaridade;
        List<Resultado> pares;
        int altura;
        No esquerda, direita;

        No(double similaridade, Resultado resultado) {
            this.similaridade = similaridade;
            this.pares = new ArrayList<>();
            this.pares.add(resultado);
            this.altura = 1;
        }
    }

    public static class RegistroRotacoes {
        public int simplesEsquerda = 0;
        public int simplesDireita = 0;
        public int duplaEsquerda = 0;
        public int duplaDireita = 0;

        public int total() {
            return simplesEsquerda + simplesDireita + duplaEsquerda + duplaDireita;
        }

        @Override
        public String toString() {
            return "Simples(E=" + simplesEsquerda + ", D=" + simplesDireita + ")"
                 + " Duplas(E=" + duplaEsquerda + ", D=" + duplaDireita + ")"
                 + " Total=" + total();
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

    private No balancear(No n, RegistroRotacoes registro) {
        atualizarAltura(n);
        int fb = fatorBalanceamento(n);

        if (fb > 1) {
            if (fatorBalanceamento(n.esquerda) < 0) {
                n.esquerda = rotacaoEsquerda(n.esquerda);
                registro.duplaDireita++;
            } else {
                registro.simplesDireita++;
            }
            return rotacaoDireita(n);
        }

        if (fb < -1) {
            if (fatorBalanceamento(n.direita) > 0) {
                n.direita = rotacaoDireita(n.direita);
                registro.duplaEsquerda++;
            } else {
                registro.simplesEsquerda++;
            }
            return rotacaoEsquerda(n);
        }

        return n;
    }

    public RegistroRotacoes inserir(Resultado resultado) {
        RegistroRotacoes registro = new RegistroRotacoes();
        raiz = inserirRecursivo(raiz, resultado.similaridade, resultado, registro);
        return registro;
    }

    private No inserirRecursivo(No n, double similaridade, Resultado resultado, RegistroRotacoes registro) {
        if (n == null) return new No(similaridade, resultado);

        if (similaridade < n.similaridade) {
            n.esquerda = inserirRecursivo(n.esquerda, similaridade, resultado, registro);
        } else if (similaridade > n.similaridade) {
            n.direita = inserirRecursivo(n.direita, similaridade, resultado, registro);
        } else {
            n.pares.add(resultado);
            return n;
        }

        return balancear(n, registro);
    }

    public List<Resultado> buscarAcimaDe(double limiar) {
        List<Resultado> resultado = new ArrayList<>();
        buscarAcimaRecursivo(raiz, limiar, resultado);
        return resultado;
    }

    private void buscarAcimaRecursivo(No n, double limiar, List<Resultado> resultado) {
        if (n == null) return;
        if (n.similaridade >= limiar) {
            buscarAcimaRecursivo(n.esquerda, limiar, resultado);
            resultado.addAll(n.pares);
            buscarAcimaRecursivo(n.direita, limiar, resultado);
        } else {
            buscarAcimaRecursivo(n.direita, limiar, resultado);
        }
    }

    public List<Resultado> listarEmOrdem() {
        List<Resultado> lista = new ArrayList<>();
        emOrdem(raiz, lista);
        return lista;
    }

    private void emOrdem(No n, List<Resultado> lista) {
        if (n == null) return;
        emOrdem(n.esquerda, lista);
        lista.addAll(n.pares);
        emOrdem(n.direita, lista);
    }

    public List<Resultado> topK(int k) {
        List<Resultado> todos = listarEmOrdem();
        List<Resultado> resultado = new ArrayList<>();
        for (int i = todos.size() - 1; i >= Math.max(0, todos.size() - k); i--) {
            resultado.add(todos.get(i));
        }
        return resultado;
    }
}