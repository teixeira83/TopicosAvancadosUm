import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Histogram;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class ExpansaoEEqualizacaoDeHistograma implements PlugIn {

    ImagePlus imagemOriginal;
    ImageProcessor processadorOriginal;
    int menorValorPossivel = 0;
    int maiorValorPossivel = 255;
    int menorValorImagem = 255;
    int maiorValorImagem = 0;

    public int getMenorValorPossivel() {
        return menorValorPossivel;
    }

    public int getMaiorValorPossivel() {
        return maiorValorPossivel;
    }

    public int getMenorValorImagem() {
        return menorValorImagem;
    }

    public void setMenorValorImagem(int menorValorImagem) {
        this.menorValorImagem = menorValorImagem;
    }

    public int getMaiorValorImagem() {
        return maiorValorImagem;
    }

    public void setMaiorValorImagem(int maiorValorImagem) {
        this.maiorValorImagem = maiorValorImagem;
    }

    public ImagePlus getImagemOriginal() {
        return imagemOriginal;
    }

    public void setImagemOriginal(ImagePlus imagemOriginal) {
        this.imagemOriginal = imagemOriginal;
    }

    public ImageProcessor getProcessadorOriginal() {
        return processadorOriginal;
    }

    public void setProcessadorOriginal(ImageProcessor processadorOriginal) {
        this.processadorOriginal = processadorOriginal;
    }

    @Override
    public void run(String s) {
        IJ.run("Clown");
        setImagemOriginal(IJ.getImage());
        setProcessadorOriginal(getImagemOriginal().getProcessor());
        IJ.run("Histogram");

        ImagePlus imagemEmEscalaCinza = transformarEmEscalaDeCinzaCalculandoMedia(getProcessadorOriginal());

        expandirHistograma(imagemEmEscalaCinza.getProcessor());
        imagemEmEscalaCinza.show();
        IJ.run("Histogram");
    }

    private ImagePlus transformarEmEscalaDeCinzaCalculandoMedia(ImageProcessor processadorOriginal){
        int pixel[] = {0, 0, 0};

        ImagePlus imagemEmEscalaCinza = IJ.createImage("Imagem Em Escala de Cinza", "8-bit", processadorOriginal.getWidth(), processadorOriginal.getHeight(), 1);

        for (int i = 0; i < processadorOriginal.getWidth(); i++) {
            for (int j = 0; j < processadorOriginal.getHeight(); j++) {
                pixel = processadorOriginal.getPixel(i, j, pixel);

                float mediaPixels = (pixel[0] + pixel[1] + pixel[2]) / 3;

                imagemEmEscalaCinza.getProcessor().putPixel(i, j, (int) mediaPixels);
            }
        }
        return imagemEmEscalaCinza;
    }

    private void expandirHistograma(ImageProcessor processador) {
        verificarMenorEMaiorValorPixelImagem(processador);

        double fator;
        double divisao;
        int soma;
        int pixel[] = {0};

        for (int i = 0; i < processador.getWidth(); i++) {
            for (int j = 0; j < processador.getHeight(); j++) {

                pixel = processador.getPixel(i, j, pixel);

                soma = getMenorValorPossivel() + ( pixel[0] - getMenorValorImagem());
                divisao = (float) ( getMaiorValorPossivel() - getMenorValorPossivel() ) / (float) ( getMaiorValorImagem() - getMenorValorImagem() );

                fator = (double) (soma * divisao);

                processador.putPixel(i, j, (int) fator);
            }
        }

    }

    private void verificarMenorEMaiorValorPixelImagem(ImageProcessor processador){
        int pixel[] = {0};

        for (int i = 0; i < processador.getWidth(); i++) {
            for (int j = 0; j < processador.getHeight(); j++) {
                pixel = processador.getPixel(i, j, pixel);

                if (pixel[0] < getMenorValorImagem()) {
                    setMenorValorImagem(pixel[0]);
                }

                if (pixel[0] > getMaiorValorImagem()) {
                    setMaiorValorImagem(pixel[0]);
                }
            }
        }
    }
}
