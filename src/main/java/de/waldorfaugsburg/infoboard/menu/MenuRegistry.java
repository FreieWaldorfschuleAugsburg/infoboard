package de.waldorfaugsburg.infoboard.menu;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import lombok.extern.slf4j.Slf4j;

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
        application.getConfiguration().getMenus().add(menu);
        updateMenu();
    }

    public void removeMenu(final UUID menuId) {
        final InfoboardMenu menu = menuMap.remove(menuId);
        if (menu == null) throw new IllegalArgumentException("invalid menu");

        application.getConfiguration().getMenus().remove(menu);
        // TODO remove references (actions)
        updateMenu();
    }

    public void updateMenu() {
        InfoboardMenu currentMenu = getCurrentMenu();
        if (currentMenu == null) {
            currentMenu = getMainMenu();
        }

        changeMenu(currentMenu);
    }

    public void changeMenu(final UUID id) {
        final InfoboardMenu targetMenu = getMenu(id);
        if (targetMenu == null) throw new IllegalArgumentException("menu not found");

        changeMenu(targetMenu);
    }

    public void changeMenu(final InfoboardMenu targetMenu) {
        currentMenuId = targetMenu.getId();

        // Clear frame and update menubar
        application.getFrame().clear();
        application.getFrame().updateMenuBar();

        // Clear streamdeck if initialized
        if (application.getStreamDeck() != null) {
            application.getStreamDeck().clear();
        }

        // Arrange buttons
        for (int i = 0; i < targetMenu.getButtons().size(); i++) {
            final InfoboardButton button = targetMenu.getButtons().get(i);

            application.getFrame().addButton(button);
            application.getStreamDeck().renderButton(button);
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
