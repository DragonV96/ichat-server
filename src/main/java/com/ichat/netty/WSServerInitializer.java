package com.ichat.netty;

import com.ichat.netty.handler.ChatHandler;
import com.ichat.netty.handler.HeartBeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Create by glw
 * 2018/11/11 23:34
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final int READERIDLESECONDS = 30;        //读空闲间隔秒数
    private static final int WRITERIDLESECONDS = 40;        //写空闲间隔秒数
    private static final int ALLIDLESECONDS = 60;           //读写空闲间隔秒数


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // Websocket 基于http协议，所以要有http编解码器
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对httpMessage进行聚合，聚合城FullHttpRequest或FullHttpResponse
        // 几乎在netty中的编程，都会使用到此Handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //========================= 以上是用于支持http协议 ===========================


        //========================= 增加心跳支持 start ===========================

        // 针对客户端，如果在1分钟时，没有向服务端发送读写心跳（ALL），则主动断开
        // 如果是读空闲或者写空闲，不处理
        pipeline.addLast(new IdleStateHandler(READERIDLESECONDS, WRITERIDLESECONDS, ALLIDLESECONDS));
        // 自定义的空闲状态监测
        pipeline.addLast(new HeartBeatHandler());


        //========================= 增加心跳支持 end ===========================


        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由：/ws
         * 本handler负责处理一些繁重复杂的事
         * 处理握手动作：handshaking（close，ping，pong）ping + pong = 心跳
         * websocket都是以frames进行传输的，不同数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new ChatHandler());
    }
}
