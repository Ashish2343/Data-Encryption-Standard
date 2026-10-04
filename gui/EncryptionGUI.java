package gui;

import service.DESService;
import service.TripleDESService;
import file.FileEncryptionService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EncryptionGUI {

    private JFrame frame;

    private JComboBox<String> algorithmBox;
    private JComboBox<String> modeBox;

    private JTextField key1Field;
    private JTextField key2Field;
    private JTextField key3Field;

    private JTextArea inputArea;
    private JTextArea outputArea;

    private JButton encryptButton;
    private JButton decryptButton;

    private JButton selectFileButton;

    private JLabel selectedFileLabel;

    private File selectedFile;


    public EncryptionGUI() {

        // -----------------------------
        // Main Window
        // -----------------------------

        frame = new JFrame("DES / 3DES Encryption Tool");

        frame.setSize(700, 600);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLayout(
                new BorderLayout(10, 10)
        );


        // -----------------------------
        // Algorithm Panel
        // -----------------------------

        JPanel algorithmPanel = new JPanel();

        JLabel algorithmLabel =
                new JLabel("Algorithm:");

        algorithmBox =
                new JComboBox<>(
                        new String[]{"DES", "3DES"}
                );

        algorithmPanel.add(algorithmLabel);
        algorithmPanel.add(algorithmBox);


        // -----------------------------
        // Mode Panel
        // -----------------------------

        JPanel modePanel = new JPanel();

        JLabel modeLabel =
                new JLabel("Mode:");

        modeBox =
                new JComboBox<>(
                        new String[]{"Text", "File"}
                );

        modePanel.add(modeLabel);
        modePanel.add(modeBox);


        // -----------------------------
        // Key Panel
        // -----------------------------

        JPanel keyPanel = new JPanel();

        key1Field = new JTextField(16);
        key2Field = new JTextField(16);
        key3Field = new JTextField(16);

        keyPanel.add(
                new JLabel("Key 1:")
        );

        keyPanel.add(key1Field);

        keyPanel.add(
                new JLabel("Key 2:")
        );

        keyPanel.add(key2Field);

        keyPanel.add(
                new JLabel("Key 3:")
        );

        keyPanel.add(key3Field);


        // -----------------------------
        // File Panel
        // -----------------------------

        JPanel filePanel = new JPanel();

        selectFileButton =
                new JButton("Select File");

        selectedFileLabel =
                new JLabel("No file selected");

        filePanel.add(selectFileButton);
        filePanel.add(selectedFileLabel);


        // -----------------------------
        // Input Text Area
        // -----------------------------

        JPanel inputPanel =
                new JPanel(new BorderLayout());

        inputPanel.add(
                new JLabel("Input Text:"),
                BorderLayout.NORTH
        );

        inputArea =
                new JTextArea(8, 50);

        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        JScrollPane inputScrollPane =
                new JScrollPane(inputArea);

        inputPanel.add(
                inputScrollPane,
                BorderLayout.CENTER
        );


        // -----------------------------
        // Output Text Area
        // -----------------------------

        JPanel outputPanel =
                new JPanel(new BorderLayout());

        outputPanel.add(
                new JLabel("Output:"),
                BorderLayout.NORTH
        );

        outputArea =
                new JTextArea(8, 50);

        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        outputArea.setEditable(false);

        JScrollPane outputScrollPane =
                new JScrollPane(outputArea);

        outputPanel.add(
                outputScrollPane,
                BorderLayout.CENTER
        );


        // -----------------------------
        // Buttons
        // -----------------------------

        JPanel buttonPanel = new JPanel();

        encryptButton =
                new JButton("Encrypt");

        decryptButton =
                new JButton("Decrypt");

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);


        // -----------------------------
        // Top Panel
        // -----------------------------

        JPanel topPanel =
                new JPanel();

        topPanel.setLayout(
                new BoxLayout(
                        topPanel,
                        BoxLayout.Y_AXIS
                )
        );

        topPanel.add(algorithmPanel);
        topPanel.add(modePanel);
        topPanel.add(keyPanel);
        topPanel.add(filePanel);


        // -----------------------------
        // Center Panel
        // -----------------------------

        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10
                        )
                );

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);


        // -----------------------------
        // Add Panels to JFrame
        // -----------------------------

        frame.add(
                topPanel,
                BorderLayout.NORTH
        );

        frame.add(
                centerPanel,
                BorderLayout.CENTER
        );

        frame.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // -----------------------------
        // Button Events
        // -----------------------------

        encryptButton.addActionListener(
                e -> encrypt()
        );

        decryptButton.addActionListener(
                e -> decrypt()
        );

        selectFileButton.addActionListener(
                e -> selectFile()
        );


        // -----------------------------
        // Mode Change Event
        // -----------------------------

        modeBox.addActionListener(
                e -> updateMode()
        );


        updateMode();


        // -----------------------------
        // Show Window
        // -----------------------------

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }


    // =================================================
    // MODE
    // =================================================

    private void updateMode() {

        String mode =
                (String) modeBox.getSelectedItem();

        boolean fileMode =
                mode.equals("File");

        selectFileButton.setVisible(
                fileMode
        );

        selectedFileLabel.setVisible(
                fileMode
        );

        inputArea.setVisible(
                !fileMode
        );
    }


    // =================================================
    // SELECT FILE
    // =================================================

    private void selectFile() {

        JFileChooser fileChooser =
                new JFileChooser();

        int result =
                fileChooser.showOpenDialog(frame);

        if (result ==
                JFileChooser.APPROVE_OPTION) {

            selectedFile =
                    fileChooser.getSelectedFile();

            selectedFileLabel.setText(
                    selectedFile.getName()
            );
        }
    }


    // =================================================
    // ENCRYPT
    // =================================================

    private void encrypt() {

        try {

            String mode =
                    (String) modeBox.getSelectedItem();


            // -----------------------------------------
            // FILE MODE
            // -----------------------------------------

            if (mode.equals("File")) {

                encryptFile();

                return;
            }


            // -----------------------------------------
            // TEXT MODE
            // -----------------------------------------

            encryptText();

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Encryption failed:\n"
                            + e.getMessage()
            );
        }
    }


    // =================================================
    // DECRYPT
    // =================================================

    private void decrypt() {

        try {

            String mode =
                    (String) modeBox.getSelectedItem();


            // -----------------------------------------
            // FILE MODE
            // -----------------------------------------

            if (mode.equals("File")) {

                decryptFile();

                return;
            }


            // -----------------------------------------
            // TEXT MODE
            // -----------------------------------------

            decryptText();

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Decryption failed:\n"
                            + e.getMessage()
            );
        }
    }


    // =================================================
    // TEXT ENCRYPTION
    // =================================================

    private void encryptText()
            throws Exception {

        String algorithm =
                (String) algorithmBox.getSelectedItem();

        String input =
                inputArea.getText();


        if (input.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter some text."
            );

            return;
        }


        long key1 =
                getKey(key1Field);


        if (algorithm.equals("DES")) {

            DESService service =
                    new DESService(key1);

            byte[] encrypted =
                    service.encrypt(
                            input.getBytes()
                    );

            outputArea.setText(
                    bytesToHex(encrypted)
            );

            service.shutdown();

        }
        else {

            long key2 =
                    getKey(key2Field);

            long key3 =
                    getKey(key3Field);


            TripleDESService service =
                    new TripleDESService(
                            key1,
                            key2,
                            key3
                    );

            byte[] encrypted =
                    service.encrypt(
                            input.getBytes()
                    );

            outputArea.setText(
                    bytesToHex(encrypted)
            );

            service.shutdown();
        }
    }


    // =================================================
    // TEXT DECRYPTION
    // =================================================

    private void decryptText()
            throws Exception {

        String algorithm =
                (String) algorithmBox.getSelectedItem();

        String input =
                inputArea.getText().trim();


        if (input.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter encrypted HEX."
            );

            return;
        }


        long key1 =
                getKey(key1Field);

        byte[] encrypted =
                hexToBytes(input);


        if (algorithm.equals("DES")) {

            DESService service =
                    new DESService(key1);

            byte[] decrypted =
                    service.decrypt(encrypted);

            outputArea.setText(
                    new String(decrypted)
            );

            service.shutdown();

        }
        else {

            long key2 =
                    getKey(key2Field);

            long key3 =
                    getKey(key3Field);


            TripleDESService service =
                    new TripleDESService(
                            key1,
                            key2,
                            key3
                    );

            byte[] decrypted =
                    service.decrypt(encrypted);

            outputArea.setText(
                    new String(decrypted)
            );

            service.shutdown();
        }
    }


    // =================================================
    // FILE ENCRYPTION
    // =================================================

    private void encryptFile()
            throws Exception {

        if (selectedFile == null) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a file first."
            );

            return;
        }


        long key1 =
                getKey(key1Field);

        long key2 =
                getKey(key2Field);

        long key3 =
                getKey(key3Field);


        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setSelectedFile(
                new File(
                        selectedFile.getName()
                                + ".enc"
                )
        );


        int result =
                fileChooser.showSaveDialog(frame);


        if (result !=
                JFileChooser.APPROVE_OPTION) {

            return;
        }


        File outputFile =
                fileChooser.getSelectedFile();


        FileEncryptionService service =
                new FileEncryptionService(
                        key1,
                        key2,
                        key3
                );


        service.encryptFile(
                selectedFile.getAbsolutePath(),
                outputFile.getAbsolutePath()
        );


        service.shutdown();


        JOptionPane.showMessageDialog(
                frame,
                "File encrypted successfully!"
        );
    }


    // =================================================
    // FILE DECRYPTION
    // =================================================

    private void decryptFile()
            throws Exception {

        if (selectedFile == null) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please select an encrypted file."
            );

            return;
        }


        long key1 =
                getKey(key1Field);

        long key2 =
                getKey(key2Field);

        long key3 =
                getKey(key3Field);


        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setSelectedFile(
                new File(
                        "decrypted_"
                                + selectedFile.getName()
                )
        );


        int result =
                fileChooser.showSaveDialog(frame);


        if (result !=
                JFileChooser.APPROVE_OPTION) {

            return;
        }


        File outputFile =
                fileChooser.getSelectedFile();


        FileEncryptionService service =
                new FileEncryptionService(
                        key1,
                        key2,
                        key3
                );


        service.decryptFile(
                selectedFile.getAbsolutePath(),
                outputFile.getAbsolutePath()
        );


        service.shutdown();


        JOptionPane.showMessageDialog(
                frame,
                "File decrypted successfully!"
        );
    }


    // =================================================
    // KEY CONVERSION
    // =================================================

    private long getKey(JTextField field) {

        return Long.parseUnsignedLong(
                field.getText().trim(),
                16
        );
    }


    // =================================================
    // BYTE[] → HEX
    // =================================================

    private String bytesToHex(byte[] data) {

        StringBuilder result =
                new StringBuilder();

        for (byte b : data) {

            result.append(
                    String.format(
                            "%02X",
                            b
                    )
            );
        }

        return result.toString();
    }


    // =================================================
    // HEX → BYTE[]
    // =================================================

    private byte[] hexToBytes(String hex) {

        if (hex.length() % 2 != 0) {

            throw new IllegalArgumentException(
                    "Invalid hexadecimal input."
            );
        }


        byte[] result =
                new byte[hex.length() / 2];


        for (int i = 0;
             i < hex.length();
             i += 2) {

            result[i / 2] =
                    (byte) Integer.parseInt(
                            hex.substring(i, i + 2),
                            16
                    );
        }


        return result;
    }


    // =================================================
    // MAIN
    // =================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                EncryptionGUI::new
        );
    }
}