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

        String[] estrategia = {"Estratégia Médias", "Estratégia Lum","Estratégia 3"};

        interfaceGrafica.addRadioButtonGroup("Escolha uma estratégia:", estrategia, 1, 3, "Estratégia 2");

        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        }
        else {
            if (interfaceGrafica.wasOKed()) {
                String opcaoEscolhida = interfaceGrafica.getNextRadioButton();

                TecnicasFundamentais tF = new TecnicasFundamentais();
                tF.run(opcaoEscolhida);

                /*
                IJ.log("_____________Últimas respostas obtidas_______________");
                IJ.log("Resposta da mensagem:" + interfaceGrafica.getNextText());
                IJ.log("Resposta da caixa de texto:" + interfaceGrafica.getNextString());
                IJ.log("Resposta do botão de rádio:" + interfaceGrafica.getNextRadioButton());
                IJ.log("Resposta do campo numérico:" + interfaceGrafica.getNextNumber());
                IJ.log("Resposta do checkbox:" + interfaceGrafica.getNextBoolean());
                IJ.log("Resposta do slider:" + interfaceGrafica.getNextNumber());

                IJ.showMessage("Plugin encerrado com sucesso!");
                */
            }
        }
    }

    @Override
    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
        if (interfaceGrafica.wasCanceled()) return false;
        /*
        IJ.log("Resposta da mensagem:" + interfaceGrafica.getNextText());
        IJ.log("Resposta da caixa de texto:" + interfaceGrafica.getNextString());
        IJ.log("Resposta do botão de rádio:" + interfaceGrafica.getNextRadioButton());
        IJ.log("Resposta do campo numérico:" + interfaceGrafica.getNextNumber());
        IJ.log("Resposta do checkbox:" + interfaceGrafica.getNextBoolean());
        IJ.log("Resposta do slider:" + interfaceGrafica.getNextNumber());
        IJ.log("\n");
        */

        return true;
    }
}