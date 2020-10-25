//RAKESH BATTINEEDI
package a2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestChatbotServer
{
    @Mock
    public Chatbot mockChatbot;

    @Mock
    public ServerSocket mockServerSocket;

    @Mock
    public Socket mockSocket;

    public ChatbotServer myServer;

    @Before
    public void setUp() throws IOException
    {
        myServer = new ChatbotServer(mockChatbot,mockServerSocket);
    }

    @Test
    public void testHandleOneClient() throws Exception
    {
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        when(mockChatbot.getResponse("input")).thenReturn("output");

        InputStream inputstream = new ByteArrayInputStream("input\n".getBytes());
        OutputStream outputstream = new ByteArrayOutputStream();

        when(mockSocket.getInputStream()).thenReturn(inputstream);
        when(mockSocket.getOutputStream()).thenReturn(outputstream);

        myServer.handleOneClient();

        assertEquals("output\n", outputstream.toString());
    }

    @Test
    public void testSocketIOException() throws Exception
    {
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        when(mockChatbot.getResponse("input")).thenReturn("response");

        InputStream inputstream = new ByteArrayInputStream("input\n".getBytes());
        OutputStream outputstream = new ByteArrayOutputStream();
        when(mockSocket.getInputStream()).thenThrow(new IOException("socket exception")).thenReturn(inputstream);
        when(mockSocket.getOutputStream()).thenReturn(outputstream);

        myServer.handleOneClient();
        myServer.handleOneClient();

        assertEquals("response\n", outputstream.toString());
    }

    @Test
    public void testAIException() throws Exception
    {
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        when(mockChatbot.getResponse("input")).thenThrow(new AIException("AIException"));

        InputStream inputstream = new ByteArrayInputStream("input\n".getBytes());
        OutputStream outputstream = new ByteArrayOutputStream();
        when(mockSocket.getInputStream()).thenReturn(inputstream);
        when(mockSocket.getOutputStream()).thenReturn(outputstream);

        myServer.handleOneClient();

        assertEquals("Got AIException:"+"AIException\n", outputstream.toString());
    }

    @Test
    public void testOutputStreamIsNull() throws Exception
    {
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        when(mockChatbot.getResponse("input")).thenReturn("output");

        InputStream inputstream = new ByteArrayInputStream("input\n".getBytes());
        OutputStream outputstream = new ByteArrayOutputStream();
        when(mockSocket.getInputStream()).thenReturn(inputstream);
        when(mockSocket.getOutputStream()).thenReturn(null).thenReturn(outputstream);

        myServer.handleOneClient();
        myServer.handleOneClient();

        assertEquals("output\n", outputstream.toString());
    }

    @Test
    public void testInputStreamIsNull() throws Exception
    {
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        when(mockChatbot.getResponse("input")).thenReturn("output");

        InputStream inputstream = new ByteArrayInputStream("input\n".getBytes());
        OutputStream outputstream = new ByteArrayOutputStream();
        when(mockSocket.getInputStream()).thenReturn(null).thenReturn(inputstream);
        when(mockSocket.getOutputStream()).thenReturn(outputstream);

        myServer.handleOneClient();
        myServer.handleOneClient();

        assertEquals("output\n", outputstream.toString());
    }

    @Test
    public void testChatbotReturnsNull() throws Exception
    {
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        when(mockChatbot.getResponse("input")).thenReturn(null);

        InputStream inputstream = new ByteArrayInputStream("input\n".getBytes());
        OutputStream outputstream = new ByteArrayOutputStream();
        when(mockSocket.getInputStream()).thenReturn(inputstream);
        when(mockSocket.getOutputStream()).thenReturn(outputstream);

        myServer.handleOneClient();

        assertEquals("null\n", outputstream.toString());
    }
}
