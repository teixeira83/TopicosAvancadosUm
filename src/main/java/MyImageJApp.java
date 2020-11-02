import ij.ImageJ;
import ij.IJ;

public class MyImageJApp {

    public static void main(String[] args) {
        new ImageJ();
//        IJ.run("Compile and Run...", "compile=C:/Users/rafael/IdeaProjects/ProjetoImageJ/src/main/java/MyGenericDialog.java");
//        IJ.run("Compile and Run...", "compile=C:/Users/rafael/IdeaProjects/ProjetoImageJ/src/main/java/InverterRGBComLUT.java");
        IJ.run("Compile and Run...", "compile=C:/Users/rafael/IdeaProjects/ProjetoImageJ/src/main/java/AjustarBrilhoContrasteESolarizacao.java");
//        IJ.run("Compile and Run...", "compile=/home/z3r0/IdeaProjects/TopicosAvancadosUm/src/main/java/AjustarBrilhoContrasteESolarizacao.java");

    }
}
