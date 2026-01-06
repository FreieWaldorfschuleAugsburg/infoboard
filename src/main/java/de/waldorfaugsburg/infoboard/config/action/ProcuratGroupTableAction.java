package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.procurat.HttpClientException;
import de.waldorfaugsburg.infoboard.procurat.ProcuratClient;
import de.waldorfaugsburg.infoboard.procurat.ProcuratGroupMembership;
import de.waldorfaugsburg.infoboard.procurat.ProcuratPerson;
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

    protected ProcuratGroupTableAction() {
        super(ButtonActionType.PROCURAT_GROUP_TABLE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            final ProcuratClient client = ProcuratClient.createInstance(application);

            final Map<ProcuratPerson, ProcuratGroupMembership> membershipMap = new HashMap<>();
            for (final ProcuratGroupMembership membership : client.getGroupMemberships(276)) {
                membershipMap.put(client.getProcuratPersonById(membership.getPersonId()), membership);
            }

            new ProcuratGroupTableFrame(application, List.of("RSA_MO", "RSA_DI", "RSA_MI", "RSA_DO", "RSA_FR", "GRP_KL", "GRP_HKU", "GRP_SPR_EN", "GRP_SPR_FR", "GRP_REL", "GRP_MUS"), membershipMap);
        } catch (HttpClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }
}
