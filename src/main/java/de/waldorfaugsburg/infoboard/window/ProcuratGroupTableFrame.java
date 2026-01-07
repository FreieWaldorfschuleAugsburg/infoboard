package de.waldorfaugsburg.infoboard.window;

import com.google.gson.JsonElement;
import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.procurat.ProcuratGroupMembership;
import de.waldorfaugsburg.infoboard.procurat.ProcuratPerson;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProcuratGroupTableFrame extends JDialog {

    public ProcuratGroupTableFrame(final InfoboardApplication application,
                                   final String title,
                                   final List<String> columns,
                                   final Map<ProcuratPerson, ProcuratGroupMembership> membershipMap) throws HeadlessException {
        super(application.getFrame());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        final JTable table = new JTable(new ProcuratTableModel(columns, membershipMap));
        table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 20));
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        final JTableHeader header = table.getTableHeader();
        header.setFont(new Font(table.getFont().getName(), Font.BOLD, 20));

        final DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        header.setDefaultRenderer(headerRenderer);

        table.setTableHeader(header);

        final TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        final List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);

        final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                c.setForeground(Color.BLACK);
                return c;
            }
        };
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);

        final TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(200);
        colModel.getColumn(1).setPreferredWidth(200);
        colModel.getColumn(2).setPreferredWidth(100);
        colModel.getColumn(3).setPreferredWidth(100);
        colModel.getColumn(4).setPreferredWidth(100);
        colModel.getColumn(5).setPreferredWidth(100);
        colModel.getColumn(6).setPreferredWidth(100);
        colModel.getColumn(7).setPreferredWidth(100);
        colModel.getColumn(8).setPreferredWidth(130);
        colModel.getColumn(11).setPreferredWidth(130);
        colModel.getColumn(12).setPreferredWidth(130);
        colModel.getColumn(9).setPreferredWidth(150);
        colModel.getColumn(10).setPreferredWidth(150);

        final JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        final JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 40));
        add(titleLabel, BorderLayout.NORTH);

        pack();
        setVisible(true);
    }

    public static class ProcuratTableModel extends DefaultTableModel {

        public ProcuratTableModel(final List<String> columns,
                                  final Map<ProcuratPerson, ProcuratGroupMembership> membershipMap) {
            super();

            addColumn("Nachname");
            addColumn("Vorname");

            for (final String column : columns) {
                addColumn(column);
            }

            for (final Map.Entry<ProcuratPerson, ProcuratGroupMembership> membership : membershipMap.entrySet()) {
                final List<String> entries = new ArrayList<>();
                entries.add(membership.getKey().getLastName());
                entries.add(membership.getKey().getFirstName());

                for (final String column : columns) {
                    final JsonElement element = membership.getValue().getJsonData().get(column);
                    if (element == null) {
                        entries.add("");
                        continue;
                    }

                    entries.add(element.getAsString());
                }

                addRow(entries.toArray(new String[0]));
            }
        }
    }

}
