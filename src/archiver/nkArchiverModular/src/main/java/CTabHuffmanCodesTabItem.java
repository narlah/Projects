import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.*;

class CTabHuffmanCodesTabItem {
    private final Comparator<String[]> BY_CHAR = Comparator.comparing(o -> o[0]);
    private final Comparator<String[]> BY_ASCII = Comparator.comparing(o -> Integer.valueOf(o[1]));
    private final Comparator<String[]> BY_CODE = (o1, o2) -> {
        int len1 = o1[3].length();
        int len2 = o2[3].length();
        return Integer.compare(len1, len2);
    };
    private final Comparator<String[]> BY_COUNT = (o1, o2) -> {
        int val1 = Integer.valueOf(o1[2]);
        int val2 = Integer.valueOf(o2[2]);
        return Integer.compare(val1, val2);
    };
    private final Comparator<String[]> BY_COUNT_REVERSED = (o2, o1) -> {
        int val1 = Integer.valueOf(o1[2]);
        int val2 = Integer.valueOf(o2[2]);
        return Integer.compare(val1, val2); //todo code duplication , find a way to deduplicate it
    };
    private CTabItem tbtmNewCodes;
    private Table table;
    private TableColumn tblclmnChar;
    private TableColumn tblclmnCharASCIICode;
    private TableColumn tblclmnCode;
    private TableColumn tblclmnCount;
    private CTabFolder tabFolder;
    private ArrayList<String[]> tableRows = new ArrayList<>();
    private boolean flagAscending = true;

    CTabHuffmanCodesTabItem(CTabFolder arg0, int arg1) {
        this.tabFolder = arg0;
    }

    void dispose() {
        if (tbtmNewCodes != null)
            tbtmNewCodes.dispose();
    }

    void initUI() {
        this.dispose();
        // tab Show Codes
        tbtmNewCodes = new CTabItem(tabFolder, SWT.None);
        tbtmNewCodes.setText("Show Codes");

        ScrolledComposite scrolledCodesComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL);
        tbtmNewCodes.setControl(scrolledCodesComposite);
        scrolledCodesComposite.setExpandHorizontal(true);
        scrolledCodesComposite.setExpandVertical(true);

        table = new Table(scrolledCodesComposite, SWT.BORDER | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        tblclmnChar = new TableColumn(table, SWT.NONE);
        tblclmnChar.setAlignment(SWT.CENTER);
        tblclmnChar.setWidth(60);
        tblclmnChar.setText("Char");

        tblclmnCharASCIICode = new TableColumn(table, SWT.NONE);
        tblclmnCharASCIICode.setWidth(57);
        tblclmnCharASCIICode.setText("   ASCII");
        table.setSortColumn(tblclmnCharASCIICode);


        tblclmnCount = new TableColumn(table, SWT.NONE);
        tblclmnCount.setWidth(60);
        tblclmnCount.setText("Count");

        tblclmnCode = new TableColumn(table, SWT.NONE);
        tblclmnCode.setWidth(350);
        tblclmnCode.setText("Huffman Code");
        Listener sortListener = e -> {
            TableColumn column = (TableColumn) e.widget;
            if (column == tblclmnChar)
                tableRows.sort(BY_CHAR);
            if (column == tblclmnCharASCIICode)
                tableRows.sort(BY_ASCII);
            if (column == tblclmnCode)
                tableRows.sort(BY_CODE);
            if (column == tblclmnCount) {
                if (flagAscending)
                    tableRows.sort(BY_COUNT);
                else
                    tableRows.sort(BY_COUNT_REVERSED);
                flagAscending = !flagAscending;
            }
            table.setSortColumn(column);
            updateTable();
        };
        tblclmnChar.addListener(SWT.Selection, sortListener);
        tblclmnCharASCIICode.addListener(SWT.Selection, sortListener);
        tblclmnCode.addListener(SWT.Selection, sortListener);
        tblclmnCount.addListener(SWT.Selection, sortListener);

        scrolledCodesComposite.setContent(table);
        scrolledCodesComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    void fillTable(HashMap<Character, String> hashMap, LinkedHashMap<Character, Integer> treeToFile) {
        tableRows.clear();
        for (Map.Entry<Character, String> entry : hashMap.entrySet()) {
            TableItem item = new TableItem(table, SWT.NONE);
            String code = entry.getKey().toString();
            switch (entry.getKey()) {
                case '\t': {
                    code = "Tab";
                    break;
                }
                case '\r': {
                    code = "New line";
                    break;
                }
                case ' ': {
                    code = "Space";
                    break;
                }
            }
            String[] stringArray = {code, (int) entry.getKey() + "", treeToFile.get(entry.getKey()) + "", entry.getValue()};
            item.setText(stringArray);
            tableRows.add(stringArray);
        }
        table.redraw();
    }

    private void updateTable() {
        table.removeAll();
        for (String[] row : tableRows) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(row);
        }
    }
}
