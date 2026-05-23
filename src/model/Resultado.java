package model;

public class Resultado {
    public String nomeDoc1;
    public String nomeDoc2;
    public double similaridade;

    public Resultado(String nomeDoc1, String nomeDoc2, double similaridade) {
        this.nomeDoc1 = nomeDoc1;
        this.nomeDoc2 = nomeDoc2;
        this.similaridade = similaridade;
    }

    @Override
    public String toString() {
        return nomeDoc1 + " <-> " + nomeDoc2 + " = " + String.format("%.2f", similaridade);
    }
}