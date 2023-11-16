import java.awt.datatransfer.StringSelection
import java.awt.Toolkit

def setToClipboard(text){
    StringSelection stringSelection = new StringSelection(text)
    def clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
    clipboard.setContents(stringSelection, null)
}