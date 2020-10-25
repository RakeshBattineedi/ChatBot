//RAKESH BATTINEEDI
package a2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Allow the Chatbot to be accessible over the network.  <br />
 * This class only handles one client at a time.  Multiple instances of ChatbotServer
 * will be run on different ports with a port-based load balancer to handle multiple clients.
 *
 * @author RAKESH BATTINEEDI
 */
public class ChatbotServer {

	/**
	 * The instance of the {@link Chatbot}.
	 */
	private Chatbot chatbot;

	/**
	 * The instance of the {@link ServerSocket}.
	 */
	private ServerSocket serversocket;

	/**
	 * Constructor for ChatbotServer.
	 *
	 * @param chatbot The chatbot to use.
	 * @param serversocket The pre-configured ServerSocket to use.
	 */
	public ChatbotServer(Chatbot chatbot, ServerSocket serversocket) {
		this.chatbot = chatbot;
		this.serversocket = serversocket;
	}

	/**
	 * Start the Chatbot server.  Does not return.
	 */
	public void startServer() {
		while(true) handleOneClient();
	}

	/**
	 * Handle interaction with a single client.  See assignment description.
	 */
	public void handleOneClient()
	{
		try
		{
			Socket socket = serversocket.accept();
			BufferedReader inputbuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outputbuffer = new PrintWriter(socket.getOutputStream(), true);

			while(true)
			{
				String input = inputbuffer.readLine();
				String output;

				if(input == null)
				{
					socket.close();
					serversocket.close();
					break;
				}
				try
				{
					output = chatbot.getResponse(input);
				}
				catch (AIException e)
				{
					output = "Got AIException:"+e.getMessage();
				}
				outputbuffer.println(output);
			}
		} catch (Exception e) {e.printStackTrace();}
	}
}
