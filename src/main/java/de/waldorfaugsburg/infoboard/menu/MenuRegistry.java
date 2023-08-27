package de.waldorfaugsburg.infoboard.menu;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class MenuRegistry {

    private final InfoboardApplication application;
    private final Map<UUID, InfoboardMenu> menuMap = new HashMap<>();

    private UUID currentMenuId;

    public MenuRegistry(final InfoboardApplication application) {
        this.application = application;

        // Insert menus into map
        application.getConfiguration().getMenus().forEach(menu -> menuMap.put(menu.getId(), menu));
    }

    public void addMenu(final InfoboardMenu menu) {
        menuMap.put(menu.getId(), menu);
        updateMenu();
    }

    public void removeMenu(final InfoboardMenu menu) {
        menuMap.remove(menu.getId(), menu);
        // TODO remove references (actions)
        updateMenu();
    }

    public void updateMenu() {
        application.getConfiguration().getMenus().clear();
        application.getConfiguration().getMenus().addAll(menuMap.values());

        final InfoboardMenu mainMenu = getMainMenu();
        if (mainMenu == null) {
            application.getConfiguration().setMainMenu(application.getConfiguration().getMenus().get(0).getId());
        }

        InfoboardMenu currentMenu = getCurrentMenu();
        if (currentMenu == null) {
            currentMenu = getMainMenu();
        }

        changeMenu(currentMenu);
    }

    public void changeMenu(final UUID id) {
        final InfoboardMenu targetMenu = getMenu(id);
        if (targetMenu == null)
            throw new IllegalArgumentException("menu not found");

        changeMenu(targetMenu);
    }

    public void changeMenu(final InfoboardMenu targetMenu) {
        currentMenuId = targetMenu.getId();

        // Clear and update frame
        application.getFrame().clearAndUpdate();

        // Clear if streamdeck is initialized
        if (application.getStreamDeck() != null) {
            application.getStreamDeck().clear();
        }

        // Arrange buttons
        for (int i = 0; i < targetMenu.getButtons().size(); i++) {
            final InfoboardButton button = targetMenu.getButtons().get(i);

            // Update frame button
            final JButton frameButton = application.getFrame().getButton(button.getIndex());
            frameButton.setText(button.getName());

            if (frameButton.getActionListeners().length == 0) {
                frameButton.addActionListener(e -> button.getAction().run(application));
            }

            // Update streamdeck button
            try {
                application.getStreamDeck().setImage(button.getIndex(), button.getStreamDeckIcon().createImage());
            } catch (final IOException e) {
                log.error("Error while setting image for key {}", button.getIndex(), e);
            }
        }
    }

    public InfoboardMenu getMenu(final UUID id) {
        return menuMap.get(id);
    }

    public InfoboardMenu getMainMenu() {
        return getMenu(application.getConfiguration().getMainMenu());
    }

    public UUID getCurrentMenuId() {
        return currentMenuId;
    }

    public InfoboardMenu getCurrentMenu() {
        return getMenu(currentMenuId);
    }
}
