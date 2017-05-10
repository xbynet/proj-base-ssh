package demo.netty;

import demo.ipproxy.com.myapp.main.main;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public void start() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class)
					.localAddress(port)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel channel)
								throws Exception {
							channel.pipeline().addLast(new EchoServerHandler());
						}
					});
			ChannelFuture f = b.bind().sync();
			System.out.println(EchoServer.class.getName()
					+ "started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
		}finally{
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
			}
		}
	}
	public static void main(String[] args) {
		new EchoServer(65535).start();
	}
}
