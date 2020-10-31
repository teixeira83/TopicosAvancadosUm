import ij.IJ;
import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.process.LUT;

public class InverterRGBComLUT implements PlugIn {

    @Override
    public void run(String s) {
        IJ.run("Clown");
        ImagePlus imagem = IJ.getImage();
        ImageProcessor processador = imagem.getProcessor();

        ImagePlus canalVermelho = criarImagem8Bit("Canal Vermelho", processador);
        ImagePlus canalVerde = criarImagem8Bit("Canal Verde", processador);
        ImagePlus canalAzul = criarImagem8Bit("Canal Azul", processador);

        int pixel[] = new int[3];

        for( int i = 0; i < processador.getWidth(); i++) {
            for( int j = 0; j < processador.getHeight(); j++) {
                pixel = processador.getPixel(i, j, pixel);

                canalVermelho.getProcessor().putPixel(i, j, pixel[0]);
                canalVerde.getProcessor().putPixel(i, j, pixel[1]);
                canalAzul.getProcessor().putPixel(i, j, pixel[2]);
            }
        }

        byte[] vermelho = new byte[256];
        byte[] verde = new byte[256];
        byte[] azul = new byte[256];
        byte[] zerado = new byte[256];

        for (int i = 0; i < 256; i++) {
            vermelho[i] = (byte) i;
            verde[i] = (byte) i;
            azul[i] = (byte) i;
            zerado[i] = 0;
        }

        LUT LUTCanalVermelho = new LUT(vermelho, zerado, zerado);
        LUT LUTCanalVerde = new LUT(zerado, verde, zerado);
        LUT LUTCanalAzul = new LUT(zerado, zerado, azul);

        canalVermelho.getProcessor().setLut(LUTCanalVermelho);
        canalVerde.getProcessor().setLut(LUTCanalVerde);
        canalAzul.getProcessor().setLut(LUTCanalAzul);

        canalVermelho.show();
        canalVerde.show();
        canalAzul.show();

    }

    public ImagePlus criarImagem8Bit(String titulo, ImageProcessor processador) {
        return IJ.createImage(titulo, "8-bit", processador.getWidth(), processador.getHeight(), 1);
    }
}
