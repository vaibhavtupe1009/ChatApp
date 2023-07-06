
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class Client extends JFrame{

    private JLabel heading = new JLabel("Client");
    private JTextArea massageArea = new JTextArea();
    private JTextField massageField = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending Request ...");
            socket = new Socket("192.168.43.195", 8888);
            System.out.println("Connection Done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
        }
        createGUI();
        massageArea.setEditable(false);
        handleEvent();
         startReader();
        // startWriter();
    }
    private void createGUI(){

        this.setTitle("Client Massager");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        massageArea.setFont(font);
        massageField.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        this.add(massageArea,BorderLayout.CENTER);
        this.add(massageField,BorderLayout.SOUTH);
        
        
        this.setVisible(true);
    }
    public void handleEvent(){ 
        massageField.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
               
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10)
                {
                    String contentToSend = massageField.getText();
                    if(!contentToSend.equalsIgnoreCase("Exit"))
                    {
                    massageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    massageField.setText("");
                    massageField.requestFocus();
                    }else{
                        massageField.setEnabled(false);
                        out.println(contentToSend);
                        out.flush();
                    }
                }
            }
         });
    }

    public void startReader() {
        Runnable r1 = () -> {

            System.out.println("Reader Started...");
            try {
                while (!socket.isClosed()) {
                    String msg = br.readLine();
                    if (msg.equalsIgnoreCase("EXIT")) {
                        System.out.println("Closed The Connection!");
                        JOptionPane.showMessageDialog(this,"Connection Is Close");
                        massageField.setText("Connection Closed.");
                        massageField.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : " + msg);
                    massageArea.append("Server : "+msg+"\n");
                }
            } catch (Exception e) {
                System.out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriter() {
        Runnable r2 = () -> {
            System.out.println("Writer Started..");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String content = br.readLine();
                     
                    out.println(content);
                    out.flush();
                    if(content.equalsIgnoreCase("EXIT"))
                        {
                            socket.close();
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String args[]) {
        new Client();
    }

}
