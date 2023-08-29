package de.waldorfaugsburg.infoboard.config.icon;

import java.util.function.Supplier;

public enum StreamDeckIconType {

    IMAGE("Bild", ImageIcon::new),
    TEXT("Text", TextIcon::new);

    private final String name;
    private final Supplier<AbstractStreamDeckIcon> supplier;

    StreamDeckIconType(final String name, final Supplier<AbstractStreamDeckIcon> supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public AbstractStreamDeckIcon getNewInstance() {
        return supplier.get();
    }
}
