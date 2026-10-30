package com.reallifebuildings.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BuilderTabletScreen extends Screen {
    private int selectedPage = 0;
    private static final String[] PAGES = {"МАТЕРИАЛЫ", "ПРОЕКТЫ", "ИНСТРУМЕНТЫ"};

    private BuilderTabletScreen() {
        super(Component.literal("REAL LIFE BUILDINGS"));
    }

    public static void open() {
        net.minecraft.client.Minecraft.getInstance().setScreen(new BuilderTabletScreen());
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 150;
        for (int i = 0; i < PAGES.length; i++) {
            final int page = i;
            this.addRenderableWidget(Button.builder(Component.literal(PAGES[i]), button -> {
                selectedPage = page;
                clearWidgets();
                init();
            }).bounds(left + i * 102, this.height / 2 - 65, 98, 20).build());
        }
        this.addRenderableWidget(Button.builder(Component.literal("ЗАКРЫТЬ"), button -> onClose())
                .bounds(this.width / 2 - 50, this.height / 2 + 90, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        graphics.fill(centerX - 165, centerY - 82, centerX + 165, centerY + 120, 0xE820252D);
        graphics.drawCenteredString(this.font, "REAL LIFE BUILDINGS", centerX, centerY - 105, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "ПЛАНШЕТ СТРОИТЕЛЯ", centerX, centerY - 88, 0xA5D6A7);

        if (selectedPage == 0) {
            graphics.drawString(this.font, "СТРОИТЕЛЬНЫЕ МАТЕРИАЛЫ", centerX - 145, centerY - 32, 0xFFFFFF);
            graphics.drawString(this.font, "Бетонная панель", centerX - 135, centerY - 12, 0xD8D8D8);
            graphics.drawString(this.font, "Фасадное стекло", centerX - 135, centerY + 3, 0xD8D8D8);
            graphics.drawString(this.font, "Дубовый пол", centerX - 135, centerY + 18, 0xD8D8D8);
            graphics.drawString(this.font, "Красный кирпич", centerX + 20, centerY - 12, 0xD8D8D8);
            graphics.drawString(this.font, "Гладкая каменная панель", centerX + 20, centerY + 3, 0xD8D8D8);
            graphics.drawString(this.font, "Тёмный бетон", centerX + 20, centerY + 18, 0xD8D8D8);
        } else if (selectedPage == 1) {
            graphics.drawCenteredString(this.font, "ПЕРВЫЙ ПРОЕКТ: СОВРЕМЕННЫЙ ДОМ", centerX, centerY - 35, 0xFFFFFF);
            graphics.drawString(this.font, "Габариты: 7 x 9 блоков, 1 этаж", centerX - 140, centerY - 12, 0xD8D8D8);
            graphics.drawString(this.font, "Комнаты: гостиная, кухня, спальня, ванная", centerX - 140, centerY + 5, 0xD8D8D8);
            graphics.drawString(this.font, "Материалы: бетон, стекло, дуб, кирпич", centerX - 140, centerY + 22, 0xD8D8D8);
            graphics.drawCenteredString(this.font, "План подготовлен; авторазмещение ещё в разработке", centerX, centerY + 52, 0xA5D6A7);
        } else {
            graphics.drawCenteredString(this.font, "ИНСТРУМЕНТЫ СТРОИТЕЛЯ", centerX, centerY - 28, 0xFFFFFF);
            graphics.drawCenteredString(this.font, "Редактор планов и режим размещения — в разработке", centerX, centerY, 0xCCCCCC);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
