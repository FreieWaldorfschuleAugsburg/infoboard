package de.waldorfaugsburg.infoboard.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class InfoboardConfiguration {
    @Getter
    private int httpPort;
    @Getter
    private String httpTarget;
    @Getter
    private String streamDeckSerial;
    @Setter
    private UUID mainMenu;
    private Map<UUID, InfoboardMenu> menus;

    public void addMenu(final InfoboardMenu menu) {
        menus.put(menu.getId(), menu);
    }

    public void removeMenu(final UUID id) {
        menus.remove(id);
    }

    public Collection<InfoboardMenu> getMenus() {
        return menus.values();
    }

    public InfoboardMenu getMenu(final UUID id) {
        final InfoboardMenu menu = menus.get(id);
        return menu != null ? menu : getMainMenu();
    }

    public UUID getMainMenuId() {
        return mainMenu;
    }

    public InfoboardMenu getMainMenu() {
        return getMenu(mainMenu);
    }
}
