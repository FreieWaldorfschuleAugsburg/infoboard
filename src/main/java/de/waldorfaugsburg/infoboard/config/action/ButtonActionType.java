package de.waldorfaugsburg.infoboard.config.action;

import java.util.function.Supplier;

public enum ButtonActionType {

    MENU_CHANGE("Menü wechseln", MenuChangeAction::new),
    OPEN_FILE("Datei öffnen", OpenFileAction::new),
    OPEN_URL("URL öffnen", OpenUrlAction::new),
    KILL_PROCESS("Prozess beenden", KillProcessAction::new),
    KEYPRESS("Tastendruck", KeypressAction::new),
    BRIGHTNESS("Helligkeit", BrightnessAction::new);

    private final String name;
    private final Supplier<AbstractButtonAction> supplier;

    ButtonActionType(final String name, final Supplier<AbstractButtonAction> supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public AbstractButtonAction getNewInstance() {
        return supplier.get();
    }
}
