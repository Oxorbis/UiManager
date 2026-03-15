package cz.creeperface.hytale.uimanager.util

import io.netty.buffer.ByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelProgressivePromise
import io.netty.channel.ChannelPromise
import java.net.SocketAddress

class DelegatedChannel(
    private val channel: Channel,
    private val writeCallback: (Any?) -> Unit
) : Channel by channel {
    override fun isWritable(): Boolean {
        return channel.isWritable
    }

    override fun bytesBeforeUnwritable(): Long {
        return channel.bytesBeforeUnwritable()
    }

    override fun bytesBeforeWritable(): Long {
        return channel.bytesBeforeWritable()
    }

    override fun alloc(): ByteBufAllocator? {
        return channel.alloc()
    }

    override fun <T : Any?> getOption(option: ChannelOption<T?>?): T? {
        return channel.getOption(option)
    }

    override fun <T : Any?> setOption(option: ChannelOption<T?>?, value: T?): Boolean {
        return channel.setOption(option, value)
    }

    override fun read(): Channel? {
        return channel.read()
    }

    override fun flush(): Channel? {
        return channel.flush()
    }

    override fun writeAndFlush(msg: Any?): ChannelFuture? {
        writeCallback(msg)
        return channel.writeAndFlush(msg)
    }

    override fun writeAndFlush(
        msg: Any?,
        promise: ChannelPromise?
    ): ChannelFuture? {
        writeCallback(msg)
        return channel.writeAndFlush(msg, promise)
    }

    override fun write(msg: Any?, promise: ChannelPromise?): ChannelFuture? {
        writeCallback(msg)
        return channel.write(msg, promise)
    }

    override fun write(msg: Any?): ChannelFuture? {
        writeCallback(msg)
        return channel.write(msg)
    }

    override fun deregister(promise: ChannelPromise?): ChannelFuture? {
        return channel.deregister(promise)
    }

    override fun close(promise: ChannelPromise?): ChannelFuture? {
        return channel.close(promise)
    }

    override fun disconnect(promise: ChannelPromise?): ChannelFuture? {
        return channel.disconnect(promise)
    }

    override fun connect(
        remoteAddress: SocketAddress?,
        localAddress: SocketAddress?,
        promise: ChannelPromise?
    ): ChannelFuture? {
        return channel.connect(remoteAddress, localAddress, promise)
    }

    override fun connect(
        remoteAddress: SocketAddress?,
        promise: ChannelPromise?
    ): ChannelFuture? {
        return channel.connect(remoteAddress, promise)
    }

    override fun bind(
        localAddress: SocketAddress?,
        promise: ChannelPromise?
    ): ChannelFuture? {
        return channel.bind(localAddress, promise)
    }

    override fun deregister(): ChannelFuture? {
        return channel.deregister()
    }

    override fun close(): ChannelFuture? {
        return channel.close()
    }

    override fun disconnect(): ChannelFuture? {
        return channel.disconnect()
    }

    override fun connect(
        remoteAddress: SocketAddress?,
        localAddress: SocketAddress?
    ): ChannelFuture? {
        return channel.connect(remoteAddress, localAddress)
    }

    override fun connect(remoteAddress: SocketAddress?): ChannelFuture? {
        return channel.connect(remoteAddress)
    }

    override fun bind(localAddress: SocketAddress?): ChannelFuture? {
        return channel.bind(localAddress)
    }

    override fun newPromise(): ChannelPromise? {
        return channel.newPromise()
    }

    override fun newProgressivePromise(): ChannelProgressivePromise? {
        return channel.newProgressivePromise()
    }

    override fun newSucceededFuture(): ChannelFuture? {
        return channel.newSucceededFuture()
    }

    override fun newFailedFuture(cause: Throwable?): ChannelFuture? {
        return channel.newFailedFuture(cause)
    }

    override fun voidPromise(): ChannelPromise? {
        return channel.voidPromise()
    }
}