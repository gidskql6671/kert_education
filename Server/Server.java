import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Server implements Runnable{
	private ServerSocket serverSocket;
	private ServerSocket serverSocket2;
	private Socket clientSocket;
	private Socket clientSocket2;
	private BufferedReader in;
	private String filePath = "/home/gidskql6671/education/source/";
	PrintWriter pw = null;
	
	@Override
	public void run() {
		try {
		serverSocket = new ServerSocket(5678);
		serverSocket2 = new ServerSocket(5679);
		
		clientSocket = serverSocket.accept();
		clientSocket2 = serverSocket2.accept();
		
		System.out.println("Å¬¶óÀÌ¾ðÆ® ¿¬°á");
		
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		
		
		System.out.println("¿¬¶ôÃ³ ¼ö½Å ½ÃÀÛ");
		readContact();
		System.out.println("¿¬¶ôÃ³ ¼ö½Å ³¡");

		System.out.println("À§Ä¡ Á¤º¸ ¼ö½Å ½ÃÀÛ");
		readLocation();
		System.out.println("À§Ä¡ Á¤º¸ ¼ö½Å ³¡");

		System.out.println("»çÁø ¼ö½Å ½ÃÀÛ");
		readImage();
		System.out.println("»çÁø ¼ö½Å ³¡");
		
		pw.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
		System.out.println("¼­¹ö Á¾·á");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readLocation() {
		try {
			String inputLine = null;
			while (inputLine == null)
				inputLine = in.readLine();
			pw.println(inputLine);
			System.out.println(inputLine);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readContact() {
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket2.getOutputStream(), "UTF-8")),true);
		
			while(true) {
				String inputLine = null;
				inputLine = in.readLine();
				pw.println(inputLine);
				System.out.println(inputLine);
				if (inputLine.equals("end"))
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readImage() {
		try {
			int totalNum = 0;
			
			totalNum = Integer.parseInt(in.readLine()); // »çÁø ÆÄÀÏ °³¼ö
			pw.println(totalNum);
			System.out.println(totalNum);

			for(int i = 0; i < totalNum; i++) {
				Socket fileSocket = serverSocket.accept();
				Socket fileSocket2 = serverSocket2.accept();
				
				byte[] buffer = new byte[10240];
				BufferedOutputStream bs = new BufferedOutputStream(fileSocket2.getOutputStream());
				int read;
				
				BufferedInputStream bis = new BufferedInputStream(fileSocket.getInputStream());
				
				while((read = bis.read(buffer)) != -1) {
					System.out.print(String.format("%d ", read));
					bs.write(buffer,0,read);
					bs.flush();
				}
				System.out.println("");
				bis.close();
				bs.close();
				fileSocket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public int byteArrayToInt(byte bytes[]) {
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.order(ByteOrder.BIG_ENDIAN).put(bytes).flip();
        return buff.getInt();
	} 
}
