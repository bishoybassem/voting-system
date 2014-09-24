package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {

	private Socket socket;
	private ServerData serverData;
	private BufferedReader inFromClient;
	private PrintWriter outToClient;

	public static final int PORT = 6000;
	
	public ServerConnection(Socket socket, ServerData serverData) throws Exception {
		this.socket = socket;
		this.serverData = serverData;
		inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outToClient = new PrintWriter(socket.getOutputStream(), true);
	}

	public void run() {
		try {
			while (true) {
				String s = inFromClient.readLine();
				if (s.startsWith("Login:")){
					String[] text = s.substring(6).split("-");
					outToClient.println(serverData.getUserStatus(text[0], text[1]).ordinal());
				} else if (s.startsWith("Register:")){
					String[] text = s.substring(9).split("-");
					outToClient.println(serverData.addUser(text[0], text[1], text[2]));
				} else if (s.startsWith("Vote:")){
					String[] text = s.substring(5).split("-");
					outToClient.println(serverData.vote(text[0], text[1]));
				} else if (s.equals("GetCandidates")){
					outToClient.println(serverData.getCandidatesString());
				} else if (s.equals("GetWinner")){
					outToClient.println(serverData.getWinner());
				} else if (s.startsWith("GetVote:")){
					outToClient.println(serverData.getUserVote(s.substring(8)));
				} else if (s.startsWith("GetEndDate")){
					outToClient.println(serverData.getEndDate());
				} else if (s.startsWith("GetStartDate")){
					outToClient.println(serverData.getStartDate());
				} else if (s.equals("Quit")){
					outToClient.close();
					inFromClient.close();
					socket.close();
					break;
				}
			}
		} catch (Exception e) {

		}
	}
	
}
