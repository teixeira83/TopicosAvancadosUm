import ij.ImageJ;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.gui.GenericDialog;

public class Teste2 implements  PlugIn{

    public void run(String s){
        ImagePlus image = IJ.getImage();
        IJ.log("Saida do metodo isThreshold()");
        IJ.log(String.valueOf(image.getTitle()));
        IJ.log(String.valueOf(image.isThreshold()));
    }


}
