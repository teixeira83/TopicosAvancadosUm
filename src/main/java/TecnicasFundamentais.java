import ij.IJ;
import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class TecnicasFundamentais implements PlugIn {

    static String media = "Estratégia Médias";
    static String lumAnalogica = "Estratégia Luminância Analógica";
    static String lumDigital = "Estratégia Luminância Digital";

    public void run(String s) {

        IJ.run("Clown");
//        IJ.open("C:/Users/rafael/Downloads/1rgbxcmyk.jpg");
        ImagePlus imagem = IJ.getImage();
        ImageProcessor processador = imagem.getProcessor();

        if (s.equals(media)) {
            ImagePlus imagemMedia = criarImagem8Bit("Imagem Aplicada Técnica Media", processador);

            int pixel[] = {0, 0, 0};

            for (int i = 0; i < processador.getWidth(); i++) {
                for (int j = 0; j < processador.getHeight(); j++) {
                    pixel = processador.getPixel(i, j, pixel);

                    float mediaPixels = (pixel[0] + pixel[1] + pixel[2]) / 3;

                    imagemMedia.getProcessor().putPixel(i, j, (int) mediaPixels);
                }
            }
            imagemMedia.show();
        }

        if (s.equals(lumAnalogica)) {
            ImagePlus imagemLumAnalogica = criarImagem8Bit("Imagem Aplicada Técnica Luminância Analógica", processador);

            double wR = 0.299;
            double wG = 0.587;
            double wB = 0.114;

            int pixel[] = {0, 0, 0};

            for (int i = 0; i < processador.getWidth(); i++) {
                for (int j = 0; j < processador.getHeight(); j++) {
                    pixel = processador.getPixel(i, j, pixel);

                    double fatorLumAnalogica = ((wR * pixel[0]) + (wG * pixel[1]) + (wB * pixel[2]));
                    imagemLumAnalogica.getProcessor().putPixel(i, j, (int) fatorLumAnalogica);
                }
            }
            imagemLumAnalogica.show();
        }

        if(s.equals(lumDigital)) {
            ImagePlus imagemLumDigital = criarImagem8Bit("Imagem Aplicada Técnica Luminância Digital", processador);

            double wR = 0.2125;
            double wG = 0.7154;
            double wB = 0.072;

            int pixel[] = {0, 0, 0};

            for (int i = 0; i < processador.getWidth(); i++) {
                for (int j = 0; j < processador.getHeight(); j++) {
                    pixel = processador.getPixel(i, j, pixel);

                    double fatorLumDigital = ((wR * pixel[0]) + (wG * pixel[1]) + (wB * pixel[2]));
                    imagemLumDigital.getProcessor().putPixel(i, j, (int) fatorLumDigital);
                }
            }
            imagemLumDigital.show();
        }
    }

    public ImagePlus criarImagem8Bit(String titulo, ImageProcessor processador) {
        return IJ.createImage(titulo, "8-bit", processador.getWidth(), processador.getHeight(), 1);
    }
}
