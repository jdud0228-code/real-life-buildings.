package com.reallifebuildings.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BuilderTabletScreen extends Screen {
    private BuilderTabletScreen() {
        super(Component.literal("REAL LIFE BUILDINGS"));
    }

    public static void open() {
        net.minecraft.client.Minecraft.getInstance().setScreen(new BuilderTabletScreen());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        graphics.drawCenteredString(this.font, "REAL LIFE BUILDINGS", centerX, centerY - 55, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "ПЛАНШЕТ СТРОИТЕЛЯ", centerX, centerY - 30, 0xA5D6A7);
        graphics.drawCenteredString(this.font, "Первая альфа-версия", centerX, centerY + 5, 0xCCCCCC);
        graphics.drawCenteredString(this.font, "Каталог зданий появится в следующих обновлениях", centerX, centerY + 25, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
