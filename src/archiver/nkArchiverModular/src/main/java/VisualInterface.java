import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

class VisualInterface {
    private ArchiveController controller = new ArchiveController();
    private CTabHuffmanCodesTabItem ctabFCodesItem;
    private CTabFolder tabFolder;
    private InOutTab inoutTabItem;
    private ShowCompressorWikiTabItem showCompressorWikiTabItem;
    private FileDialog fd;

    public enum compressionTypes {
        Huffman("nik"), GZiPStream("zip"), LZ77("lz7"), Something_Else("arc");
        private final String fileExtension;

        compressionTypes(String type) {
            this.fileExtension = type;
        }

        public String getExtension() {
            return fileExtension;
        }
    }

    /**
     * @wbp.parser.entryPoint
     */
    Shell initUI() {
        Display display = new Display();
        //JFrame frame = new JFrame();
        final Shell shell = new Shell(display, SWT.NO_REDRAW_RESIZE | SWT.CLOSE | SWT.BORDER);
        controller.setCompressorName("Huffman"); // default selected
        tabFolder = new CTabFolder(shell, SWT.BORDER);
        tabFolder.setSimple(false);
        tabFolder.setBounds(0, 30, 600, 600);
        int tabSelection = 0;
        tabFolder.setSelection(tabSelection);
        tabFolder.setFocus();
        tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        // tab items
        inoutTabItem = new InOutTab(tabFolder);

        if (controller.getCompressorName() != null && controller.getCompressorName().equalsIgnoreCase("Huffman")) {
            ctabFCodesItem = new CTabHuffmanCodesTabItem(tabFolder, SWT.NONE);
        }

        showCompressorWikiTabItem = new ShowCompressorWikiTabItem(tabFolder, controller.getCompressorName());

        // <tabs

        shell.setSize(600, 600);
        shell.setText("Achiever Visual Interface");
        shell.setLayout(null); //todo layout ?

        // Menu management
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);

        MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
        mntmNewSubmenu.setText("File");

        Menu menu_1 = new Menu(mntmNewSubmenu);
        mntmNewSubmenu.setMenu(menu_1);

        MenuItem fileMenuOpen = new MenuItem(menu_1, SWT.NONE);
        fileMenuOpen.setText("Open");

        MenuItem fileMenuClose = new MenuItem(menu_1, SWT.NONE);
        fileMenuClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                controller.setInFile(null);
                inoutTabItem.changeInOut("", "");
                inoutTabItem.setFolderFileLabel("");
                inoutTabItem.addToTextArea("File Closed...");
            }
        });
        fileMenuClose.setText("Close");

        // menu item exit
        MenuItem fileMenuExit = new MenuItem(menu_1, SWT.NONE);
        fileMenuExit.setText("Exit");
        fileMenuExit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                inoutTabItem.addToTextArea("Exiting...");
                System.exit(1);
            }
        });

        // menu item open (File selector)
        class Open implements SelectionListener {
            public void widgetSelected(SelectionEvent event) {
                fd = new FileDialog(shell, SWT.OPEN);
                fd.setText("Open");
                fd.setFilterPath(".");
                String[] filterExt = {"*.*", "*.txt", "*.doc", "*.rtf", "*.xml"};
                fd.setFilterExtensions(filterExt);
                String selected = fd.open();
                if (selected != null) {
                    controller.setInFile(selected);
                    String outFileName = controller.setOutFileFromIn(selected);
                    int lastSeparator = selected.lastIndexOf(File.separator);
                    String inFileNameOnly = selected.substring(lastSeparator + 1);
                    String outFileNameOnly = outFileName.substring(outFileName.lastIndexOf(File.separator) + 1);
                    inoutTabItem.changeInOut(inFileNameOnly, outFileNameOnly);
                    inoutTabItem.setFolderFileLabel(selected.substring(0, lastSeparator));
                    inoutTabItem.addToTextArea("File selected ...");
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        }

        fileMenuOpen.addSelectionListener(new Open());

        new MenuItem(menu, SWT.SEPARATOR);

        @SuppressWarnings(value = {"unused"})
        MenuItem menuItem = new MenuItem(menu, SWT.SEPARATOR);


        MenuItem mntmNewSubmenuActions = new MenuItem(menu, SWT.CASCADE);
        mntmNewSubmenuActions.setText("Actions");
        Menu menu_actions = new Menu(mntmNewSubmenuActions);
        mntmNewSubmenuActions.setMenu(menu_actions);

        // menu item Archive
        MenuItem menuArchive = new MenuItem(menu_actions, SWT.NONE);
        menuArchive.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    if (controller.getInFile() == null || Objects.equals(controller.getInFile(), "")) {
                        inoutTabItem.addToTextArea("No file selected...");
                        return;
                    }
                    controller.compress();
                    for (int i = 0; i <= 100; i++) {
                        inoutTabItem.addPercentageToProgressBar(i);
                        Thread.sleep(10);
                    }
                    inoutTabItem.addToTextArea("File compressed!");
                    ctabFCodesItem.initUI();
                    if (controller.getCompressorName().equalsIgnoreCase("Huffman")) {
                        ctabFCodesItem.fillTable((HashMap<Character, String>) controller.getDataStructure(), ((HuffmanCompressor) controller.getCompressor()).getTreeToFile());
                        inoutTabItem.addToTextArea("Huffman codes populated ...");
                    }
                    tabFolder.setSelection(0);
                } catch (IllegalArgumentException | IOException e) {
                    System.out.println("Error " + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        menuArchive.setText("Archive");

        // menu item UnPack
        MenuItem menuUnpack = new MenuItem(menu_actions, SWT.NONE);
        menuUnpack.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
            }
        });
        menuUnpack.setText("UnPack");

        // menu item ShowCodes
        // MenuItem menuShowCodes = new MenuItem(menu, SWT.NONE);
        // menuShowCodes.setText("Show Compressor info");

        // Compress Selector combo
        final Combo comboCompressSelector = new Combo(shell, SWT.NONE);
        comboCompressSelector.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                String selectedMethod = comboCompressSelector.getText();
                if (!controller.getCompressorName().equalsIgnoreCase(selectedMethod)) {
                    controller.changeCompressor(selectedMethod);
                    ctabFCodesItem.dispose();
                    if (selectedMethod.equalsIgnoreCase("Huffman")) {
                        ctabFCodesItem.initUI();
                    }
                    showCompressorWikiTabItem.changeTo(selectedMethod);

                    String extension = compressionTypes.valueOf(selectedMethod).getExtension();
                    String newOutFileName = controller.updateOutFileExtension(extension);
                    inoutTabItem.setInFile(controller.getInFile());
                    inoutTabItem.updateOutExtension(newOutFileName);
                }
            }
        });
        comboCompressSelector.setItems(Arrays.toString(compressionTypes.values()).replaceAll("^.|.$", "").split(", "));
        comboCompressSelector.setBounds(0, 0, 600, 10);
        comboCompressSelector.select(0);

        return shell;
    }
}
