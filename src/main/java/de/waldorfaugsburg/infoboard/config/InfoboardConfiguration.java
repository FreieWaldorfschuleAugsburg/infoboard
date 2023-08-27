package de.waldorfaugsburg.infoboard.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class InfoboardConfiguration {
    private boolean production;
    private String streamDeckSerial;
    @Setter
    private UUID mainMenu;
    private List<InfoboardMenu> menus;
}
