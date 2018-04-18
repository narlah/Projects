import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class InOutTab {
    private CTabFolder tabFolder;
    private Label inFileLabel;
    private Label outFileLabel;
    private Label folderFileLabel;
    private Text textArea;
    private ProgressBar progressBar;

    InOutTab(CTabFolder folder) {
        this.tabFolder = folder;
        initUI();
    }

    private void initUI() {
        // TAb IN AND OUT
        final int lengthOfLabel = 45;
        final int lengthOfDataField = 560;
        final int vHeightOfLabel = 15;

        CTabItem inOutTabItem = new CTabItem(tabFolder, SWT.NONE);
        inOutTabItem.setText("In - out");

        Composite compositeInOut = new Composite(tabFolder, SWT.NONE);
        compositeInOut.setLayout(null);

        Label folder_lbl = new Label(compositeInOut, SWT.NONE);
        folder_lbl.setBounds(5, 10, lengthOfLabel, vHeightOfLabel);
        folder_lbl.setText("Folder :");
        folderFileLabel = new Label(compositeInOut, SWT.NONE);
        folderFileLabel.setBounds(lengthOfLabel + 10, 10, lengthOfDataField, vHeightOfLabel);


        Label in_lbl = new Label(compositeInOut, SWT.NONE);
        in_lbl.setBounds(5, 30, lengthOfLabel, vHeightOfLabel);
        in_lbl.setText("In :");
        inFileLabel = new Label(compositeInOut, SWT.NONE);
        inFileLabel.setBounds(lengthOfLabel + 10, 30, lengthOfDataField, vHeightOfLabel);


        Label out_lbl = new Label(compositeInOut, SWT.NONE);
        out_lbl.setBounds(5, 50, lengthOfLabel, vHeightOfLabel);
        out_lbl.setText("Out : ");
        outFileLabel = new Label(compositeInOut, SWT.NONE);
        outFileLabel.setBounds(lengthOfLabel + 10, 50, lengthOfDataField, vHeightOfLabel);


        progressBar = new ProgressBar(compositeInOut, 4);
        progressBar.setBounds(5, 70, 580, 10);
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);


        textArea = new Text(compositeInOut, SWT.MULTI);
        textArea.setBounds(5, 80, 580, 300);
        textArea.setBackground(new Color(null, 208, 208, 208));
        textArea.setEditable(false);
        textArea.setVisible(true);

        inOutTabItem.setControl(compositeInOut);
    }

    void changeInOut(String in, String out) {
        this.inFileLabel.setText(in);
        this.outFileLabel.setText(out);
    }

    void updateOutExtension(String out) {
        if (out != null)
            this.outFileLabel.setText(out);
    }

    void setInFile(String in) {
        if (in != null)
            this.inFileLabel.setText(in);
    }

    void addToTextArea(String str) {
        textArea.setText(textArea.getText() + "\n" + str);
    }

    void addPercentageToProgressBar(int i) {
        progressBar.setSelection(i);
    }

    int getPercentageToProgressBar() {
        return progressBar.getState();
    }

    //    public void clearTextArea() {
//        textArea.setText("");
//    }
    void setFolderFileLabel(String folderFileLabel) {
        this.folderFileLabel.setText(folderFileLabel);
    }
}
