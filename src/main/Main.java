package main;

import model.Documento;
import model.Resultado;
import estruturas.AVLTree;
import comparador.ComparadorDeDocumentos;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import estruturas.HashTable2;

public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java main.Main <diretorio> <limiar> <modo> [argumentos]");
            return;
        }

        String diretorio = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2];

        List<Documento> documentos = new ArrayList<>();
        File pasta = new File(diretorio);
        File[] arquivos = pasta.listFiles((dir, nome) -> nome.endsWith(".txt"));

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Nenhum arquivo .txt encontrado em: " + diretorio);
            return;
        }

        for (File arquivo : arquivos) {
            documentos.add(new Documento(arquivo.getPath()));
        }

        AVLTree avl = new AVLTree();
        int totalPares = 0;
        int totalRotacoesSimples = 0;
        int totalRotacoesDuplas = 0;

        for (int i = 0; i < documentos.size(); i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                double similaridade = ComparadorDeDocumentos.calcularSimilaridade(
                        documentos.get(i), documentos.get(j));

                Resultado resultado = new Resultado(
                        documentos.get(i).getNomeDoArquivo(),
                        documentos.get(j).getNomeDoArquivo(),
                        similaridade);

                AVLTree.RegistroRotacoes registro = avl.inserir(resultado);
                totalRotacoesSimples += registro.simplesDireita + registro.simplesEsquerda;
                totalRotacoesDuplas += registro.duplaDireita + registro.duplaEsquerda;
                totalPares++;
            }
        }

        StringBuilder saida = new StringBuilder();
        saida.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
        saida.append("Total de documentos processados: ").append(documentos.size()).append("\n");
        saida.append("Total de pares comparados: ").append(totalPares).append("\n");
        saida.append("Função hash utilizada: polinomial (HashTable) e sdbm (HashTable2)\n");
        saida.append("Métrica de similaridade: Jaccard\n");
        saida.append("Rotações simples: ").append(totalRotacoesSimples).append("\n");
        saida.append("Rotações duplas: ").append(totalRotacoesDuplas).append("\n");

        if (modo.equals("lista")) {
            List<Resultado> pares = avl.buscarAcimaDe(limiar);
            saida.append("\nPares com similaridade >= ").append(limiar).append(":\n");
            saida.append("---------------------------------\n");
            if (pares.isEmpty()) {
                saida.append("Nenhum par encontrado.\n");
            } else {
                for (Resultado r : pares) {
                    saida.append(r.toString()).append("\n");
                }
            }

            List<Resultado> todos = avl.listarEmOrdem();
            if (!todos.isEmpty()) {
                saida.append("\nPares com menor similaridade:\n");
                saida.append("---------------------------------\n");
                saida.append(todos.get(0).toString()).append("\n");
            }

        } else if (modo.equals("topK")) {
            if (args.length < 4) {
                System.out.println("Modo topK requer o valor de K.");
                return;
            }
            int k = Integer.parseInt(args[3]);
            List<Resultado> top = avl.topK(k);
            saida.append("\nTop ").append(k).append(" pares mais similares:\n");
            saida.append("---------------------------------\n");
            for (Resultado r : top) {
                saida.append(r.toString()).append("\n");
            }

        } else if (modo.equals("busca")) {
            if (args.length < 5) {
                System.out.println("Modo busca requer dois nomes de arquivo.");
                return;
            }
            String nomeDoc1 = args[3];
            String nomeDoc2 = args[4];

            Documento doc1 = null, doc2 = null;
            for (Documento d : documentos) {
                if (d.getNomeDoArquivo().endsWith(nomeDoc1)) doc1 = d;
                if (d.getNomeDoArquivo().endsWith(nomeDoc2)) doc2 = d;
            }

            if (doc1 == null || doc2 == null) {
                System.out.println("Arquivo não encontrado.");
                return;
            }

            double sim = ComparadorDeDocumentos.calcularSimilaridade(doc1, doc2);
            saida.append("\nComparando: ").append(nomeDoc1).append(" <-> ").append(nomeDoc2).append("\n");
            saida.append("Similaridade calculada: ").append(String.format("%.2f", sim)).append("\n");
            saida.append("Métrica utilizada: Jaccard\n");

        } else {
            System.out.println("Modo inválido. Use: lista, topK ou busca.");
            return;
        }

        String resultado = saida.toString();
        System.out.println(resultado);

        try (PrintWriter writer = new PrintWriter(new FileWriter("resultado.txt"))) {
            writer.print(resultado);
        } catch (IOException e) {
            System.err.println("Erro ao gravar resultado.txt: " + e.getMessage());
        }

        // Descomente para ver distribuição das hash tables

        /*System.out.println("\n=== DISTRIBUIÇÃO HASHTABLE 1 (polinomial) ===");
        documentos.get(0).getFrequencias().imprimirTabela();*/

        /*System.out.println("\n=== DISTRIBUIÇÃO HASHTABLE 2 (sdbm) ===");
        HashTable2 ht2 = new HashTable2();
        for (String palavra : documentos.get(0).getFrequencias().getChaves()) {
        ht2.put(palavra, 1);
        }
        ht2.imprimirTabela();*/
    }
}