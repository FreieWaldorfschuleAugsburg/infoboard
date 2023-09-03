package de.waldorfaugsburg.infoboard.menu;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.window.InfoboardFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class MenuRenderer {

    private final InfoboardApplication application;

    private UUID currentMenuId;

    public MenuRenderer(final InfoboardApplication application) {
        this.application = application;
    }

    public void updateMenu() {
        changeMenu(getCurrentMenu());
    }

    public void changeMenu(final UUID id) {
        changeMenu(application.getConfiguration().getMenu(id));
    }

    public void changeMenu(final InfoboardMenu targetMenu) {
        currentMenuId = targetMenu.getId();

        // Clear frame and update menubar
        application.getFrame().clear();

        // Clear streamdeck if initialized
        if (application.getStreamDeck() != null) {
            application.getStreamDeck().clear();
        }

        // Arrange buttons
        for (int i = 0; i < InfoboardFrame.BUTTON_COUNT; i++) {
            final InfoboardButton button = targetMenu.getButtons().get(i);

            if (button != null) {
                application.getFrame().renderButton(i, button);
                if (application.getStreamDeck() != null) {
                    application.getStreamDeck().renderButton(i, button);
                }
            }
        }
    }

    public UUID getCurrentMenuId() {
        return currentMenuId;
    }

    public InfoboardMenu getCurrentMenu() {
        return application.getConfiguration().getMenu(currentMenuId);
    }
}
