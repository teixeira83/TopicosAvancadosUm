import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import ij.plugin.PlugIn;

import java.awt.*;


public class AjustarBrilhoContrasteESolarizacao implements PlugIn, DialogListener {

    private ImagePlus imagemOriginal;
    private ImageProcessor processadorOriginal;

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

    public void run(String s) {
        apresentarInterfaceGrafica();
    }

    public void apresentarInterfaceGrafica() {
        IJ.run("Clown");

        setImagemOriginal(IJ.getImage());
        setProcessadorOriginal(imagemOriginal.getProcessor());

        GenericDialog interfaceGrafica = new GenericDialog("Ajustar Imagem");
        interfaceGrafica.addDialogListener(this);

        interfaceGrafica.addSlider("Configurar Brilho: ", -255, 255, 0, 1);
        interfaceGrafica.addSlider("Configurar Contraste: ", -255, 255, 0, 1);
        interfaceGrafica.addMessage("Configurar Solarização nos pixels com valor de...");
        interfaceGrafica.addSlider("No mínimo: ", 0, 255, 0, 1);
        interfaceGrafica.addSlider("No máximo: ", 0, 255, 255, 1);

        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {

                ImageProcessor processadorAuxiliar = getProcessadorOriginal().createProcessor(getImagemOriginal().getWidth(), getProcessadorOriginal().getHeight());
                ajustarImagem(interfaceGrafica, getProcessadorOriginal(), processadorAuxiliar);
                getImagemOriginal().setProcessor(processadorAuxiliar);
                getImagemOriginal().updateAndDraw();
            }
        }
    }

    @Override
    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent awtEvent) {
        if (interfaceGrafica.wasCanceled()) return false;

        ImageProcessor processadorAuxiliar = getProcessadorOriginal().createProcessor(getImagemOriginal().getWidth(), getProcessadorOriginal().getHeight());

        ajustarImagem(interfaceGrafica, getProcessadorOriginal(), processadorAuxiliar);

        getImagemOriginal().setProcessor(processadorAuxiliar);

        getImagemOriginal().updateAndDraw();

        return true;
    }

    private void ajustarImagem(GenericDialog interfaceGrafica, ImageProcessor processadorOriginal, ImageProcessor processadorAuxiliar) {

        int sliderBrilho = (int) interfaceGrafica.getNextNumber();
        int sliderContraste = (int) interfaceGrafica.getNextNumber();
        int sliderSolarizacaoMinima = (int) interfaceGrafica.getNextNumber();
        int sliderSolarizacaoMaxima = (int) interfaceGrafica.getNextNumber();

        ajustarBrilho(processadorAuxiliar, sliderBrilho);

        ajustarContraste(processadorAuxiliar, sliderContraste);
        ajustarSolarizacao(processadorAuxiliar, sliderSolarizacaoMinima, sliderSolarizacaoMaxima);
    }

    private int[] tratarPixel(int[] pixel) {
        for (int i = 0; i < 3; i++) {
            if (pixel[i] < 0) {
                pixel[i] = 0;
            }
            if (pixel[i] > 255) {
                pixel[i] = 255;
            }
        }
        return pixel;
    }

    private void ajustarBrilho(ImageProcessor processadorAuxiliar, int sliderBrilho) {
        int pixel[] = {0, 0, 0};

        for (int i = 0; i < getProcessadorOriginal().getWidth(); i++) {
            for (int j = 0; j < getProcessadorOriginal().getHeight(); j++) {
                pixel = getProcessadorOriginal().getPixel(i, j, pixel);

                pixel[0] = pixel[0] - sliderBrilho;
                pixel[1] = pixel[1] - sliderBrilho;
                pixel[2] = pixel[2] - sliderBrilho;

                pixel = tratarPixel(pixel);

                processadorAuxiliar.putPixel(i, j, pixel);
            }
        }
    }

    private void ajustarContraste(ImageProcessor processadorAuxiliar, int sliderContraste) {
        int pixel[] = {0, 0, 0};

        float fator = (float) (259 * (sliderContraste + 255)) / (float) (255 * (259 - sliderContraste));

        for (int i = 0; i < processadorAuxiliar.getWidth(); i++) {
            for (int j = 0; j < processadorAuxiliar.getHeight(); j++) {
                pixel = processadorAuxiliar.getPixel(i, j, pixel);

                pixel[0] = (int) (fator * (pixel[0] - 128)) + 128;
                pixel[1] = (int) (fator * (pixel[1] - 128)) + 128;
                pixel[2] = (int) (fator * (pixel[2] - 128)) + 128;

                pixel = tratarPixel(pixel);

                processadorAuxiliar.putPixel(i, j, pixel);
            }
        }
    }

    private void ajustarSolarizacao(ImageProcessor processadorAuxiliar, int sliderSolarizacaoMinima, int sliderSolarizacaoMaxima) {

        int pixel[] = {0, 0, 0};

        for (int i = 0; i < processadorAuxiliar.getWidth(); i++) {
            for (int j = 0; j < processadorAuxiliar.getHeight(); j++) {
                pixel = processadorAuxiliar.getPixel(i, j, pixel);

                for (int p = 0; p < 3; p++) {
                    if ((pixel[p] > sliderSolarizacaoMinima) && (pixel[p] < sliderSolarizacaoMaxima)) {
                        pixel[p] = 255 - pixel[p];
                    }
                }
                processadorAuxiliar.putPixel(i, j, pixel);
            }
        }
    }

}
