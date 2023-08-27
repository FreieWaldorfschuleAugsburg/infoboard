package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;

import java.util.UUID;

public class MenuChangeAction extends AbstractButtonAction {

    private UUID targetMenu;

    @Override
    public void run(final InfoboardApplication application) {
        application.getMenuRegistry().changeMenu(targetMenu);
    }
}
