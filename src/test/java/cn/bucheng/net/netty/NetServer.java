package cn.bucheng.net.netty;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.CountDownLatch;

/**
 * @author ：yinchong
 * @create ：2019/7/2 8:48
 * @description：
 * @modified By：
 * @version:
 */
public class NetServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try{
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addFirst(new StringEncoder());
                socketChannel.pipeline().addFirst(new LengthFieldPrepender(4));
                socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
                       if("PING".equals(s)){
                           System.out.println(s);
                           return;
                       }

                        BaseMessage baseMessage = JSON.parseObject(s, BaseMessage.class);
                        System.out.println(baseMessage.getTital());
                        System.out.println(baseMessage.getBody().length);
                    }

                });
            }
        });


             bootstrap.bind(9097);
             countDownLatch.await();
            System.out.println("------------->start server 9097");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
