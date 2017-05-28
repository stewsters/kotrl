package kotrl.ui

import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JComponent

/**
 * A simple button

 * @author julien MAITRE
 */
open class AsciiTerminalButton : JComponent, MouseListener {
    private val asciiPanel: AsciiPanel
    private var name: String
    private var x: Int
    private var y: Int
    protected var mouseCurrentColor: Color
    protected var mouseDefaultColor: Color
    protected var mouseEnteredColor: Color
    protected var mouseBackgroundColor: Color? = null

    constructor(asciiPanel: AsciiPanel, name: String, x: Int, y: Int, mouseDefaultColor: Color, mouseEnteredColor: Color) {
        this.asciiPanel = asciiPanel
        this.name = name
        this.x = x
        this.y = y
        this.mouseCurrentColor = mouseDefaultColor
        this.mouseDefaultColor = mouseDefaultColor
        this.mouseEnteredColor = mouseEnteredColor
        setBounds(x * asciiPanel.characterSize.width, y * asciiPanel.characterSize.height, name.length * asciiPanel.characterSize.width, asciiPanel.characterSize.height)
        this.addMouseListener(this)
    }

    constructor(asciiPanel: AsciiPanel, name: String, x: Int, y: Int, mouseDefaultColor: Color, mouseEnteredColor: Color, mouseBackgroundColor: Color) {
        this.asciiPanel = asciiPanel
        this.name = name
        this.x = x
        this.y = y
        this.mouseCurrentColor = mouseDefaultColor
        this.mouseDefaultColor = mouseDefaultColor
        this.mouseEnteredColor = mouseEnteredColor
        this.mouseBackgroundColor = mouseBackgroundColor
        setBounds(x * asciiPanel.characterSize.width, y * asciiPanel.characterSize.height, name.length * asciiPanel.characterSize.width, asciiPanel.characterSize.height)
        this.addMouseListener(this)
    }

    override fun mouseClicked(e: MouseEvent) {
        // nothing
    }

    override fun mousePressed(e: MouseEvent) {
        // nothing
    }

    override fun mouseReleased(e: MouseEvent) {
        // nothing
    }

    override fun mouseEntered(e: MouseEvent) {
        mouseCurrentColor = mouseEnteredColor
        asciiPanel.repaint()
    }

    override fun mouseExited(e: MouseEvent) {
        mouseCurrentColor = mouseDefaultColor
        asciiPanel.repaint()
    }

    override fun paint(g: Graphics?) {
        asciiPanel.writeString(x, y, name, mouseCurrentColor, if (mouseBackgroundColor != null) mouseBackgroundColor!! else asciiPanel.defaultCharacterBackgroundColor)
    }

    override fun getX(): Int {
        return super.getX()
    }

    override fun getY(): Int {
        return super.getY()
    }

    override fun getName(): String? {
        return super.getName()
    }

    override fun setName(name: String?) {
        super.setName(name)
    }
}
