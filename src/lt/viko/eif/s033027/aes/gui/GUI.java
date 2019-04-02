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
    private JProgressBar codingStatus = new JProgressBar();

    // File
    private File file = new File("C:\\Users\\tomax\\Desktop\\test – kopija");

    //
    private ProgramExecution programExecution = new ProgramExecution();

    boolean pause = false;
    boolean unPause = false;
    boolean sos = false;

    private final Object object = new Object();
    // threadai
    private  Thread t1;
    private Thread t2;

    private boolean running;
    private boolean crypting;

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
        setJProgressBar();
    }

    private void setMainPanel() {
        add(mainPanel); // setting up the mainPanel for usage.
        setVisible(true);
        mainPanel.setLayout(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 220);
        setResizable(false);
    }

    private void addToMainPanel() {
        mainPanel.add(selectDirectory); // adding components to the mainPanel;
        mainPanel.add(engagePauseAndUnpause);
        mainPanel.add(inputDirectory);
        mainPanel.add(startDecrypting);
        mainPanel.add(startEncrypting);
        mainPanel.add(sosStop);
        mainPanel.add(codingStatus);
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

    private void setJProgressBar() {
        codingStatus.setBounds(20, 150, 440, 30);
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
            unPause = !unPause;
        });
    }

    private void setStartEncrypting() {
        startEncrypting.addActionListener(e ->
        {
            startEncrypting();
        });
    }

    private void startEncrypting() {
        setUpParameterBeforeCrypting();
        createFirstThread();
        createEncryptingThread();
    }

    private void setStartDecrypting() {
        startDecrypting.addActionListener(e ->
        {
            startDecrypting();
        });
    }

    private void startDecrypting() {
        setUpParameterBeforeCrypting();
        createFirstThread();
        createDecryptingThread();
    }

    private void setUpParameterBeforeCrypting() {
        running = true;
        crypting = false;
        sos = false;
        programExecution.running = true;
        programExecution.zipRequired = false;
        programExecution.counterOfFolders = 0;
        programExecution.currentFolder = 0;
    }

    private void setSosStop() {
        sosStop.addActionListener(e ->
        {
           sos = true;
        });
    }

    private void createFirstThread() {
        t1 = new Thread(() -> {
            //programExecution.
            while(running) {
                setProgressBar();
                try {
                    t1.sleep(300);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            checkOnSosCall();
            codingStatus.setMaximum(1);
        });
        t1.start();
    }

    private void createEncryptingThread() {
        t2 = new Thread(() -> {
            programExecution.checkIfFileForEncode(file);
            running = false;
        });
        t2.start();
    }

    private void createDecryptingThread() {
        t2 = new Thread(() -> {
            programExecution.checkIfFileForDecode(file);
            running = false;
            });
        t2.start();
    }

    private void pauseThread() {
        if(pause) {
            synchronized (this.object) {
                try {
                    object.wait();
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } else if(unPause) {
            synchronized (this.object) {
                object.notify();
            }
        }
    }

    private void checkOnSosCall() {
        if(sos) {
            if(!programExecution.returnRunning()) {
                file = programExecution.returnNewFile();
                System.out.println(file.getAbsolutePath());
                if(crypting) {
                    startDecrypting();

                }
                else {
                    startEncrypting();
                }
            }
        }
    }

    private void setProgressBar() {
        try {
            codingStatus.setMaximum(programExecution.getCounterOfFolders());
            codingStatus.setValue(programExecution.getCurrentFolder());
        }
        catch (Exception e) { }
    }
}