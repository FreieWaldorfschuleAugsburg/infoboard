package de.waldorfaugsburg.infoboard.window;

import com.google.gson.JsonElement;
import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.procurat.ProcuratGroupMembership;
import de.waldorfaugsburg.infoboard.procurat.ProcuratPerson;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProcuratGroupTableFrame extends JDialog {

    public ProcuratGroupTableFrame(final InfoboardApplication application,
                                   final List<String> columns,
                                   final Map<ProcuratPerson, ProcuratGroupMembership> membershipMap) throws HeadlessException {
        super(application.getFrame());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        final JTable table = new JTable(new ProcuratTableModel(columns, membershipMap));
        table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 30));
        table.setRowHeight(30);

        final TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        final List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        table.setRowSorter(sorter);

        final JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
