import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.plugin.PlugIn;
import ij.gui.GenericDialog;

import java.awt.*;

public class Threshold implements PlugIn, DialogListener {

    private ImagePlus imagemOriginal;

    private ImagePlus getImagemOriginal() {
        return imagemOriginal;
    }

    private void setImagemOriginal(ImagePlus imagemOriginal) {
        this.imagemOriginal = imagemOriginal;
    }

    public void run(String s) {

        setImagemOriginal(IJ.getImage());

        if ( getImagemOriginal().getType() != 0 ) {
            converterEscalaCinzaUsandoMedia();
        }
        getImagemOriginal().getProcessor().snapshot();

        apresentarInterfaceGrafica();
    }

    private void apresentarInterfaceGrafica() {

        GenericDialog interfaceGrafica = new GenericDialog("Image Adjust");
        interfaceGrafica.addDialogListener(this);
        interfaceGrafica.addMessage("Ajustar Threshold");
        interfaceGrafica.addSlider("Threshold: ", 0, 255, 0, 1);
        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("Plugin cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                atualizarImagem(interfaceGrafica);
            }
        }
    }

    private void converterEscalaCinzaUsandoMedia() {

        ImagePlus novaImagem = IJ.createImage("Imagem Aplicada Técnica Media", "8-bit", getImagemOriginal().getProcessor().getWidth(), getImagemOriginal().getProcessor().getHeight(), 1);

        int pixel[] = {0, 0, 0};

        for (int i = 0; i < getImagemOriginal().getProcessor().getWidth(); i++) {
            for (int j = 0; j < getImagemOriginal().getProcessor().getHeight(); j++) {
                pixel = getImagemOriginal().getProcessor().getPixel(i, j, pixel);

                float mediaPixels = (pixel[0] + pixel[1] + pixel[2]) / 3;

                novaImagem.getProcessor().putPixel(i, j, (int) mediaPixels);
            }
        }
        getImagemOriginal().setProcessor(novaImagem.getProcessor());
    }

    private void aplicarThreshold(int sliderThreshold) {

        int pixel;

        getImagemOriginal().getProcessor().reset();

        for (int i = 0; i < getImagemOriginal().getProcessor().getWidth(); i++) {
            for (int j = 0; j < getImagemOriginal().getProcessor().getHeight(); j++) {
                pixel = getImagemOriginal().getProcessor().getPixel(i, j);

                if ( pixel < sliderThreshold) {
                    pixel = 0;
                }
                if (pixel > sliderThreshold) {
                    pixel = 255;
                }
                getImagemOriginal().getProcessor().putPixel(i,j, pixel);
            }
        }
    }

    private void atualizarImagem(GenericDialog interfaceGrafica) {

        int sliderThreshold = (int) interfaceGrafica.getNextNumber();
        aplicarThreshold(sliderThreshold);
        getImagemOriginal().updateAndDraw();
    }

    @Override
    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent awtEvent) {

        if (interfaceGrafica.wasCanceled()) {
            return false;
        }

        atualizarImagem(interfaceGrafica);
        return true;
    }
}
