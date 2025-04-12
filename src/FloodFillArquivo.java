import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class FloodFillArquivo {

    public void floodFillFila(BufferedImage imagem, int linha, int coluna, int corNova) {

        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        int corAntiga = imagem.getRGB(coluna, linha);

        if (corAntiga == corNova) {
            return;
        }

        boolean[][] visto = new boolean[largura][altura];
        Queue<int[]> fila = new LinkedList<>();

        fila.offer(new int[]{linha, coluna});
        visto[linha][coluna] = true;
        imagem.setRGB(coluna, linha, corNova);

        int[] mlinhas = {-1, 1, 0, 0}; //movimento linha, vertical
        int[] mcolunas = {0, 0, -1, 1}; //movimento coluna, horizontal

        while (!fila.isEmpty()) {
            int[] pixel = fila.poll();
            int x = pixel[0];
            int y = pixel[1];

            for (int i = 0; i < 4; i++) {
                int novaLinha = x + mlinhas[i];
                int novaColuna = y + mcolunas[i];

                if (novaLinha >= 0 && novaLinha < largura && novaColuna >= 0 && novaColuna < altura && novaColuna < largura &&
                        imagem.getRGB(novaLinha, novaColuna) == corAntiga && !visto[novaLinha][novaColuna]) {
                    imagem.setRGB(novaLinha, novaColuna, corNova);
                    visto[novaLinha][novaColuna] = true;
                    fila.offer(new int[]{novaLinha, novaColuna});
                }
            }
        }
    }
    public void floodFillPilha(BufferedImage imagem, int linha, int coluna, int corNova) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        int corAntiga = imagem.getRGB(coluna, linha);

        System.out.println("Flood Fill Pilha iniciado em: (" + linha + ", " + coluna + ")" + " cor antiga: " + corAntiga + " cor nova: " + corNova);

        if (corAntiga == corNova) {
            System.out.println("Cor antiga é igual à cor nova. Retornando.");
            return;
        }

        boolean[][] visto = new boolean[altura][largura]; // Correção: Dimensões invertidas
        Stack<int[]> pilha = new Stack<>();

        pilha.push(new int[]{linha, coluna});
        visto[linha][coluna] = true;
        imagem.setRGB(coluna, linha, corNova);
        System.out.println("Pixel inicial [" + linha + ", " + coluna + "] marcado como visto e cor alterada.");

        int[] mlinhas = {-1, 1, 0, 0}; //movimento linha, vertical
        int[] mcolunas = {0, 0, -1, 1}; //movimento coluna, horizontal

        while (!pilha.isEmpty()) {
            int[] pixel = pilha.pop();
            int x = pixel[0];
            int y = pixel[1];

            for (int i = 0; i < 4; i++) {
                int novaLinha = x + mlinhas[i];
                int novaColuna = y + mcolunas[i];

                // Correção nas condições de limite e acesso à imagem
                if (novaLinha >= 0 && novaLinha < altura && novaColuna >= 0 && novaColuna < largura &&
                        imagem.getRGB(novaColuna, novaLinha) == corAntiga && !visto[novaLinha][novaColuna]) {
                    imagem.setRGB(novaColuna, novaLinha, corNova);
                    visto[novaLinha][novaColuna] = true;
                    pilha.push(new int[]{novaLinha, novaColuna});
                }
            }
        }
    }

    public BufferedImage carregarImagem(String caminhoArquivo) {
        BufferedImage imagem = null;
        try {
            File arquivo = new File(caminhoArquivo);
            imagem = ImageIO.read(arquivo);
            if (imagem == null) {
                System.out.println("Erro ao carregar a imagem. Arquivo invalido ou formato nao suportado!");
            } else {
                System.out.println("Imagem carregada com sucesso.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar a imagem." + e.getMessage());
        }
        return imagem;
    }

    public void salvarImagem(BufferedImage imagem, String caminhoArquivo) {
        try {
            String formato = caminhoArquivo.substring(caminhoArquivo.lastIndexOf(".") + 1);
            File saidaArquivo = new File(caminhoArquivo);
            ImageIO.write(imagem, "png", saidaArquivo);
            System.out.println("Imagem salva com sucesso em: " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar a imagem." + e.getMessage());
        }
    }
    public void main(String[] args) {
        FloodFillArquivo flood = new FloodFillArquivo();
        String caminhoEntrada = "/Users/felps/IdeaProjects/Flood/imagem/frame.png";
        String caminhoSaidaFila = "/Users/felps/IdeaProjects/Flood/imagem_saida_Fila.png";
        String caminhoSaidaPilha = "/Users/felps/IdeaProjects/Flood/imagem_saida_Pilha.png";
        int linhaInicio = 7;
        int colunaInicio = 7;
        int corNova = 0xFF800080; // Cor nova (Roxo)

        System.out.println("Coordenadas iniciais: (" + linhaInicio + ", " + colunaInicio + ")");

        BufferedImage imagemOriginal = flood.carregarImagem(caminhoEntrada);

        if (imagemOriginal != null) {
            // Verifica se a imagem tem as dimensões esperadas
            int larguraOriginal = imagemOriginal.getWidth();
            int alturaOriginal = imagemOriginal.getHeight();
            System.out.println("Dimensoes da imagem carregada: " + larguraOriginal + "x" + alturaOriginal);
            if (larguraOriginal != 8 || alturaOriginal != 8) {
                System.err.println("A imagem carregada não tem as dimensões esperadas (8x8).");
                return;
            }

            //Fila
            BufferedImage imagemFila = copiarImagem(imagemOriginal);
            flood.floodFillFila(imagemFila, linhaInicio, colunaInicio, corNova);
            flood.salvarImagem(imagemFila, caminhoSaidaFila);

            //Pilha
            BufferedImage imagemPilha = copiarImagem(imagemOriginal);
            flood.floodFillPilha(imagemPilha, linhaInicio, colunaInicio, corNova);
            flood.salvarImagem(imagemPilha, caminhoSaidaPilha);
        }
    }
    private static BufferedImage copiarImagem(BufferedImage original) {
        BufferedImage copia = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        copia.getGraphics().drawImage(original, 0, 0, null);
        return copia;
    }
}



