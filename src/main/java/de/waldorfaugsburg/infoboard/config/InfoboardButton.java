package de.waldorfaugsburg.infoboard.config;

import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;
import de.waldorfaugsburg.infoboard.config.icon.AbstractStreamDeckIcon;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoboardButton {
    private String name;
    private List<AbstractButtonAction> actions;
    private AbstractStreamDeckIcon streamDeckIcon;

    public List<AbstractButtonAction> getActions() {
        if (actions == null) {
            actions = new ArrayList<>();
        }

        return actions;
    }
}
