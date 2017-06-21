import java.io.IOException;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import org.junit.Test;

public class Test3 {
	@Test
	public void test1() throws IOException{
		final SSHClient ssh = new SSHClient();
//		ssh.loadKnownHosts();
		ssh.addHostKeyVerifier(new HostKeyVerifier() {
			@Override
			public boolean verify(String paramString, int paramInt,
					PublicKey paramPublicKey) {
				return true;
			}
		});
		ssh.connect("139.129.111.84");
		try {
			ssh.authPassword("root", "Ipmacro709394");
//			ssh.authPublickey(System.getProperty("user.name"));
			final Session session = ssh.startSession();
			try {
				final Command cmd = session.exec("rm -rf aaaaa");
				System.out.println(IOUtils.readFully(cmd.getInputStream())
						.toString());
				cmd.join(5, TimeUnit.SECONDS);
				System.out.println("\n** exit status: " + cmd.getExitStatus());
			} finally {
				session.close();
			}
		} finally {
			ssh.disconnect();
		}
	}
	public static void main(String[] args) throws Exception {
		
	}
}
