package main;


import java.awt.Font;
import java.awt.FontFormatException;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import words.FileWord;

/**
 *
 * @author Sebastian152
 */
public class Hangman extends javax.swing.JFrame {

    /*Variables*/
    Font font;
    int lifes;
    int currentIcon = 0;
    String currentWord = new String();
    FileWord words = new FileWord("src/words/words.txt");
    String[] imagePaths = {
                            "src/img/begin.jpg", 
                            "src/img/phase1.jpg", 
                            "src/img/phase2.jpg",
                            "src/img/phase3.jpg", 
                            "src/img/phase4.jpg", 
                            "src/img/phase5.jpg",
                            "src/img/phase6.jpg",
                            "src/img/gameLoss.jpg",
                            "src/img/gameWin.jpg"
                            };
    ImageIcon[] icons = new ImageIcon[imagePaths.length];
    
    private void startIcons() {
        for (int i = 0; i < imagePaths.length; i++) {
            icons[i] = new ImageIcon(imagePaths[i]);
        }
    }
    
    private void startGame() throws IOException, UnsupportedAudioFileException {
        lblHangmanImage.setIcon(icons[0]);
        lifes = 7;
        lblHangmanLifes.setText(
                 "Lifes " + String.valueOf(lifes));
        currentIcon = 0;
        btnHangmanPlay.setEnabled(false);
        // Enabling the stuff
        btnHangmanChoosed.setEnabled(true);
        txtHangmanChoosedLetter.setEnabled(true);
        currentWord = words.getWord().toUpperCase();
        char[] wordGuessedChars;
        wordGuessedChars = new char[currentWord.length()];
        Arrays.fill(wordGuessedChars, '_');
        String wordGuessed = new String();
        for(char c : wordGuessedChars) {
            wordGuessed += c + " ";
        }
        lblWord.setText(wordGuessed);
        
    }
    
    private void choosingChar() throws LineUnavailableException, IOException, InterruptedException {
        // Obtaining an unique character from the JTextField
        char[] wordGuessedChars;
        
        String str = lblWord.getText().replaceAll("\\s", "");
        
        wordGuessedChars = new char[str.length()];
        wordGuessedChars = str.toCharArray(); //cadena normal _____ sin espacios
                
        boolean atLeastOneConcurrence = false;
        char choosedChar = txtHangmanChoosedLetter.getText().toUpperCase().charAt(0);
        for(int i = 0; i < currentWord.length(); i++) {
            if(currentWord.charAt(i) == choosedChar) {
                atLeastOneConcurrence = true;
                wordGuessedChars[i] = choosedChar;
            }
        }
        System.gc();
        if(atLeastOneConcurrence == false) {
            fail();
        }
        else {
            String wordGuessed = new String(wordGuessedChars);
            Clip clip = AudioSystem.getClip();
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                        new File("src/resources/sounds/Bubble.wav")
                                .getAbsoluteFile());
                clip.open(audioInputStream);
            } catch (UnsupportedAudioFileException ex) {
                showError(ex);
            }
            // Cargar el archivo de sonido en el Clip


            // Reproducir el sonido
            clip.start();

             // Esperar a que termine de reproducirse el sonido
                while (!clip.isRunning()) {
                    Thread.sleep(1);
                }
                while (clip.isRunning()) {
                    Thread.sleep(1);
                }      
            lblWord.setText(wordGuessed);
            clip.close();
        }
        if (currentWord.equals(lblWord.getText())) {
            winGame();
        } 
    }
    
    private void loseGame() throws LineUnavailableException, IOException, InterruptedException {
        Clip clip = AudioSystem.getClip();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File("src/resources/sounds/Error.wav")
                            .getAbsoluteFile());
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException ex) {
            showError(ex);
        }
        // Cargar el archivo de sonido en el Clip
        
        
        // Reproducir el sonido
        clip.start();
        
         // Esperar a que termine de reproducirse el sonido
            while (!clip.isRunning()) {
                Thread.sleep(1);
            }
            message("Mission failed, you loss!"); 
            
        clip.close();
        gameOver();
    }
    
    private void winGame() throws IOException, LineUnavailableException, InterruptedException {
        lblHangmanImage.setIcon(icons[8]);
        Clip clip = AudioSystem.getClip();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File("src/resources/sounds/Win.wav")
                            .getAbsoluteFile());
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException ex) {
            showError(ex);
        }
        // Reproducir el sonido
        clip.start();
         // Esperar a que termine de reproducirse el sonido
            while (!clip.isRunning()) {
                Thread.sleep(1);
            }
            
        message("Congratulations, you win");
        clip.close();
        
        
        gameOver();
    }
    
    private void fail() throws LineUnavailableException, IOException, InterruptedException {
        lifes--;
        lblHangmanLifes.setText("Lifes " + String.valueOf(lifes));
        lblHangmanImage.setIcon(icons[++currentIcon]);
        Clip clip = AudioSystem.getClip();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File("src/resources/sounds/Fail.wav")
                            .getAbsoluteFile());
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException ex) {
            showError(ex);
        }
        // Cargar el archivo de sonido en el Clip
        
        
        // Reproducir el sonido
        clip.start();
        
        // Esperar a que termine de reproducirse el sonido
        while (!clip.isRunning()) {
            Thread.sleep(1);
        }
        while (clip.isRunning()) {
            Thread.sleep(1);
        }
            
        clip.close();
        if(lifes==0) {
            loseGame();
        }
    }
    
    private void gameOver() {
        btnHangmanChoosed.setEnabled(false);
        btnHangmanPlay.setEnabled(true);
        txtHangmanChoosedLetter.setText("");
        txtHangmanChoosedLetter.setEnabled(false);
    }
    
    /**
     * Creates new form Hangman
     */
    public Hangman() throws FontFormatException, IOException, UnsupportedAudioFileException {
        startIcons();
        initComponents();
        this.font = Font.createFont(Font.TRUETYPE_FONT, 
                new File("src/resources/Alkatra-VariableFont_wght.ttf")).
                deriveFont(Font.PLAIN, 20);
        lblHangmanLifes.setFont(font);
        lblHangmanLetterChoosed.setFont(font);
        lblHangmanWordTitle.setFont(font);
        btnHangmanChoosed.setFont(font);
        txtHangmanChoosedLetter.setFont(font);
        lblWord.setFont(font);
        btnHangmanPlay.setFont(font);
        setLocationRelativeTo(null);
        
    }
    
    // Static methods
    /**
     * It will show any error via JOptionPane
     * @param ex Any exception type, NullPointer, IOException, etc.
     */
    public static void showError(Throwable ex){
       JOptionPane.showMessageDialog(null, 
                    "An error has ocurred: " + ex.getMessage(), 
               "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static void message(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panHangman = new javax.swing.JPanel();
        lblHangmanLifes = new javax.swing.JLabel();
        lblHangmanImage = new javax.swing.JLabel();
        lblHangmanWordTitle = new javax.swing.JLabel();
        panHangmanWord = new javax.swing.JPanel();
        lblWord = new javax.swing.JLabel();
        txtHangmanChoosedLetter = new javax.swing.JTextField();
        lblHangmanLetterChoosed = new javax.swing.JLabel();
        btnHangmanChoosed = new javax.swing.JButton();
        btnHangmanPlay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panHangman.setBorder(new javax.swing.border.MatteBorder(null));

        lblHangmanLifes.setBackground(new java.awt.Color(255, 255, 255));
        lblHangmanLifes.setForeground(new java.awt.Color(0, 0, 0));
        lblHangmanLifes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHangmanLifes.setText("Lifes");

        lblHangmanImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHangmanImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/begin.jpg"))); // NOI18N
        lblHangmanImage.setText("jLabel1");

        lblHangmanWordTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHangmanWordTitle.setText("Word");

        panHangmanWord.setBackground(new java.awt.Color(255, 255, 255));

        lblWord.setForeground(new java.awt.Color(0, 0, 0));
        lblWord.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panHangmanWordLayout = new javax.swing.GroupLayout(panHangmanWord);
        panHangmanWord.setLayout(panHangmanWordLayout);
        panHangmanWordLayout.setHorizontalGroup(
            panHangmanWordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panHangmanWordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panHangmanWordLayout.setVerticalGroup(
            panHangmanWordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panHangmanWordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWord, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panHangmanLayout = new javax.swing.GroupLayout(panHangman);
        panHangman.setLayout(panHangmanLayout);
        panHangmanLayout.setHorizontalGroup(
            panHangmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panHangmanLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblHangmanImage, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(panHangmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHangmanWordTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panHangmanWord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHangmanLifes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panHangmanLayout.setVerticalGroup(
            panHangmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panHangmanLayout.createSequentialGroup()
                .addGroup(panHangmanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panHangmanLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblHangmanImage, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panHangmanLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblHangmanLifes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblHangmanWordTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panHangmanWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        txtHangmanChoosedLetter.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtHangmanChoosedLetter.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHangmanChoosedLetter.setEnabled(false);
        txtHangmanChoosedLetter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHangmanChoosedLetterKeyTyped(evt);
            }
        });

        lblHangmanLetterChoosed.setBackground(new java.awt.Color(255, 255, 255));
        lblHangmanLetterChoosed.setFont(font);
        lblHangmanLetterChoosed.setForeground(new java.awt.Color(0, 0, 0));
        lblHangmanLetterChoosed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHangmanLetterChoosed.setText("Choose a letter:");

        btnHangmanChoosed.setBackground(new java.awt.Color(255, 255, 255));
        btnHangmanChoosed.setForeground(new java.awt.Color(0, 0, 0));
        btnHangmanChoosed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons/btnSubmit.png"))); // NOI18N
        btnHangmanChoosed.setText("Submit");
        btnHangmanChoosed.setEnabled(false);
        btnHangmanChoosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHangmanChoosedActionPerformed(evt);
            }
        });

        btnHangmanPlay.setBackground(new java.awt.Color(255, 255, 255));
        btnHangmanPlay.setForeground(new java.awt.Color(0, 0, 0));
        btnHangmanPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons/playBtn.png"))); // NOI18N
        btnHangmanPlay.setText("Play");
        btnHangmanPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHangmanPlayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panHangman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblHangmanLetterChoosed, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtHangmanChoosedLetter, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnHangmanChoosed, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(btnHangmanPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panHangman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHangmanChoosedLetter, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(lblHangmanLetterChoosed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHangmanChoosed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHangmanPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtHangmanChoosedLetterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHangmanChoosedLetterKeyTyped
        String text = txtHangmanChoosedLetter.getText();
        if (text.length() >= 1) {
            txtHangmanChoosedLetter.setText("");
        }
    }//GEN-LAST:event_txtHangmanChoosedLetterKeyTyped

    private void btnHangmanChoosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHangmanChoosedActionPerformed
        try {
            choosingChar();
        } catch (LineUnavailableException 
                | IOException ex) {
            showError(ex);
        } catch (InterruptedException ex) {
            showError(ex);
        }
    }//GEN-LAST:event_btnHangmanChoosedActionPerformed

    private void btnHangmanPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHangmanPlayActionPerformed
        try {
            startGame();
        } catch (IOException | UnsupportedAudioFileException ex) {
            showError(ex);
        }
    }//GEN-LAST:event_btnHangmanPlayActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            words.closeFile();
        } catch (IOException ex) {
            showError(ex);
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException 
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hangman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Hangman().setVisible(true);
                } catch (FontFormatException 
                        | IOException 
                        | UnsupportedAudioFileException ex) {
                    showError(ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHangmanChoosed;
    private javax.swing.JButton btnHangmanPlay;
    private javax.swing.JLabel lblHangmanImage;
    private javax.swing.JLabel lblHangmanLetterChoosed;
    private javax.swing.JLabel lblHangmanLifes;
    private javax.swing.JLabel lblHangmanWordTitle;
    private javax.swing.JLabel lblWord;
    private javax.swing.JPanel panHangman;
    private javax.swing.JPanel panHangmanWord;
    private javax.swing.JTextField txtHangmanChoosedLetter;
    // End of variables declaration//GEN-END:variables
}
