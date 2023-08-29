package de.waldorfaugsburg.infoboard.config;

import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;
import de.waldorfaugsburg.infoboard.config.icon.AbstractStreamDeckIcon;
import lombok.*;

@Data
@NoArgsConstructor
public class InfoboardButton {
    private int index;
    private String name;
    private AbstractButtonAction action;
    private AbstractStreamDeckIcon streamDeckIcon;
}
