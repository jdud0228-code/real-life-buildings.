package com.reallifebuildings.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import com.reallifebuildings.block.BuildingBlocks;

public class BuilderTabletScreen extends Screen {
    private int selectedPage = 0;
    private int selectedProject = 0;
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
        if (selectedPage == 1) {
            this.addRenderableWidget(Button.builder(Component.literal("Дом"), button -> selectedProject = 0)
                    .bounds(this.width / 2 - 145, this.height / 2 - 35, 65, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Коттедж"), button -> selectedProject = 1)
                    .bounds(this.width / 2 - 75, this.height / 2 - 35, 80, 20).build());
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
            drawMaterial(graphics, BuildingBlocks.CONCRETE_PANEL.get(), "Бетонная панель", centerX - 145, centerY - 13);
            drawMaterial(graphics, BuildingBlocks.FACADE_GLASS.get(), "Фасадное стекло", centerX - 145, centerY + 6);
            drawMaterial(graphics, BuildingBlocks.OAK_FLOOR.get(), "Дубовый пол", centerX - 145, centerY + 25);
            drawMaterial(graphics, BuildingBlocks.RED_BRICK.get(), "Красный кирпич", centerX + 10, centerY - 13);
            drawMaterial(graphics, BuildingBlocks.SMOOTH_STONE_PANEL.get(), "Гладкая каменная панель", centerX + 10, centerY + 6);
            drawMaterial(graphics, BuildingBlocks.DARK_CONCRETE.get(), "Тёмный бетон", centerX + 10, centerY + 25);
            drawMaterial(graphics, BuildingBlocks.INTERIOR_WOOD.get(), "Дерево интерьера", centerX - 145, centerY + 44);
            drawMaterial(graphics, BuildingBlocks.TERRACE_TILE.get(), "Плитка террасы", centerX + 10, centerY + 44);
            graphics.drawCenteredString(this.font, "Рецепты доступны через верстак", centerX, centerY + 66, 0xA5D6A7);
        } else if (selectedPage == 1) {
            if (selectedProject == 0) {
                graphics.drawCenteredString(this.font, "СОВРЕМЕННЫЙ ДОМ", centerX, centerY - 35, 0xFFFFFF);
                graphics.drawString(this.font, "7 x 9 блоков • 1 этаж", centerX - 145, centerY - 12, 0xD8D8D8);
                graphics.drawString(this.font, "Гостиная • кухня • спальня • ванная", centerX - 145, centerY + 8, 0xD8D8D8);
                renderHouseBlueprint(graphics, centerX + 34, centerY - 8);
                graphics.drawCenteredString(this.font, "Можно построить планшетом", centerX, centerY + 78, 0xA5D6A7);
            } else {
                graphics.drawCenteredString(this.font, "ЗАГОРОДНЫЙ КОТТЕДЖ", centerX, centerY - 35, 0xFFFFFF);
                graphics.drawString(this.font, "9 x 11 блоков • 2 этажа", centerX - 145, centerY - 12, 0xD8D8D8);
                graphics.drawString(this.font, "1 этаж: гостиная, кухня, прихожая", centerX - 145, centerY + 8, 0xD8D8D8);
                graphics.drawString(this.font, "2 этаж: спальня, кабинет, ванная", centerX - 145, centerY + 23, 0xD8D8D8);
                graphics.drawCenteredString(this.font, "Строится отдельным чертежом в руке", centerX, centerY + 70, 0xA5D6A7);
                graphics.drawCenteredString(this.font, "Материалы: пол 198 • бетон 220 • крыша 99", centerX, centerY + 84, 0xD8D8D8);
                graphics.drawCenteredString(this.font, "Стекло 8 • кирпич 71", centerX, centerY + 98, 0xD8D8D8);
            }        } else {
            graphics.drawCenteredString(this.font, "ИНСТРУМЕНТЫ СТРОИТЕЛЯ", centerX, centerY - 28, 0xFFFFFF);
            graphics.drawCenteredString(this.font, "Построить: планшет в руке → ПКМ по земле", centerX, centerY - 4, 0xCCCCCC);
            graphics.drawCenteredString(this.font, "Повернуть дом: зажми Shift при ПКМ", centerX, centerY + 14, 0xCCCCCC);
            graphics.drawCenteredString(this.font, "Сначала проверь свободную площадку 7 x 9", centerX, centerY + 32, 0xCCCCCC);
            graphics.drawCenteredString(this.font, "Мебель: кресло и диван — можно использовать как сиденья", centerX, centerY + 48, 0xD8D8D8);
            graphics.drawCenteredString(this.font, "Шкаф: 27 слотов для хранения предметов", centerX, centerY + 64, 0xD8D8D8);
            graphics.drawCenteredString(this.font, "Умная лампа: ПКМ включает и выключает свет", centerX, centerY + 80, 0xD8D8D8);
            graphics.drawCenteredString(this.font, "Звонок: ПКМ воспроизводит сигнал", centerX, centerY + 96, 0xA5D6A7);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawMaterial(GuiGraphics graphics, net.minecraft.world.level.block.Block block,
                              String label, int x, int y) {
        graphics.renderItem(new ItemStack(block), x, y - 2);
        graphics.drawString(this.font, label, x + 22, y + 2, 0xD8D8D8);
    }

    private void renderHouseBlueprint(GuiGraphics graphics, int x, int y) {
        int cell = 13;
        // Top-down 7 x 9 room plan. Colors distinguish room zones.
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 7; col++) {
                int color;
                if (row < 4 && col < 4) color = 0xFF547B9A;       // living room
                else if (row < 4) color = 0xFF9A8054;             // kitchen
                else if (col < 4) color = 0xFF6D8A65;             // bedroom
                else color = 0xFF8A6D8A;                          // bathroom
                int x1 = x + col * cell;
                int y1 = y + row * cell;
                graphics.fill(x1, y1, x1 + cell - 1, y1 + cell - 1, color);
                graphics.fill(x1, y1, x1 + cell - 1, y1 + 1, 0xFF20252D);
                graphics.fill(x1, y1, x1 + 1, y1 + cell - 1, 0xFF20252D);
            }
        }
        graphics.fill(x, y, x + 7 * cell, y + 1, 0xFFFFFFFF);
        graphics.fill(x, y + 9 * cell - 1, x + 7 * cell, y + 9 * cell, 0xFFFFFFFF);
        graphics.fill(x, y, x + 1, y + 9 * cell, 0xFFFFFFFF);
        graphics.fill(x + 7 * cell - 1, y, x + 7 * cell, y + 9 * cell, 0xFFFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
