package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class HttpHandler implements Runnable {

	private int bufferSize = 1024;
	private String localCharset = "UTF-8";
	private SelectionKey key;

	public HttpHandler(SelectionKey key) {
		this.key = key;
	}

	public void handleAccept() throws IOException {
		SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
	}

	public void handleRead() throws IOException {
		// ��ȡchannel
		SocketChannel sc = (SocketChannel) key.channel();
		// ��ȡbuffer������
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		buffer.clear();
		// û�ж���������ر�
		if (sc.read(buffer) == -1) {
			sc.close();
		} else {
			// ������������
			buffer.flip();
			String receivedString = Charset.forName(localCharset).newDecoder().decode(buffer).toString();
			// ����̨��ӡ������ͷ
			String[] requestMessage = receivedString.split("\r\n");
			for (String s : requestMessage) {
				System.out.println(s);
				// ��������˵������ͷ�Ѿ���ӡ��
				if (s.isEmpty())
					break;
			}

			// ����̨��ӡ������Ϣ
			String[] firstLine = requestMessage[0].split(" ");
			System.out.println();
			System.out.println("Method:\t" + firstLine[0]);
			System.out.println("url:\t" + firstLine[1]);
			System.out.println("HTTP Version:\t" + firstLine[2]);
			System.out.println();

			// ���ؿͻ���
			StringBuilder sendString = new StringBuilder();
			sendString.append("HTTP/1.1 200 OK\r\n");// ��Ӧ�������У�200��ʾ�����ɹ�
			sendString.append("Content-Type:text/html;charset=" + localCharset + "\r\n");
			sendString.append("\r\n");// ����ͷ�������һ������

			sendString.append("<html><head><title>��ʾ����</title></head><body>");
			sendString.append("���յ��������ǣ�<br/>");
			for (String s : requestMessage) {
				sendString.append(s + "<br/>");
			}
			sendString.append("</body></html>");
			buffer = ByteBuffer.wrap(sendString.toString().getBytes(localCharset));
			sc.write(buffer);
			sc.close();
		}
	}

	public void run() {
		try {
			// ���յ���������ʱ
			if (key.isAcceptable()) {
				handleAccept();
			}
			// ������
			if (key.isReadable()) {
				handleRead();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}