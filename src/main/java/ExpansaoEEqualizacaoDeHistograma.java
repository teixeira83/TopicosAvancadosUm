import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class ExpansaoEEqualizacaoDeHistograma implements PlugIn {

    ImagePlus imagemOriginal;
    ImageProcessor processadorOriginal;
    int menorValorPossivel = 0;
    int maiorValorPossivel = 255;
    int menorValorImagem = 255;
    int maiorValorImagem = 0;
    Map<Integer, Integer> dicionarioHistograma = new HashMap<Integer, Integer>();
    Map<Integer, Integer> dicionarioHistogramaEqualizado = new HashMap<Integer, Integer>();
    int totalDeElementosPixel = 0;


    public int getTotalDeElementosPixel() {
        return totalDeElementosPixel;
    }

    public void addTotalDeElementosPixel() {
        this.totalDeElementosPixel += 1;
    }


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
        IJ.run("Cell Colony");
        setImagemOriginal(IJ.getImage());
        setProcessadorOriginal(getImagemOriginal().getProcessor());
        IJ.run("Histogram");
        ImagePlus imagemEmEscalaCinzaExpandir = transformarEmEscalaDeCinzaCalculandoMedia(getProcessadorOriginal());
        expandirHistograma(imagemEmEscalaCinzaExpandir.getProcessor());
        imagemEmEscalaCinzaExpandir.show();
        IJ.run("Histogram");

        IJ.run("Nile Bend");
        setImagemOriginal(IJ.getImage());
        setProcessadorOriginal(getImagemOriginal().getProcessor());
        IJ.run("Histogram");
        ImagePlus imagemEmEscalaCinzaEqualizar = transformarEmEscalaDeCinzaCalculandoMedia(getProcessadorOriginal());
        equalizarHistograma(imagemEmEscalaCinzaEqualizar.getProcessor());
        imagemEmEscalaCinzaEqualizar.show();
        IJ.run("Histogram");
    }

    private ImagePlus transformarEmEscalaDeCinzaCalculandoMedia(ImageProcessor processadorOriginal){
        int pixel[] = {0, 0, 0};

        ImagePlus imagemEmEscalaCinza = IJ.createImage("Imagem Em Escala de Cinza", "8-bit", getProcessadorOriginal().getWidth(), getProcessadorOriginal().getHeight(), 1);

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

    private void equalizarHistograma(ImageProcessor processador) {
        contarIntensidadePixels(processador);
        calcularValoresPixels(processador);
        trocarValoresPixels(processador);
    }

    private void contarIntensidadePixels(ImageProcessor processador) {

        int[] pixel = {0};
        for (int i = 0; i < processador.getWidth(); i++) {
            for (int j = 0; j < processador.getHeight(); j++) {

                pixel = processador.getPixel(i, j, pixel);

                if( dicionarioHistograma.get(pixel[0]) != null) {
                    dicionarioHistograma.put(pixel[0], dicionarioHistograma.get(pixel[0]) + 1);
                } else {
                    dicionarioHistograma.put(pixel[0], 1);
                    addTotalDeElementosPixel();
                }
            }
        }
    }
    private void calcularValoresPixels(ImageProcessor processador) {

        Iterator<Integer> numeros = dicionarioHistograma.keySet().iterator();

        float probabilidadeAcumulada = 0;
        while (numeros.hasNext()) {
            int intensidade = numeros.next();
            int quantidadeDePixels = dicionarioHistograma.get(intensidade);
            float probabilidade = quantidadeDePixels / (float) getTotalDeElementosPixel();

            probabilidadeAcumulada += probabilidade;

            dicionarioHistogramaEqualizado.put(intensidade, (int) ( probabilidadeAcumulada * 0.1 ) );
        }
    }

    private void trocarValoresPixels(ImageProcessor processador) {
        int[] pixel = {0};

        for (int i = 0; i < processador.getWidth(); i++) {
            for (int j = 0; j < processador.getHeight(); j++) {
                pixel = processador.getPixel(i, j, pixel);

                if (dicionarioHistogramaEqualizado.get(pixel[0]) != null) {
                    processador.putPixel(i, j, dicionarioHistogramaEqualizado.get(pixel[0]));
                }
            }
        }
    }
}
