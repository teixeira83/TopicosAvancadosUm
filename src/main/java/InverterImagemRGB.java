import ij.IJ;
import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class InverterImagemRGB implements PlugIn {

    public void run(String arg) {
        IJ.run("Clown");
        ImagePlus imagem = IJ.getImage();
        ImageProcessor processador = imagem.getProcessor();

        ImagePlus imagemVermelha = criarImagem("Imagem Vermelha", processador);
        ImagePlus imagemVerde = criarImagem("Imagem Verde", processador);
        ImagePlus imagemAzul = criarImagem("Imagem Azul", processador);

        int pixel[] = {0, 0, 0};
        for( int i = 0; i  < processador.getWidth(); i++) {
            for( int j = 0; j < processador.getHeight(); j++) {
                pixel  = processador.getPixel(i, j, pixel);
                imagemVermelha.getProcessor().putPixel(i, j, pixel[0]);
                imagemVerde.getProcessor().putPixel(i, j, pixel[1]);
                imagemAzul.getProcessor().putPixel(i, j, pixel[2]);
            }
        }

        imagemVermelha.show();
        imagemVerde.show();
        imagemAzul.show();
    }

    public ImagePlus criarImagem(String titulo, ImageProcessor processador) {
        return IJ.createImage(titulo, "8-bit", processador.getWidth(), processador.getHeight(), 1);
    }
}