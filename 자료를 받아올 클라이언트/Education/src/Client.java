import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;

public class Client implements Runnable{
	private Socket socket;
	private BufferedReader in;
	private String filePath = "/Users/ATIV/Desktop/resource/";
	private String server_ip = "155.230.52.58";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(server_ip, 5679);
			socket.connect(address);
			System.out.println("서버 연결 성공");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

			System.out.println("연락처 수신 시작");
			readContact();
			System.out.println("연락처 수신 끝");

			System.out.println("위치 정보 수신 시작");
			readLocation();
			System.out.println("위치 정보 수신 끝");

			System.out.println("사진 수신 시작");
			readImage();
			System.out.println("사진 수신 끝");
			
			in.close();
			socket.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readContact() {
		PrintWriter pw = null;
		try {
			File file = new File(filePath + "Contact.txt");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		
			while(true) {
				String inputLine = null;
				inputLine = in.readLine();
				System.out.println(inputLine);
				if (inputLine.equals("end"))
					break;
				pw.println(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			pw.close();
		}
	}
	private void readLocation() {
		try {
			String inputLine = in.readLine();
			System.out.println(inputLine);
			URL url = new URL("https://www.google.com/maps/place/"+inputLine);
			Desktop.getDesktop().browse(new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void readImage() {
		try {
			int totalNum = 0;
			
			System.out.println("AA");
			totalNum = Integer.parseInt(in.readLine()); // 사진 파일 개수
			System.out.println(totalNum);

			for(int i = 0; i < totalNum; i++) {
				Socket fileSocket = new Socket();
				SocketAddress address = new InetSocketAddress(server_ip, 5679);
				fileSocket.connect(address);
				
				byte[] buffer = new byte[10240];
				FileOutputStream fos = new FileOutputStream(filePath + "testimage" + i + ".jpg");
				int read;
				System.out.println(filePath + "testimage" + i + ".jpg");
				
				BufferedInputStream bis = new BufferedInputStream(fileSocket.getInputStream());
				
				while((read = bis.read(buffer)) != -1) {
					System.out.print(String.format("%d ", read));
					fos.write(buffer,0,read);
					fos.flush();
				}
				System.out.println("");
				bis.close();
				fos.close();
				fileSocket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}