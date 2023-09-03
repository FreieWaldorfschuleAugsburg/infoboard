package de.waldorfaugsburg.infoboard.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoboardMenu {
    private UUID id;
    private String name;
    private Map<Integer, InfoboardButton> buttons;
}
