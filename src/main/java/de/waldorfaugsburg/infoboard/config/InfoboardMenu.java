package de.waldorfaugsburg.infoboard.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoboardMenu {
    private UUID id;
    private String name;
    private List<InfoboardButton> buttons;
}
