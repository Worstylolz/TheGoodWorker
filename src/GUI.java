import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI implements ActionListener {

    Random random = new Random();

    public Random getRandom() {
        return random;
    }

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

    public int getTimerTime() {
        return timerTime;
    }

    private Thread workerThread;

    private JTextArea consoleTextArea;
    private JScrollPane scrollPane;
    KeyboardRobot keyboardRobot;




    public GUI() {
        //vytvoření GUI
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

    /**
     * Vypíše zprávu do console v GUI.
     * @param message zpráva na vypsání.
     */
    public void logToConsole(String message) {
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
            keyboardRobot= new KeyboardRobot(this);
            if (polePismen.length == characters.length) {
                workerThread = new Thread(() -> {
                    if (checkBox.isSelected()) {
                        disenchanterMethod(characters, polePismen);
                    } else {
                        antiAfkCrafting(characters, polePismen);
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


        private void disenchanterMethod(char[] chars, String[] pocty) {

            for (int i = 0; i < chars.length; i++) {
                int aktualPoctyInt=Integer.parseInt(pocty[i]);
                int wholeTimeOfcliclicks=0;
                //nastavena hodnota mimo index z důvodu nepotřebného spuštění pokud nenastane.
                int nextAfkSession = aktualPoctyInt+1;
                logToConsole("Pouštím novou session, kde písmeno je char="+chars[i]+" a počet úhozů na tento char="+pocty[i]);
                for (int j = 0; j < aktualPoctyInt; j++) {
                    int actualClickTime=keyboardRobot.getDefinedRandomDelay();
                    Instant actualTimeInstant=Instant.now();
                    logToConsole(returnFormatedDateFromInstant(actualTimeInstant)+"Aktuální index="+i+"\nBudu čekat="+ prettyPrintTimes(actualClickTime)+"\nNásledující klik bude proveden po:"+returnFormatedDateFromInstant(actualTimeInstant.plusMillis(actualClickTime)));
                    wholeTimeOfcliclicks+=actualClickTime;
                    keyboardRobot.sleeper(actualClickTime);
                    if (j==nextAfkSession){
                        keyboardRobot.antiAfkMoveOnSide();
                    }
                    keyboardRobot.getRobot().setAutoDelay(keyboardRobot.getDefinedRandomDelayBetween());
                    logToConsole(returnFormatedDateFromInstant(Instant.now())+"Provádím klik.");
                    keyboardRobot.clickOnSomeButtonWithRandomTimes(chars[i]);
                    if (wholeTimeOfcliclicks>=240000){
                        wholeTimeOfcliclicks=0;
                        int createdNextTimeOfAfk=j + random.nextInt(10);
                        nextAfkSession=(createdNextTimeOfAfk)<aktualPoctyInt?createdNextTimeOfAfk:aktualPoctyInt-1;
                    }
                }
            }
        }

        /**Metoda, který nám dle času v inputu ms spočte dle Počet*ms celkový čas craftení všech itemů. Následně vždy jednou mezi 22min+náhodný čas z 300 sekund provede antiAfkMetodu.
         * indexy z chars jsou navázané na indexy z počtu stejně jako u disenchant metody. index pole pocty= index pole chars. Tato skutečnost vždy musí být dodržená.
         * @param chars pole tlačítek, na které má kliknout.
         * @param pocty pole počtů, kolikrát má kliknout
         */
        public void antiAfkCrafting(char[] chars, String[] pocty){
            for (int i = 0; i < chars.length; i++) {
                List<Long> timesToWait=new ArrayList<>();
                int aktualPoctyInt = Integer.parseInt(pocty[i]);
                long wholeTimeFromPocty=aktualPoctyInt*timerTime;
                long timesToWaitRaw=0;
                while (wholeTimeFromPocty>timesToWaitRaw){
                    long increasedTime=(1320000+random.nextInt(300000));
                    timesToWaitRaw+=increasedTime;
                    timesToWait.add(increasedTime);
                }
                Instant timeForVypis=Instant.now();
                for (long casKliku :
                        timesToWait) {
                    timeForVypis=timeForVypis.plusMillis(Math.toIntExact(casKliku));
                    logToConsole(prettyPrintTimes(Math.toIntExact(casKliku))+"\nNásledující klik bude proveden po:"+returnFormatedDateFromInstant(timeForVypis));
                }
                logToConsole("Celkový počet indexů je:"+timesToWait.size());
                logToConsole("Celý proces bude trvat:"+ prettyPrintTimes(Math.toIntExact(timesToWaitRaw))+"\nBude dookončen:"+returnFormatedDateFromInstant(Instant.now().plusMillis(timesToWaitRaw))+"\nCelkový počet indexů je:"+timesToWait.size());
                for (int t = 0; t < timesToWait.size(); t++) {
                    Instant actualTimeInstant=Instant.now();
                    logToConsole(returnFormatedDateFromInstant(actualTimeInstant)+"Aktuální index="+t+"/"+timesToWait.size()+"\nBudu čekat="+ prettyPrintTimes(Math.toIntExact(timesToWait.get(t)))+"\nNásledující klik bude proveden po:"+returnFormatedDateFromInstant(actualTimeInstant.plusMillis(Math.toIntExact(timesToWait.get(t)))));
                    keyboardRobot.sleeper(Math.toIntExact(timesToWait.get(t)));
                    keyboardRobot.antiAfkMoveOnSide();
                    logToConsole(returnFormatedDateFromInstant(Instant.now())+"Provádím klik.");
                    keyboardRobot.getRobot().setAutoDelay(keyboardRobot.getDefinedRandomDelayBetween());
                    keyboardRobot.clickOnSomeButtonWithRandomTimes(chars[i]);
                    }
                }
            logToConsole("Proces je dokončen!");
            }


        /**
         *
         * @param milliseconds int milisekund.
         * @return String s výpisem času.
         */
        private String prettyPrintTimes(int milliseconds){

            // formula for conversion for
            // milliseconds to minutes.
            long minutes = (milliseconds / 1000) / 60;

            // formula for conversion for
            // milliseconds to seconds
            long seconds = (milliseconds / 1000) % 60;

            long millisecondsAfterCalculate = milliseconds % 1000;

            // Print the output
            return minutes + " minutes "
                    + seconds + " seconds "
                    + millisecondsAfterCalculate + " milliseconds.";
        }

        private String returnFormatedDateFromInstant(Instant instant){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSSS").withZone(ZoneId.systemDefault());
            return dtf.format(instant)+":";
        }
    }
