import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI implements ActionListener {

    Random random = new Random();
    private JLabel succes;
    private JFrame frame;
    private JPanel panel;
    private JLabel buttonsLabel;
    private JLabel pocetLabel;
    private JButton button;
    private JTextField buttonsTextField;
    private JTextField pocetTextArea;
    int clicked = 0;

    public GUI() {
        frame = new JFrame();
        panel = new JPanel();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setLocationRelativeTo(null);

        panel.setLayout(null);

        frame.setTitle("The good worker");
//            frame.pack();

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        buttonsLabel = new JLabel("Buttony na které klikám.Zadávat bez mezery '1245'");
        buttonsLabel.setBounds(10, 20, 80, 25);
        panel.add(buttonsLabel);

        buttonsTextField = new JTextField(20);
        buttonsTextField.setBounds(100, 20, 165, 25);
        panel.add(buttonsTextField);

        pocetLabel = new JLabel("Počet kliků. Zadávat s mezerou '20 40'");
        pocetLabel.setBounds(10, 50, 80, 25);
        panel.add(pocetLabel);

        pocetTextArea = new JTextField(20);
        pocetTextArea.setBounds(100, 50, 165, 25);
        panel.add(pocetTextArea);

        button = new JButton("StartBoTTa");
        button.setBounds(10, 80, 80, 25);
        button.addActionListener(this);
        panel.add(button);

        succes = new JLabel("");
        succes.setBounds(10, 110, 300, 25);;
        panel.add(succes);

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] polePismen = pocetTextArea.getText().split(" ");
        char[] characters = buttonsTextField.getText().toCharArray();
        succes.setText("Procesuji.");
        if (polePismen.length==characters.length) {
            RobotWriter robotWriter = new RobotWriter();
            robotWriter.processButtonsWithTimes(characters,polePismen);
            succes.setText("Cyklus dokončen");
        }else {
            succes.setText("Počet písmen a počet úhozů nesouhlasí. Prosím zadejte validní počet písmen a úhozů.");
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

        private void processButtonsWithTimes(char[] chars,String[] pocty) {
            List<Integer> valuesOfTimes= new ArrayList<>();
            int countOftimes=0;
            int wholeTimesOfWorkValue=0;
            int countOfTimesInWorkload=0;
            int whenStopAfk=0;
            boolean notSet=true;
            for (int i = 0; i < pocty.length; i++) {
                for (int j = 0; j < Integer.parseInt(pocty[i]); j++) {
                    valuesOfTimes.add(getDefinedRandomDelay());
                    wholeTimesOfWorkValue+=valuesOfTimes.get(countOftimes);
                    if (notSet&&(wholeTimesOfWorkValue<=300000)){
                        whenStopAfk=countOftimes+random.nextInt(10);
                        notSet=false;
                    }
                            countOftimes++;
                }
            }
            for (int i = 0; i < pocty.length; i++) {
                for (int j = 0; j < Integer.parseInt(pocty[i]); j++) {
                    char c = chars[i];
                    if (countOfTimesInWorkload==whenStopAfk){
                        switch (random.nextInt(3)+1){
                            case 1:
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyPress(Character.toUpperCase("a".toCharArray()[0]));
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyRelease(Character.toUpperCase("a".toCharArray()[0]));
                                break;
                                case 2:
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyPress(Character.toUpperCase("w".toCharArray()[0]));
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyRelease(Character.toUpperCase("w".toCharArray()[0]));
                                break;
                                case 3:
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyPress(Character.toUpperCase("d".toCharArray()[0]));
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyRelease(Character.toUpperCase("d".toCharArray()[0]));
                                break;
                                case 4:
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyPress(KeyEvent.VK_SPACE);
                                robot.delay(getDefinedRandomDelayBetween());
                                robot.keyRelease(KeyEvent.VK_SPACE);
                                break;
                        }
                    }
                    try {
                        Thread.sleep(valuesOfTimes.get(countOfTimesInWorkload));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    robot.delay(getDefinedRandomDelayBetween());
                    if (Character.isUpperCase(c)) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                    }
                    robot.delay(getDefinedRandomDelayBetween());
                    robot.keyPress(Character.toUpperCase(c));
                    robot.delay(getDefinedRandomDelayBetween());
                    robot.keyRelease(Character.toUpperCase(c));
                    robot.delay(getDefinedRandomDelayBetween());
                    if (Character.isUpperCase(c)) {
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    }
                }
                countOfTimesInWorkload++;
            }
        }
        private int getDefinedRandomDelay(){
            return random.nextInt(2000)+5000;
        }

        private int getDefinedRandomDelayBetween(){
            return random.nextInt(50)+50;
        }
    }
}
