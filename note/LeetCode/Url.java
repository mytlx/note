import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Scanner;

/**
 * @author TLX
 * @date 2019.7.20
 * @time 12:17
 */
public class Url {

    public static void main(String[] args) {

        // Scanner sc = new Scanner(System.in);
        // String s = sc.nextLine();

        String s = getClipboardString();
        System.out.println(s);

        assert s != null;
        char[] a = s.toCharArray();

        StringBuilder sb = new StringBuilder();

        sb.append("./");

        for (char anA : a) {
            if (anA != ' ')
                sb.append(anA);
            else {
                sb.append("%20");
            }
        }

        System.out.println(sb.toString());
        setClipboardString(sb.toString());
    }


    public static void setClipboardString(String text) {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Transferable trans = new StringSelection(text);

        clipboard.setContents(trans, null);
    }



    public static String getClipboardString() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();


        Transferable trans = clipboard.getContents(null);

        if (trans != null) {

            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {

                    return (String) trans.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
