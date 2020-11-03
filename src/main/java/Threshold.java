import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import ij.plugin.PlugIn;

import java.awt.*;


public class Threshold implements PlugIn {

    ImagePlus imagemOriginal;
    ImageProcessor processadorOriginal;
    ImagePlus imagemCinza;

    public ImageProcessor getProcessadorOriginal() {
        return processadorOriginal;
    }

    public void setProcessadorOriginal(ImageProcessor processadorOriginal) {
        this.processadorOriginal = processadorOriginal;
    }


    public ImagePlus getImagemCinza() {
        return imagemCinza;
    }

    public void setImagemCinza(ImagePlus imagemCinza) {
        this.imagemCinza = imagemCinza;
    }

    public ImagePlus getImagemOriginal() {
        return imagemOriginal;
    }

    public void setImagemOriginal(ImagePlus imagemOriginal) {
        this.imagemOriginal = imagemOriginal;
    }

    public void run(String s) {
        apresentarInterfaceGrafica();
        getImagemOriginal().updateAndDraw();
    }

    public void apresentarInterfaceGrafica() {

        IJ.run("Nile Bend");
        setImagemOriginal(IJ.getImage());
        setProcessadorOriginal(getImagemOriginal().getProcessor());

        GenericDialog interfaceGrafica = new GenericDialog("Ajustar Imagem");

        //problemas para fazer com o listener, será implementado depois
        //interfaceGrafica.addDialogListener(this);

        interfaceGrafica.addSlider("Configurar Threshold: ", 0, 255, 0, 1);

        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                int sliderThreshold = (int) interfaceGrafica.getNextNumber();
                ImagePlus imagemCinza = transformarEmEscalaDeCinzaCalculandoMedia();
                ajustarThreshold(interfaceGrafica, sliderThreshold, imagemCinza);
                IJ.log("Saida do metodo isThreshold()");
                IJ.log(String.valueOf(imagemCinza.isThreshold()));
            }
        }
    }

    private void ajustarThreshold(GenericDialog interfaceGrafica, int sliderThreshold, ImagePlus imagemCinza) {

        int pixel[] = {0, 0, 0};
        ImageProcessor processador = imagemCinza.getProcessor();
        for (int i = 0; i < getProcessadorOriginal().getWidth(); i++) {
            for (int j = 0; j < getProcessadorOriginal().getHeight(); j++) {
                pixel = getProcessadorOriginal().getPixel(i, j, pixel);

                for (int p = 0; p < 3; p++) {
                    if (pixel[p] > sliderThreshold) {
                        pixel[p] = 255;
                    }
                    if (pixel[p] < sliderThreshold) {
                        pixel[p] = 0;
                    }
                }
                processador.putPixel(i, j, pixel);
            }
        }
        imagemCinza.show();

    }

    private ImagePlus transformarEmEscalaDeCinzaCalculandoMedia(){
        int pixel[] = {0, 0, 0};

        ImagePlus imagemEmEscalaCinza = IJ.createImage("Imagem Em Escala de Cinza", "8-bit", getImagemOriginal().getProcessor().getWidth(), getImagemOriginal().getProcessor().getHeight(), 1);

        for (int i = 0; i < processadorOriginal.getWidth(); i++) {
            for (int j = 0; j < processadorOriginal.getHeight(); j++) {
                pixel = processadorOriginal.getPixel(i, j, pixel);

                float mediaPixels = (pixel[0] + pixel[1] + pixel[2]) / 3;

                imagemEmEscalaCinza.getProcessor().putPixel(i, j, (int) mediaPixels);
            }
        }
        return imagemEmEscalaCinza;
    }

//    @Override
//    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent awtEvent) {
//        if (interfaceGrafica.wasCanceled()) return false;
//        int sliderThreshold = (int) interfaceGrafica.getNextNumber();
//
//        ImageProcessor processadorAuxiliar = getImagemOriginal().getProcessor().createProcessor(getProcessadorOriginal().getWidth(), getProcessadorOriginal().getHeight());
////        ajustarThreshold(processadorAuxiliar, sliderThreshold);
//
//        return true;
//    }
}
