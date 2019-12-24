public class Main {
		public static void main(String[] args) {

					System.out.println("서버 시작");

							Thread server = new Thread(new Server());
									server.start();
											
											
										}
}

