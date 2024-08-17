package sypztep.penomior.client.object;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import sypztep.penomior.common.util.AnimationUtils;
import sypztep.penomior.common.util.DrawContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ScrollableTextList {
    private static final float SCROLL_SPEED = 1.2F; // Speed of the scrolling effect
    private static final int ICON_SIZE = 16; // Size of the icon

    private final List<ListElement> items;
    private float scrollOffset; // Use float for smooth scrolling
    private float targetScrollOffset;
    private Map<String, Object> values;
    private final int textHeight; // Height of each text item
    private int x;
    private int y;
    private int width;
    private int height;

    public ScrollableTextList(List<ListElement> items, Map<String, Object> values) {
        this.items = items;
        this.values = values;
        this.scrollOffset = 0;
        this.targetScrollOffset = 0;
        this.textHeight = 25;
    }

    public void updateValues(Map<String, Object> newValues) {
        this.values = newValues;
    }

    public void render(DrawContext context, TextRenderer textRenderer, int x, int y, int width, int height, float scale, float iconscale, int alpha, float deltaTick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        int totalItems = items.size();
        int totalScrollableHeight = totalItems * textHeight; // Total height of all items
        int maxScrollOffset = (int) Math.max(0, totalScrollableHeight - height / scale); // Maximum scrollable area

        // Smoothly update scrollOffset towards targetScrollOffset
        if (scrollOffset != targetScrollOffset) {
            float delta = (targetScrollOffset - scrollOffset) * SCROLL_SPEED * deltaTick;
            if (Math.abs(delta) > 1) {
                scrollOffset += delta;
            } else {
                scrollOffset = targetScrollOffset; // Snap to target if close enough
            }
        }

        // Clamp scrollOffset to be within bounds
        scrollOffset = Math.min(Math.max(scrollOffset, 0), maxScrollOffset);

        int currentY = y - (int) scrollOffset;

        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0F);
        // Container for text
        DrawContextUtils.drawRect(context, (int) (x / scale), y, (int) (width / scale), (int) (height / scale) + 10, 0xFF1E1E1E);
        // Render text items with icons and optional buttons
        for (ListElement listElement : items) {
            String itemText = listElement.text();
            Identifier icon = listElement.icon(); // Get icon for the item
            boolean isMainContext = itemText.equals(itemText.toUpperCase()); // Main context is upper case like 'HELLO'
            int offsetX = isMainContext ? 50 : 0; // Additional x-offset for main context text

            if (currentY >= y + height / scale) break; // Stop rendering if the content is out of view

            // Prepend "● " if not main context
            String displayText = isMainContext ? itemText : "● " + itemText;

            // Format the text with values
            String formattedText = StringFormatter.format(displayText, values);

            // Wrap text to fit width (considering scaling)
            List<String> wrappedLines = wrapText(textRenderer, formattedText, (int) (width / scale));

            int textBlockHeight = wrappedLines.size() * textRenderer.fontHeight; // Calculate total height of the wrapped text block

            for (String line : wrappedLines) {
                if (currentY >= y && currentY + (textRenderer.fontHeight * scale) <= y + height / scale) {
                    // Calculate vertical alignment offset to center the text block within the element's area
                    int textY = currentY + (textHeight - textBlockHeight) / 2;
                    // Calculate horizontal position
                    int textX = (int) (x / scale) + offsetX;

                    // Apply a "tab" like offset to the text
                    int tabOffset = 30; // Adjust this value to control the "tab" spacing
                    textX += tabOffset;

                    // Render icon if present
                    if (icon != null) {
                        matrixStack.push();
                        matrixStack.translate((x + offsetX - ICON_SIZE) / scale - 10, (textY + (textRenderer.fontHeight * scale) / 2 - (float) ICON_SIZE / 2), 0);
                        matrixStack.scale(iconscale, iconscale, 1.0F);
                        context.drawGuiTexture(icon, 0, 0, ICON_SIZE, ICON_SIZE); // Adjust x, y, width, height
                        matrixStack.pop();
                    }

                    // Draw text
                    AnimationUtils.drawFadeText(context, textRenderer, line, textX, textY, alpha);

                    // Draw gradient line below main context text
                    if (isMainContext) {
                        int gradientY = currentY + textRenderer.fontHeight + 14;// Position the gradient line just below the text
                        DrawContextUtils.renderHorizontalLineWithCenterGradient(context, (int) (x / scale), gradientY, (int) (width / scale), 1, 400,0xFFFFFFFF, 0); // Adjust colors as needed
                    }
                }
                currentY += textRenderer.fontHeight;

                if (currentY >= y + height / scale) break; // Stop rendering if the content is out of view
            }

            currentY += Math.max(0, textHeight - wrappedLines.size() * textRenderer.fontHeight); // Adjust for item spacing
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

        if (!currentLine.isEmpty())
            lines.add(currentLine.toString());

        return lines;
    }

    public void scroll(int amount, double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
            targetScrollOffset -= amount;

            int totalItems = items.size();
            int totalScrollableHeight = totalItems * textHeight;

            int maxScrollOffset = Math.max(0, totalScrollableHeight - this.height);
            targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScrollOffset));
        }
    }

    public boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static class StringFormatter {
        public static String format(String template, Map<String, Object> values) {
            List<Map.Entry<String, Object>> sortedEntries = values.entrySet()
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getKey().length(), e1.getKey().length()))
                    .toList();

            for (Map.Entry<String, Object> entry : sortedEntries) {
                String placeholder = "%" + entry.getKey();
                Object value = entry.getValue();
                String replacement = formatValue(value);
                template = template.replace(placeholder, replacement);
            }
            return template;
        }

        private static String formatValue(Object value) {
            // Apply a color code (for example, green) before returning the formatted value
            String colorCode = "§6"; // Change this to any other color code if needed
            return switch (value) {
                case Integer i -> colorCode + String.format("%d", value);
                case Float v -> colorCode + String.format("%.2f", value);
                case Double v -> colorCode + String.format("%.2f", value);
                case null, default -> {
                    assert value != null;
                    yield colorCode + value;
                }
            };
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
