public class Documento {
    private String nomeDoArquivo;
    private HashTable frequencias;

    public Documento (String caminhoDoArquivo) {
        this.nomeDoArquivo = caminhoDoArquivo;
        this.frequencias = new HashTable;
        logicaDeProcessamento(caminhoDoArquivo);
    }

    public void lerArquivo (String caminhoDoArquivo){
        try {
            String texto = new String(Files.readAllBytes(file.caminhoDoArquivo()));
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void normalizacao (String texto){
        texto = texto.toLowerCase();
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        
    }

    public void logicaDeProcessamento (String caminhoDoArquivo) {
    
    }
}