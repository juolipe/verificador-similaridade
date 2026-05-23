package comparador;

import model.Documento;
import estruturas.HashTable;
import java.util.ArrayList;
import java.util.List;

public class ComparadorDeDocumentos {

    /**
     * Calcula a similaridade de Jaccard entre dois documentos.
     * Jaccard(A, B) = |A ∩ B| / |A ∪ B|
     */
    public static double calcularSimilaridade(Documento doc1, Documento doc2) {
        HashTable freq1 = doc1.getFrequencias();
        HashTable freq2 = doc2.getFrequencias();

        List<String> palavras1 = getPalavras(freq1);
        List<String> palavras2 = getPalavras(freq2);

        int intersecao = 0;
        for (String palavra : palavras1) {
            if (freq2.contains(palavra)) {
                intersecao++;
            }
        }

        int uniao = palavras1.size() + palavras2.size() - intersecao;
        if (uniao == 0) return 0.0;

        return (double) intersecao / uniao;
    }

    private static List<String> getPalavras(HashTable tabela) {
        List<String> palavras = new ArrayList<>();
        for (String chave : tabela.getChaves()) {
            palavras.add(chave);
        }
        return palavras;
    }
}