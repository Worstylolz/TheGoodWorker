import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI implements ActionListener {

    Random random = new Random();

    private JLabel timeoutBetween;
    private JTextField timeoutTimer;
    private JLabel succes;
    private JFrame frame;
    private JPanel panel;
    private JLabel buttonsLabel;
    private JLabel pocetLabel;
    private JButton button;

    private JButton button2;
    private JTextField buttonsTextField;
    private JTextField pocetTextArea;
    private JCheckBox checkBox; // Added checkbox
    private JCheckBox checkBoxShowConsole; // Added checkbox na konzoli

    int timerTime = 0;
    private Thread workerThread;

    private JTextArea consoleTextArea;
    private JScrollPane scrollPane;




    public GUI() {
        frame = new JFrame();
        panel = new JPanel();
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setLocationRelativeTo(null);

        panel.setLayout(null);

        frame.setTitle("The good worker");
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        buttonsLabel = new JLabel("Buttons");
        buttonsLabel.setBounds(10, 20, 80, 25);
        panel.add(buttonsLabel);

        buttonsTextField = new JTextField(20);
        buttonsTextField.setBounds(100, 20, 165, 25);
        panel.add(buttonsTextField);

        pocetLabel = new JLabel("Clics");
        pocetLabel.setBounds(10, 50, 80, 25);
        panel.add(pocetLabel);

        pocetTextArea = new JTextField(20);
        pocetTextArea.setBounds(100, 50, 165, 25);
        panel.add(pocetTextArea);

        timeoutBetween = new JLabel("ms");
        timeoutBetween.setBounds(10, 80, 80, 25);
        panel.add(timeoutBetween);

        timeoutTimer = new JTextField(20);
        timeoutTimer.setBounds(100, 80, 165, 25);
        panel.add(timeoutTimer);

        button = new JButton("Start");
        button.setBounds(10, 110, 80, 25);
        button.addActionListener(this);
        panel.add(button);

        button2 = new JButton("Stop");
        button2.setBounds(90, 110, 80, 25);
        button2.addActionListener(e -> {
            if (workerThread != null) {
                workerThread.interrupt();
                workerThread = null; // Vyčistíme odkaz na vlákno
                succes.setText("Proces byl zastaven.");
            }
        });
        panel.add(button2);


        checkBox = new JCheckBox("DisenchanterMode");
        checkBox.setBounds(180, 110, 100, 25); // Set the checkbox position
        panel.add(checkBox);

        checkBoxShowConsole = new JCheckBox("console");
        checkBoxShowConsole.setBounds(10, 140, 100, 25); // Set the checkbox position
        panel.add(checkBoxShowConsole);
        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        consoleTextArea.setBounds(10, 170, 600, 200);
        consoleTextArea.setVisible(false);
        consoleTextArea.setWrapStyleWord(true);
        consoleTextArea.setLineWrap(true);
        consoleTextArea.getScrollableTracksViewportWidth();
        DefaultCaret caret = (DefaultCaret) consoleTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        consoleTextArea.setCaret(caret);
        panel.add(consoleTextArea);

        scrollPane = new JScrollPane(consoleTextArea); // Vytvoření JScrollPane kolem JTextArea
        scrollPane.setBounds(10, 170, 600, 200); // Nastavte umístění JScrollPane
        scrollPane.setVisible(false); // Skryj JScrollPane na začátku
        panel.add(scrollPane);

        checkBoxShowConsole.addActionListener(e -> {
            boolean consoleVisible = checkBoxShowConsole.isSelected();
            consoleTextArea.setVisible(consoleVisible);
            scrollPane.setVisible(true);
            consoleTextArea.revalidate();
            consoleTextArea.repaint();
        });

        succes = new JLabel("");
        succes.setBounds(110, 140, 600, 25);;
        panel.add(succes);

        frame.setVisible(true);

    }


    public static void main(String[] args) {
        new GUI();
    }

    private void logToConsole(String message) {
        consoleTextArea.append(message + "\n");
//        consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            consoleTextArea.revalidate();
            String[] polePismen = pocetTextArea.getText().split(" ");
            char[] characters = buttonsTextField.getText().toCharArray();
            succes.setText("Procesuji.");
            timerTime = Integer.parseInt(timeoutTimer.getText());
            RobotWriter robotWriter = new RobotWriter();
            if (polePismen.length == characters.length) {
                workerThread = new Thread(() -> {
                    if (checkBox.isSelected()) {
                        robotWriter.disenchanterMethod(characters, polePismen);
                    } else {
                        robotWriter.afkWorkerForCrafting(characters, polePismen);
                    }
                    succes.setText("Cyklus dokončen");
                });
                workerThread.start();
            } else {
                succes.setText("Počet písmen a počet úhozů nesouhlasí. Prosím zadejte validní počet písmen a úhozů.");
            }
        } catch (Exception ex) {
            logToConsole(String.valueOf(ex));
        }
    }

    public class RobotWriter {
        Robot robot;

        public RobotWriter() {
            setRobot();
        }

        void setRobot() {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            robot.setAutoDelay(1000);
        }

        public void sendKeys() {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            tab();
        }

        public void setStringToClipBoard(String string) {
            StringSelection selection = new StringSelection(string);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }

        public void enter() {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }

        public void tab() {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        }

        private void sleeper(int howLong){
            try {
                Thread.sleep(howLong);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        private void disenchanterMethod(char[] chars, String[] pocty) {
            List<Integer> valuesOfTimes = new ArrayList<>();
            int countOftimes = 0;
            int wholeTimesOfWorkValue = 0;
            int countOfTimesInWorkload = 0;
            int whenStopAfk = 0;
            boolean notSet = true;
            sleeper(5000);
            for (String s : pocty) {
                for (int j = 0; j < Integer.parseInt(s); j++) {
                    valuesOfTimes.add(getDefinedRandomDelay());
                    wholeTimesOfWorkValue += valuesOfTimes.get(countOftimes);
                    logToConsole("wholeTimesOfWorkValue=" + wholeTimesOfWorkValue);
                    if (notSet && (wholeTimesOfWorkValue >= 300000)) {
                        whenStopAfk = countOftimes + random.nextInt(10);
                        notSet = false;
                    }
                    countOftimes++;
                }
            }
            for (int i = 0; i < pocty.length; i++) {
                for (int j = 0; j < Integer.parseInt(pocty[i]); j++) {
                    char c = chars[i];
                    if ((countOfTimesInWorkload == whenStopAfk) && countOfTimesInWorkload != 0) {
                        sleeper(valuesOfTimes.get(countOfTimesInWorkload));
                        antiAfkMoveOnSide();
                    }
                    sleeper(valuesOfTimes.get(countOfTimesInWorkload));
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    clickOnSomeButtonWithRandomTimes(c);
                    countOfTimesInWorkload++;
                }
            }
        }

        private void afkWorkerForCrafting(char[] chars, String[] pocty){
            List<Integer> valuesOfTimes = new ArrayList<>();
            for (int i = 0; i < chars.length; i++) {
                for (int j = 0; j < pocty.length; j++) {
                    for (int k = 0; k < Integer.parseInt(pocty[j]); k++) {
                        valuesOfTimes.add(getDefinedRandomDelay(300000));
                    }
                }
            }
            logToConsole("Hodnoty časů na spuštění:\n"+valuesOfTimes);
            for (int i = 0; i < valuesOfTimes.size(); i++) {
//                succes.setText(returnActualTime()+"Aktuální index="+i+"\nBudu čekat="+casNaVraceni(valuesOfTimes.get(i)));
                Instant actualTimeInstant=Instant.now();
                logToConsole(returnFormatedDateFromInstant(actualTimeInstant)+"Aktuální index="+i+"\nBudu čekat="+casNaVraceni(valuesOfTimes.get(i))+"\nNásledující klik bude proveden v:"+returnFormatedDateFromInstant(actualTimeInstant.plusMillis(valuesOfTimes.get(i))));
                sleeper(valuesOfTimes.get(i));
                logToConsole(returnFormatedDateFromInstant(Instant.now())+"Provádím klik.");
//                succes.setText(returnActualTime()+"Provádím klik.");
                clickOnSomeButtonWithRandomTimes(chars[i]);
            }
        }

        private String casNaVraceni(int milliseconds){

            // formula for conversion for
            // milliseconds to minutes.
            long minutes = (milliseconds / 1000) / 60;

            // formula for conversion for
            // milliseconds to seconds
            long seconds = (milliseconds / 1000) % 60;

            // Print the output
            return minutes + " minutes and "
                    + seconds + " seconds.";
        }

        private String returnFormatedDateFromInstant(Instant instant){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());
            return dtf.format(instant)+":";
        }

        private void clickOnSomeButtonWithRandomTimes(Character c){
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.setAutoDelay(getDefinedRandomDelayBetween());
            robot.keyPress(Character.toUpperCase(c));
            robot.setAutoDelay(getDefinedRandomDelayBetween());
            robot.keyRelease(Character.toUpperCase(c));
            robot.setAutoDelay(getDefinedRandomDelayBetween());
            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
        private int getDefinedRandomDelay() {
            return random.nextInt(1000) + timerTime;
        }
        private int getDefinedRandomDelay(Integer definedtimer) {
            return random.nextInt(definedtimer) + timerTime;
        }

        private int getDefinedRandomDelayBetween() {
            return random.nextInt(50) + 50;
        }

        private void antiAfkMoveOnSide() {
            switch (random.nextInt(4) + 1) {
                case 1 -> {
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyPress(Character.toUpperCase("a".toCharArray()[0]));
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyRelease(Character.toUpperCase("a".toCharArray()[0]));
                }
                case 2 -> {
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyPress(Character.toUpperCase("w".toCharArray()[0]));
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyRelease(Character.toUpperCase("w".toCharArray()[0]));
                }
                case 3 -> {
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyPress(Character.toUpperCase("d".toCharArray()[0]));
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyRelease(Character.toUpperCase("d".toCharArray()[0]));
                }
                case 4 -> {
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.setAutoDelay(getDefinedRandomDelayBetween());
                    robot.keyRelease(KeyEvent.VK_SPACE);
                }
            }
        }
    }
}
