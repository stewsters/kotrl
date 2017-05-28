package kotrl.ui

import java.awt.Dimension
import javax.swing.JFrame

/**
 * Create a JFrame with an AsciiPanel.

 * @author Julien MAITRE
 */
class AsciiTerminal(title: String, dimension: Dimension, tilesetFile: String, characterWidth: Int, characterHeight: Int) : JFrame() {
    val asciiPanel: AsciiPanel

    init {
        asciiPanel = AsciiPanel(dimension, tilesetFile, characterWidth, characterHeight)

        this.title = title
        this.isResizable = false
        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.contentPane.add(asciiPanel)
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}