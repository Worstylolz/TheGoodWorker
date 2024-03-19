import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class KeyboardRobot {
    Robot robot;

    public Robot getRobot() {
        return robot;
    }

    GUI gui;
    public KeyboardRobot(GUI gui) {
        this.gui=gui;
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

    public void sleeper(int howLong){
        try {
            Thread.sleep(howLong);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kliknutí na tlačítko s rozdílnými časy, aby čas nebyl stejný a nebylo tak lehce rozeznatelné, že se jedná o program.
     * @param c tlačítko, na které budu chtít kliknout.
     */
    public void clickOnSomeButtonWithRandomTimes(Character c){
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

    /**
     *
     * @return náhodný čas + ms time nastavený v input ms
     */
    public int getDefinedRandomDelay() {
        return gui.getRandom().nextInt(1000) + gui.getTimerTime();
    }
    private int getDefinedRandomDelay(Integer definedtimer) {
        return gui.getRandom().nextInt(definedtimer) + gui.getTimerTime();
    }

    /**
     *
     * @return náhodný čas na robot autoDelay.
     */

    public int getDefinedRandomDelayBetween() {
        return gui.getRandom().nextInt(50) + 50;
    }

    /**
     * Metoda provede náhodné pohnutí do stran, nebo stihnutí mezerníku. Tím způsobí, že charakter vypadne ze stavu AFK.
     */
    public void antiAfkMoveOnSide() {
        gui.logToConsole("Provádím antiAFK session");
        switch (gui.getRandom().nextInt(4) + 1) {
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
        sleeper(5000);
    }
}