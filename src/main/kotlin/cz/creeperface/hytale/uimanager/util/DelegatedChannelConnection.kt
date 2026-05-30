package cz.creeperface.hytale.uimanager.util

import com.hypixel.hytale.protocol.ToClientPacket
import com.hypixel.hytale.protocol.io.ChannelConnection

class DelegatedChannelConnection(
    private val connection: ChannelConnection,
    private val writeCallback: (ToClientPacket) -> Unit
) : ChannelConnection by connection {
    override fun write(packet: ToClientPacket) {
        writeCallback(packet)
        connection.write(packet)
    }

    override fun writeAndFlush(packet: ToClientPacket) {
        writeCallback(packet)
        connection.writeAndFlush(packet)
    }

    override fun write(packets: Array<ToClientPacket>) {
        packets.forEach(writeCallback)
        connection.write(packets)
    }

    override fun writeAndFlush(packets: Array<ToClientPacket>) {
        packets.forEach(writeCallback)
        connection.writeAndFlush(packets)
    }
}
