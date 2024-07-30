package sypztep.penomior.client.object;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import sypztep.penomior.common.util.AnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrollableTextList {
    private static final int ITEM_HEIGHT = 25; // Height of each text item
    private final List<String> items;
    private int scrollOffset;
    private final Map<String, Object> values;

    public ScrollableTextList(List<String> items, Map<String, Object> values) {
        this.items = items;
        this.values = values;
        this.scrollOffset = 0;
    }

    public void render(DrawContext context, TextRenderer textRenderer, int x, int y, int width, int height, float scale, int alpha) {
        int totalItems = items.size();
        int maxScrollOffset = Math.max(0, totalItems * ITEM_HEIGHT - height);

        // Adjust scroll offset to be within bounds
        scrollOffset = Math.min(Math.max(scrollOffset, 0), maxScrollOffset);

        int currentY = y - scrollOffset;

        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0F);

        for (String itemText : items) {
            boolean isMainContext = itemText.equals(itemText.toUpperCase()); // Main context is upper case like 'HELLO'
            int offsetX = isMainContext ? 50 : 0; // Additional x-offset for main context text

            if (currentY >= y + height) break;

            // Prepend "● " if not main context
            String displayText = isMainContext ? itemText : "● " + itemText;

            // Format the text with values
            String formattedText = StringFormatter.format(displayText, values);

            // Wrap text to fit width
            List<String> wrappedLines = wrapText(textRenderer, formattedText, (int) (width / scale));

            for (String line : wrappedLines) {
                if (currentY >= y && currentY + (textRenderer.fontHeight * scale) <= y + height) {
                    AnimationUtils.drawFadeText(context, textRenderer, line, (int) ((x + offsetX) / scale), currentY, alpha);
                }
                currentY += textRenderer.fontHeight;

                if (currentY >= y + height) break;
            }

            currentY += Math.max(0, ITEM_HEIGHT - wrappedLines.size() * textRenderer.fontHeight); // Adjust for item spacing
        }

        matrixStack.pop();
    }

    private List<String> wrapText(TextRenderer textRenderer, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        int lineWidth = 0;
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            int wordWidth = textRenderer.getWidth(word + " ");

            if (lineWidth + wordWidth > maxWidth) {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
                lineWidth = 0;
            }

            currentLine.append(word).append(" ");
            lineWidth += wordWidth;
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public void scroll(int amount) {
        scrollOffset -= amount;
    }

    public static class StringFormatter {
        public static String format(String template, Map<String, Object> values) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                String placeholder = "%" + entry.getKey();
                Object value = entry.getValue();
                String replacement = formatValue(value);
                template = template.replace(placeholder, replacement);
            }
            return template;
        }

        private static String formatValue(Object value) {
            if (value instanceof Integer) {
                return String.format("%d", value);
            } else if (value instanceof Float) {
                return String.format("%.2f", value);
            } else if (value instanceof Double) {
                return String.format("%.2f", value);
            } else {
                return value.toString();
            }
        }
    }
}
