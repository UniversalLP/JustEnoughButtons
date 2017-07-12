package de.universallp.justenoughbuttons.core.network;

import de.universallp.justenoughbuttons.client.Localization;
import de.universallp.justenoughbuttons.core.CommonProxy;
import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by universal on 20.09.2016 14:31.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class MessageMagnetMode implements IMessage, IMessageHandler<MessageMagnetMode, IMessage> {

    public boolean removePlayer;

    public MessageMagnetMode() { }

    public MessageMagnetMode(boolean remove) {
        removePlayer = remove;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        removePlayer = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(removePlayer);
    }

    @Override
    public IMessage onMessage(MessageMagnetMode message, MessageContext ctx) {
        EntityPlayerMP p = ctx.getServerHandler().player;
        MinecraftServer s = ctx.getServerHandler().player.mcServer;
        boolean isOP = MessageExecuteButton.checkPermissions(p, s);
        ITextComponent msg = new TextComponentTranslation(Localization.NO_PERMISSIONS);
        msg.setStyle(msg.getStyle().setColor(TextFormatting.RED));

        if (!isOP && ConfigHandler.magnetRequiresOP) {
            p.sendMessage(msg);
            return null;
        }

        if (message.removePlayer)
            CommonProxy.MAGNET_MODE_HANDLER.removePlayer(ctx.getServerHandler().player);
        else
            CommonProxy.MAGNET_MODE_HANDLER.addPlayer(ctx.getServerHandler().player);

        return null;
    }
}
