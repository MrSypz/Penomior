package sypztep.penomior.client.object;

import net.minecraft.util.Identifier;

public record ListElement(String text, Identifier icon) {
    public ListElement(String text) {
        this(text,null);
    }
}