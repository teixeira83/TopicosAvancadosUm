import ij.ImageJ;
import ij.IJ;

public class MyImageJApp {

    public static void main(String[] args) {
        new ImageJ();
        //IJ.run("Compile and Run...", "compile=C:/Users/rafael/IdeaProjects/ProjetoImageJ/src/main/java/MyGenericDialog.java");
//        IJ.run("Compile and Run...", "compile=C:/Users/rafael/IdeaProjects/ProjetoImageJ/src/main/java/Teste.java");
        IJ.run("Compile and Run...", "compile=C:/Users/rafael/IdeaProjects/ProjetoImageJ/src/main/java/InverterRGBComLUT.java");

    }
}
