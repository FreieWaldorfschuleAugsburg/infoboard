package de.waldorfaugsburg.infoboard.config.action;

import java.util.function.Supplier;

public enum ButtonActionType {

    MENU_CHANGE("Menü wechseln", MenuChangeAction::new),
    RUN_COMMAND("Befehl ausführen", RunCommandAction::new),
    CLOSE_DIALOGS("Dialoge schließen", CloseDialogsAction::new),
    KEYPRESS("Tastendruck", KeypressAction::new),
    BRIGHTNESS("Helligkeit", BrightnessAction::new),
    MINIMIZE("Minimieren", MinimizeAction::new),
    MAXIMIZE("Maximieren", MaximizeAction::new),
    MONITOR_WAKE("Monitor wecken", MonitorWakeAction::new),
    MONITOR_SLEEP("Monitor abschalten", MonitorSleepAction::new),
    PROCURAT_GROUP_TABLE("Procurat!5 Gruppe", ProcuratGroupTableAction::new);

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
