package de.universallp.justenoughbuttons;

import de.universallp.justenoughbuttons.client.ClientProxy;
import de.universallp.justenoughbuttons.core.CommonProxy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Created by universallp on 09.08.2016 16:07.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
@Mod(modid = JEIButtons.MODID, version = JEIButtons.VERSION, clientSideOnly = true, guiFactory = "de.universallp.justenoughbuttons.client.GuiFactory")
public class JEIButtons {

    public static final String MODID = "justenoughbuttons";
    public static final String VERSION = "1.10.2-1.6.2";
    public static final String MOD_MOREOVERLAYS = "moreoverlays";
    public static boolean isServerSidePresent = false;
    public static boolean enableOverlays = true;

    @Mod.Instance
    public static JEIButtons instance;

    @SidedProxy(clientSide = "de.universallp.justenoughbuttons.client.ClientProxy", serverSide = "de.universallp.justenoughbuttons.core.CommonProxy")
    public static CommonProxy proxy;

    public static EnumButtonCommands btnGameMode = EnumButtonCommands.SURVIVAL;
    public static EnumButtonCommands btnTrash    = EnumButtonCommands.DELETE;
    public static EnumButtonCommands btnSun      = EnumButtonCommands.SUN;
    public static EnumButtonCommands btnRain     = EnumButtonCommands.RAIN;
    public static EnumButtonCommands btnDay      = EnumButtonCommands.DAY;
    public static EnumButtonCommands btnNight    = EnumButtonCommands.NIGHT;
    public static EnumButtonCommands btnNoMobs   = EnumButtonCommands.NOMOBS;
    public static EnumButtonCommands btnFreeze   = EnumButtonCommands.FREEZETIME;
    public static EnumButtonCommands btnMagnet   = EnumButtonCommands.MAGNET;

    public static EnumButtonCommands[] btnCustom   = new EnumButtonCommands[] { EnumButtonCommands.CUSTOM1, EnumButtonCommands.CUSTOM2,
                                                                                EnumButtonCommands.CUSTOM3, EnumButtonCommands.CUSTOM4 };

    public static boolean configHasChanged = false;

    public static EnumButtonCommands hoveredButton;
    public static boolean isAnyButtonHovered;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerKeyBind();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ConfigHandler.loadPostInit();
        setUpPositions();
        proxy.postInit(event);
    }

    public enum EnumButtonCommands {
        CREATIVE("gamemode 1", 5, 5),
        ADVENTURE("gamemode 2", 5, 5),
        SURVIVAL("gamemode 0", 5, 5),
        SPECTATE("gamemode 3", 5, 5),
        DELETE("clear ", 65, 5),
        RAIN("weather rain", 25, 5),
        SUN("weather clear", 45, 5),
        DAY("time set day", 5, 26),
        NIGHT("time set night", 25, 26),
        FREEZETIME("gamerule doDaylightCycle", 25, 47),
        NOMOBS("kill @e[type=!Player]", 5, 47),
        MAGNET("tp", 25, 47),
        CUSTOM1("", 5, 68, 0),
        CUSTOM2("", 25, 68, 1),
        CUSTOM3("", 5, 89, 2),
        CUSTOM4("", 25, 89, 3);

        public boolean isEnabled = true;
        boolean isVisible = true;

        String command;

        EnumButtonState state = EnumButtonState.DISABLED;

        static final int width = 18;
        static final int height = 19;
        int xPos;
        int yPos;
        public byte id;

        EnumButtonCommands(String commandToExecute, int x, int y) {
            this.command = commandToExecute;
            this.xPos = x;
            this.yPos = y;
        }

        EnumButtonCommands (String commandToExecute, int x, int y, int id) {
            this.id = (byte) id;
            this.command = commandToExecute;
            this.xPos = x;
            this.yPos = y;
        }

        static final ResourceLocation icons = new ResourceLocation(MODID, "textures/icons.png");

        public void setEnabled(boolean b) {
            isEnabled = b;
        }

        public void setPosition(int x, int y) {
            xPos = x;
            yPos = y;
        }

        public void setVisible(boolean visible) { isVisible = visible; }

        public EnumButtonCommands cycle() {
            if (ordinal() == 0)
                return ADVENTURE.isEnabled ? ADVENTURE : SURVIVAL;
            else if (ordinal() == 1)
                return SURVIVAL;
            else if (ordinal() == 2)
                return SPECTATE.isEnabled ? SPECTATE : CREATIVE;
            else if (ordinal() == 3)
                return CREATIVE;
            else
                return this; // Other buttons don't cycle
        }

        public void draw(GuiContainer parent) {
            if (!isVisible || getCommand().equals(""))
                return;
            int mouseX = proxy.getMouseX();
            int mouseY = proxy.getMouseY();

            if (isEnabled) {
                if (canExecuteCommand(getCommand()))
                    if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
                        state = EnumButtonState.HOVERED;
                        hoveredButton = this;
                        isAnyButtonHovered = true;
                    } else
                        state = EnumButtonState.ENABLED;
                else
                    state = EnumButtonState.DISABLED;
            } else {
                if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
                    hoveredButton = this;
                    isAnyButtonHovered = true;
                }
                state = EnumButtonState.DISABLED;
            }

            parent.mc.renderEngine.bindTexture(icons);

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            parent.drawTexturedModalRect(xPos, yPos, width * iconID(), height * state.ordinal(), width, height);
            RenderHelper.disableStandardItemLighting();

        }

        public String getCommand() {
            return this.ordinal() > MAGNET.ordinal() ? ConfigHandler.customCommand[id] : command;
        }

        public int iconID() {
            if (this == MAGNET)
                return 12;
            return this.ordinal() > MAGNET.ordinal() ? 11 : this.ordinal();
        }
    }

    public enum EnumButtonState {
        DISABLED,
        ENABLED,
        HOVERED
    }

    private static boolean canExecuteCommand(String c) {
        return ClientProxy.player.canCommandSenderUseCommand(1, c);
    }


    public static class ConfigHandler {
        public static boolean enableAdventureMode  = true;
        public static boolean enableSpectatoreMode = true;

        public static boolean enableSaves    = true;
        public static int magnetRadius = 8;

        public static boolean enableClearInventory = false;

        static boolean enableGamemode = true;
        static boolean enableDelete   = true;
        static boolean enableTime     = true;
        static boolean enableWeather  = true;
        static boolean enableKillMobs = true;
        static boolean enableDayCycle = true;
        static boolean enableMagnet   = true;
        static boolean[] enableCustom   = new boolean[] { false, false, false, false };

        public static String[] customCommand = new String[] { "help", "help", "help", "help" }; // Halp halp halp
        public static String[] customName    = new String[] { "Print Help", "Print Help", "Print Help", "Print Help" };

        static final String CATEGORY = "buttons";
        public static final String CATEGORY_CUSTOM = "custombuttons";

        public static boolean showButtons = true;

        public static Configuration config;

        static void load() {
            showButtons = config.getBoolean("showButtons",          CATEGORY, true, "When false no buttons will be shown");

            enableAdventureMode  = config.getBoolean("enableAdventureMode",  CATEGORY, true, "When false the gamemode button won't allow you to switch to adventure mode");
            enableSpectatoreMode = config.getBoolean("enableSpectatoreMode", CATEGORY, true, "When false the gamemode button won't allow you to switch to spectator mode");

            enableGamemode       = config.getBoolean("enableGamemode",       CATEGORY, true, "When false the gamemode button will be disabled");
            enableDelete         = config.getBoolean("enableDelete",         CATEGORY, true, "When false the delete button will be disabled");
            enableWeather        = config.getBoolean("enableWeather",        CATEGORY, true, "When false the weather buttons will be disabled");
            enableTime           = config.getBoolean("enableTime",           CATEGORY, true, "When false the time buttons will be disabled");
            enableKillMobs       = config.getBoolean("enableKillMobs",       CATEGORY, true, "When false the kill entities button will be disabled");
            enableDayCycle       = config.getBoolean("enableDayCycle",       CATEGORY, true, "When false the freeze time button will be disabled");
            enableMagnet         = config.getBoolean("enableMagnet",         CATEGORY, true, "When false the magnet mode button will be disabled");

            enableSaves          = config.getBoolean("enableSaves",         CATEGORY, true, "When false the four save slots will be disabled");

            magnetRadius         = config.getInt("magnetRadius", CATEGORY, 12, 1, 32, "The radius in which the magnet mode attracts items");

            enableClearInventory = config.getBoolean("enableClearInventory", CATEGORY, false, "When true shift clicking the delete buttonwill clear your inventory");

            // Custom Buttons

            for (int i = 0; i < enableCustom.length; i++) {
                enableCustom[i]  = config.getBoolean("enableCustomButton." + i, CATEGORY_CUSTOM, false, "When true you'll get a button, which executes a custom command");
                customCommand[i] = config.getString("customCommand." + i,       CATEGORY_CUSTOM, "help",   "The command to be executed by the custom button");
                customName[i]    = config.getString("customName." + i,          CATEGORY_CUSTOM, "Print Help",   "The tooltip of the custom button");
            }



            EnumButtonCommands.ADVENTURE.setEnabled(enableAdventureMode);
            EnumButtonCommands.SPECTATE.setEnabled(enableSpectatoreMode);

            btnGameMode.setVisible(enableGamemode);
            btnDay.setVisible(enableTime);
            btnNight.setVisible(enableTime);
            btnTrash.setVisible(enableDelete);
            btnNoMobs.setVisible(enableKillMobs);
            btnFreeze.setVisible(enableDayCycle);
            btnRain.setVisible(enableWeather);
            btnSun.setVisible(enableWeather);
            btnMagnet.setVisible(enableMagnet);

            for (int i = 0; i < btnCustom.length; i++)
                btnCustom[i].setVisible(enableCustom[i]);

            if (config.hasChanged())
                config.save();
        }

        static void loadPostInit() { if(config.hasChanged()) config.save(); }

        static void loadConfig(File configFile) {
            if (config == null)
                config = new Configuration(configFile);
            load();
            MinecraftForge.EVENT_BUS.register(new ConfigHandler());
        }

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if(eventArgs.getModID().equals(MODID)) {
                config.save();
                load();
                configHasChanged = true;
            }
        }
    }

    public static void setUpPositions() {
        EnumButtonCommands[] btns = new EnumButtonCommands[] { btnGameMode, btnRain, btnSun, btnTrash, btnDay, btnNight,
                                                               btnNoMobs, btnFreeze, btnMagnet, btnCustom[0], btnCustom[1], btnCustom[2], btnCustom[3],};

        int x = 0, y = 0;
        for (EnumButtonCommands b : btns) {
            if (!b.isVisible)
                continue;

            b.setPosition((EnumButtonCommands.width + 2) * x + 5, (EnumButtonCommands.height + 2) * y + 5);
            x++;

            if (y == 0 && x == 4) {
                y++;
                x = 0;
            } else if (y == 1 && x == 3) {
                y++;
                x = 0;
            } else if (x % 2 == 0 && y > 1) {
                x = 0;
                y++;
            }
        }
    }
}
