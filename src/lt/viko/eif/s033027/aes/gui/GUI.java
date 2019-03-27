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
    private JButton confirmDirectory = new JButton("Patvirtinti");

    // Progress bar
   // private JProgressBar codingStatus;

    // File
    private File file = new File("C:\\Users\\tomax\\Desktop\\JavaProjectAES");

    //
    private ProgramExecution programExecution = new ProgramExecution();

    public void executeProgram() {
        setUpGUI();
        setInputDirectory();
        setConfirmDirectory();
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
        setSize(500, 500);
    }

    private void addToMainPanel() {
        mainPanel.add(selectDirectory); // adding components to the mainPanel;
        mainPanel.add(confirmDirectory);
        mainPanel.add(inputDirectory);
    }

    private void setJTextField() {
        inputDirectory.setBounds(20, 20, 300, 30); // setting up TextField properties.
    }

    private void setJButton() {
        selectDirectory.setBounds(330, 20, 130, 30); // setting up JButton properties.
        confirmDirectory.setBounds(20, 60, 440, 30);
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

    private void setConfirmDirectory() {
        confirmDirectory.addActionListener(e ->
        {
            createFirstThread();
            createSecondThread();
        });
    }

    private void createFirstThread() {
        Thread t = new Thread(() ->
        {

        });
        t.start();
    }

    private void createSecondThread() {
        Thread t = new Thread(() ->
        {
            try {
                //programExecution.checkIfFileForEncode(file);

                //programExecution.stop = true;

                programExecution.checkIfFileForDecode(file);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}