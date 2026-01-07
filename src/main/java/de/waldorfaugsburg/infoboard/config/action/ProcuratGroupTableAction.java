package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.procurat.*;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import de.waldorfaugsburg.infoboard.window.ProcuratGroupTableFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProcuratGroupTableAction extends AbstractButtonAction {

    private int groupId;

    protected ProcuratGroupTableAction() {
        super(ButtonActionType.PROCURAT_GROUP_TABLE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            final ProcuratClient client = ProcuratClient.createInstance(application);

            final Map<ProcuratPerson, ProcuratGroupMembership> membershipMap = new HashMap<>();
            for (final ProcuratGroupMembership membership : client.getGroupMemberships(groupId)) {
                membershipMap.put(client.getProcuratPersonById(membership.getPersonId()), membership);
            }

            final ProcuratGroup group = client.getProcuratGroupById(groupId);

            new ProcuratGroupTableFrame(application, "Gruppeneinteilung/Randstundenabfrage: Klasse " + group.getName(), List.of("RSA_MO", "RSA_DI", "RSA_MI", "RSA_DO", "RSA_FR", "GRP_KL", "GRP_HKU", "GRP_SPR_EN", "GRP_SPR_FR", "GRP_REL", "GRP_MUS"), membershipMap);
        } catch (HttpClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel pathLabel = new JLabel("Gruppen-ID");
        pathLabel.setBounds(0, 4, 56, 14);
        contentPane.add(pathLabel);

        final JSpinner groupIdSpinner = new JSpinner();
        groupIdSpinner.setBounds(64, 1, 165, 20);
        groupIdSpinner.setValue(groupId);
        groupIdSpinner.addChangeListener(e -> {
            groupId = (int) groupIdSpinner.getValue();
            frame.updateList();
        });
        contentPane.add(groupIdSpinner);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }
}
