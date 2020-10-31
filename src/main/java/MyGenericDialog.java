import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;

public class MyGenericDialog implements PlugIn, DialogListener {
    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Conversão para Escala de Cinza");
        interfaceGrafica.addDialogListener(this);

        String[] estrategia = {"Estratégia Médias", "Estratégia Luminância Analógica","Estratégia Luminância Digital"};

        interfaceGrafica.addRadioButtonGroup("Escolha uma estratégia:", estrategia, 3, 1, "Estratégia Médias");

        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        }
        else {
            if (interfaceGrafica.wasOKed()) {
                String opcaoEscolhida = interfaceGrafica.getNextRadioButton();

                TecnicasFundamentais tF = new TecnicasFundamentais();
                tF.run(opcaoEscolhida);

            }
        }
    }

    @Override
    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
        if (interfaceGrafica.wasCanceled()) return false;

        return true;
    }
}