package lt.viko.eif.s033027.aes.gui;

import lt.viko.eif.s033027.aes.execution.ProgramExecution;

import javax.swing.*;
import java.io.File;

public class GUI extends JFrame {

    private JPanel mainPanel;

    // TextFields
    private JTextField inputDirectory = new JTextField();

    // Buttons
    private JButton selectDirectory = new JButton("Direktorija");
    private JButton engagePauseAndUnpause = new JButton("Pause/Unpause");
    private JButton startEncrypting = new JButton("Užkoduoti");
    private JButton startDecrypting = new JButton("Atkoduoti");
    private JButton sosStop = new JButton("SOS STOP");

    // Progress bar
   // private JProgressBar codingStatus;

    private final Object syncObject = new Object();

    // File
    private File file = new File("C:\\Users\\tomax\\Desktop\\test – kopija");

    //
    private ProgramExecution programExecution = new ProgramExecution();

    boolean pause = false;
    // threadai
    private  Thread t1;
    private Thread t2;

    public void executeProgram() {
        setUpGUI();
        setInputDirectory();
        setEngagePauseAndUnpause();
        setStartDecrypting();
        setStartEncrypting();
        setSosStop();
    }

    private void setUpGUI() {
        setMainPanel();
        addToMainPanel();
        setJTextField();
        setJButton();
    }

    private void setMainPanel() {
        add(mainPanel); // setting up the mainPanel for usage.
        setVisible(true);
        mainPanel.setLayout(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 190);
        setResizable(false);
    }

    private void addToMainPanel() {
        mainPanel.add(selectDirectory); // adding components to the mainPanel;
        mainPanel.add(engagePauseAndUnpause);
        mainPanel.add(inputDirectory);
        mainPanel.add(startDecrypting);
        mainPanel.add(startEncrypting);
        mainPanel.add(sosStop);
    }

    private void setJTextField() {
        inputDirectory.setBounds(20, 20, 300, 30); // setting up TextField properties.
    }

    private void setJButton() {
        selectDirectory.setBounds(330, 20, 130, 30); // setting up JButton properties.
        engagePauseAndUnpause.setBounds(320, 60, 140, 30);
        startEncrypting.setBounds(20, 60, 140, 30);
        startDecrypting.setBounds(170, 60, 140, 30);
        sosStop.setBounds(20, 100, 440, 30);

    }

    private void setInputDirectory() {
        selectDirectory.addActionListener(e -> // inputDirectory button action listener
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(file);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                inputDirectory.setText(file.toString());
            }
        });
    }

    private void setEngagePauseAndUnpause() {
        engagePauseAndUnpause.addActionListener(e ->
        {
            pause = !pause;

        });
    }

    private void setStartEncrypting() {
        startEncrypting.addActionListener(e ->
        {
            createFirstThread();
            createEncryptingThread();
        });
    }

    private void setStartDecrypting() {
        startDecrypting.addActionListener(e ->
        {
            createFirstThread();
            createDecryptingThread();
        });
    }

    private void setSosStop() {
        sosStop.addActionListener(e ->
        {
            programExecution.stop = true;
        });
    }

    private void createFirstThread() {
        t1 = new Thread(() ->
        {

        });
        t1.start();
    }

    private void createEncryptingThread() {
        t2 = new Thread(() ->
        {
            try {
                programExecution.checkIfFileForEncode(file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }

    private void createDecryptingThread() {
        t2 = new Thread(() ->
        {
            try {
                programExecution.checkIfFileForDecode(file);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }
}