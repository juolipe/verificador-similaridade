package model;

import estruturas.HashTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;

public class Documento {
    private String nomeDoArquivo;
    private HashTable frequencias;

    public Documento(String caminhoDoArquivo) {
        this.nomeDoArquivo = caminhoDoArquivo;
        this.frequencias = new HashTable();
        logicaDeProcessamento(caminhoDoArquivo);
    }

    public String lerArquivo(String caminhoDoArquivo) {
        try {
            String texto = new String(Files.readAllBytes(Paths.get(caminhoDoArquivo)));
            return texto;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public String normalizacao(String texto) {
        texto = texto.toLowerCase();
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[^\\p{ASCII}]", "");
        texto = texto.replaceAll("[^a-z0-9\\s]", "");
        return texto;
    }

    public String[] tokenizacao(String texto) {
        return texto.split("\\s+");
    }

    public boolean removerStopWords(String palavra) {
        String[] stopwords_br = {
            "a", "o", "as", "os", "um", "uma", "uns", "umas", "ao", "aos",
            "do", "da", "dos", "das", "no", "na", "nos", "nas", "pelo", "pela", "pelos", "pelas",
            "eu", "tu", "ele", "ela", "nos", "vos", "eles", "elas", "me", "mim", "comigo",
            "te", "ti", "contigo", "se", "si", "consigo", "lhe", "lhes",
            "meu", "minha", "meus", "minhas", "teu", "tua", "teus", "tuas", "seu", "sua",
            "seus", "suas", "nosso", "nossa", "nossos", "nossas", "vosso", "vossa", "vossos", "vossas",
            "de", "em", "por", "para", "com", "sem", "sob", "sobre", "entre", "ate",
            "desde", "contra", "perante", "atraves", "alem", "dentro", "fora", "perto",
            "longe", "durante",
            "e", "mas", "ou", "porque", "pois", "que", "se", "como", "quando", "embora",
            "porem", "todavia", "contudo", "entao", "tambem", "apesar", "caso",
            "portanto", "logo",
            "ser", "estar", "ter", "haver", "ir", "vir", "fazer", "poder", "dever",
            "querer", "saber", "esta", "estao", "tem", "tenho", "e",
            "esse", "essa", "isso", "este", "esta", "isto", "aquele", "aquela", "aquilo",
            "aqueles", "aquelas", "deste", "desta", "destes", "destas", "disso", "daquilo",
            "nisto", "naquilo",
            "la", "aqui", "ali", "onde", "aonde",
            "ah", "oh", "ei", "oi", "ola", "opa", "eita", "nossa", "caramba", "poxa",
            "uau", "xi", "ih", "ue", "hein",
            "cara", "mano", "mina", "vei", "velho", "brother", "meu", "parça", "parceiro",
            "camarada", "bro", "fera", "chefe", "moleque", "garoto", "menino", "menina",
            "guri", "guria", "pia", "gajo", "gaja", "bacana", "maneiro", "massa", "show",
            "top", "legal", "beleza", "joia", "firmeza", "daora", "dahora", "responsa",
            "sinistro", "brabo", "irado", "supimpa", "bala", "zica", "animal", "monstro", "mito", "lenda"
        };

        for (String stop : stopwords_br) {
            if (stop.equals(palavra)) {
                return true;
            }
        }
        return false;
    }

    public void adicionarPalavra(String palavra) {
        if (frequencias.contains(palavra)) {
            int frequenciaAtual = frequencias.get(palavra);
            frequencias.put(palavra, frequenciaAtual + 1);
        } else {
            frequencias.put(palavra, 1);
        }
    }

    public void logicaDeProcessamento(String caminhoDoArquivo) {
        String texto = lerArquivo(caminhoDoArquivo);
        texto = normalizacao(texto);
        String[] palavras = tokenizacao(texto);

        for (String palavra : palavras) {
            if (!palavra.isEmpty() && !removerStopWords(palavra)) {
                adicionarPalavra(palavra);
            }
        }
    }

    public String getNomeDoArquivo() {
        return nomeDoArquivo;
    }

    public HashTable getFrequencias() {
        return frequencias;
    }
}