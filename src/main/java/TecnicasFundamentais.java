import ij.IJ;
import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class TecnicasFundamentais implements PlugIn {

    static String media = "Estratégia Médias";
    static String lum = "Estratégia Lum";

    public void run(String s) {

        IJ.run("Clown");
        ImagePlus imagem = IJ.getImage();
        ImageProcessor processador = imagem.getProcessor();

        if (s.equals(media)) {

           ImagePlus novaImagem = criarImagem("Nova Imagem Aplicada Técnica Médias", processador);

           int pixel[] = {0, 0, 0};
           for( int i = 0; i  < processador.getWidth(); i++) {
               for( int j = 0; j < processador.getHeight(); j++) {
                   pixel  = processador.getPixel(i, j, pixel);
                   float novoValorPixel = ( pixel[0] + pixel[1] + pixel[2] ) / 3;
                   novaImagem.getProcessor().putPixel(i, j, (int) novoValorPixel);
               }
           }
           novaImagem.show();
       }

       if (s.equals(lum)) {
           System.out.println("TO AQI");
           double wR = 0.299;
           double wG = 0.587;
           double wB = 0.114;

           ImagePlus novaImagem = criarImagem("Nova Imagem Aplicada Técnica Lum", processador);

           int pixel[] = {0, 0, 0};
           for( int i = 0; i  < processador.getWidth(); i++) {
               for( int j = 0; j < processador.getHeight(); j++) {
                   pixel  = processador.getPixel(i, j, pixel);
                   double novoValorPixel = ( wR * pixel[0]) + ( wG * pixel[1] ) + ( wB * pixel[2] );
                   novaImagem.getProcessor().putPixel(i, j, (int) novoValorPixel);
               }
           }
           novaImagem.show();
       }
    }

    public ImagePlus criarImagem(String titulo, ImageProcessor processador) {
        return IJ.createImage(titulo, "8-bit", processador.getWidth(), processador.getHeight(), 1);
    }
}
