package de.univrsal.justenoughbuttons.core.network;

/**
 * Created by universal on 05.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */

/**
 * Class to allow JEB to use command buttons without cheats
 */
public class MessageExecuteButton {// implements IMessage, IMessageHandler<MessageExecuteButton, IMessage> {

    public static final byte GM_CREATIVE  = 0;
    public static final byte GM_ADVENTURE = 1;
    public static final byte GM_SURVIVAL  = 2;
    public static final byte GM_SPECTATE  = 3;
    public static final byte DELETE       = 4;
    public static final byte RAIN         = 5;
    public static final byte SUN          = 6;
    public static final byte DAY          = 7;
    public static final byte NIGHT        = 8;
    public static final byte FREEZE       = 9;
    public static final byte KILL         = 10;
    public static final byte MAGNET       = 11;
    public static final byte DELETE_ALL   = 12;
    private int commandOrdinal;
    private String[] cmd;

    public MessageExecuteButton() { }

    public MessageExecuteButton(int cmdId, String[] cmd) {
        this.commandOrdinal = cmdId;
        this.cmd = cmd != null ? cmd : new String[] { "" };
    }

//    @Override
//    public void fromBytes(ByteBuf buf) {
//        this.commandOrdinal = buf.readByte();
//        int l = buf.readByte();
//        this.cmd = new String[l];
//
//        for (int i = 0; i < l; i++)
//            this.cmd[i] = ByteBufUtils.readUTF8String(buf);
//    }
//
//    @Override
//    public void toBytes(ByteBuf buf) {
//        buf.writeByte(commandOrdinal);
//        buf.writeByte(cmd.length);
//        for (int i = 0; i < cmd.length; i++)
//            ByteBufUtils.writeUTF8String(buf, cmd[i] != null ? cmd[i] : "");
//    }
//
//    public static boolean checkPermissions(EntityPlayer player, MinecraftServer server) {
//        if (server.isSinglePlayer())
//            return true;
//
//        for (String pl : server.getPlayerList().getOppedPlayerNames()) {
//            if (pl.equals(player.getDisplayNameString()))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public IMessage onMessage(MessageExecuteButton message, MessageContext ctx) {
//        EntityPlayerMP p = ctx.getServerHandler().player;
//
//        if (p == null)
//            return null;
//        MinecraftServer s = ctx.getServerHandler().player.server;
//        World world = s.getWorld(p.dimension);
//
//        WorldInfo worldinfo = world.getWorldInfo();
//        boolean isOP = checkPermissions(p, s);
//        boolean error = true;
//
//        switch (message.commandOrdinal) {
//            case GM_ADVENTURE:
//                if (!isOP && ConfigHandler.gamemodeRequiresOP)
//                    break;
//
//                error = false;
//                p.setGameType(GameType.ADVENTURE);
//                break;
//            case GM_CREATIVE:
//                if (!isOP && ConfigHandler.gamemodeRequiresOP)
//                    break;
//
//                error = false;
//                p.setGameType(GameType.CREATIVE);
//                break;
//            case GM_SURVIVAL:
//                if (!isOP && ConfigHandler.gamemodeRequiresOP)
//                    break;
//
//                error = false;
//                p.setGameType(GameType.SURVIVAL);
//                break;
//            case GM_SPECTATE:
//                if (!isOP && ConfigHandler.gamemodeRequiresOP)
//                    break;
//
//                error = false;
//                p.setGameType(GameType.SPECTATOR);
//                break;
//            case DELETE_ALL:
//                if (!isOP && ConfigHandler.deleteRequiresOP)
//                    break;
//
//                error = false;
//                p.inventory.clear();
//                break;
//            case DELETE:
//                if (!isOP && ConfigHandler.deleteRequiresOP)
//                    break;
//
//                error = false;
//                p.inventory.setItemStack(ItemStack.EMPTY);
//                break;
//            case SUN:
//                if (!isOP && ConfigHandler.weatherRequiresOP)
//                    break;
//
//                error = false;
//                worldinfo.setCleanWeatherTime(1000000);
//                worldinfo.setRainTime(0);
//                worldinfo.setThunderTime(0);
//                worldinfo.setRaining(false);
//                worldinfo.setThundering(false);
//                break;
//            case RAIN:
//                if (!isOP && ConfigHandler.weatherRequiresOP)
//                    break;
//
//                error = false;
//                worldinfo.setCleanWeatherTime(0);
//                worldinfo.setRainTime(1000000);
//                worldinfo.setThunderTime(1000000);
//                worldinfo.setRaining(true);
//                worldinfo.setThundering(false);
//                break;
//            case DAY:
//                if (!isOP && ConfigHandler.timeRequiresOP)
//                    break;
//
//                error = false;
//                worldinfo.setWorldTime(1000);
//                break;
//            case NIGHT:
//                if (!isOP && ConfigHandler.timeRequiresOP)
//                    break;
//
//                error = false;
//                world.setWorldTime(13000);
//                break;
//            case FREEZE:
//                if (!isOP && ConfigHandler.timeFreezeRequiresOP)
//                    break;
//
//                error = false;
//                boolean origValue = worldinfo.getGameRulesInstance().getBoolean("doDaylightCycle");
//                worldinfo.getGameRulesInstance().setOrCreateGameRule("doDaylightCycle", origValue ? "false" : "true");
//                break;
//            case KILL:
//                if (!isOP && ConfigHandler.killMobsRequiresOP)
//                    break;
//
//                error = false;
//                for (Iterator<Entity> e = world.loadedEntityList.iterator(); e.hasNext();) {
//                    Entity entity = e.next();
//
//                    if (!(entity instanceof EntityPlayer) && entity instanceof EntityLiving || entity instanceof EntityItem) {
//                        world.removeEntity(entity);
//                    }
//                }
//
//                break;
//        }
//
//        if (error) {
//            ITextComponent msg = new TextComponentTranslation(Localization.NO_PERMISSIONS);
//            msg.setStyle(msg.getStyle().setColor(TextFormatting.RED));
//            p.sendMessage(msg);
//        }
//        return null;
//    }
}
