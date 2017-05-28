package kotrl.ui


import java.awt.Color
import java.awt.event.MouseEvent

/**
 * A button selectable.

 * @author julien MAITRE
 */
class AsciiSelectableTerminalButton : AsciiTerminalButton {
    var isSelect = false
        set(select) {
            field = select
            changeColor()
        }
    private var mouseSelectColor: Color

    constructor(asciiPanel: AsciiPanel, label: String, x: Int, y: Int, mouseDefaultColor: Color, mouseEnteredColor: Color, mouseSelectColor: Color) : super(asciiPanel, label, x, y, mouseDefaultColor, mouseEnteredColor) {
        this.mouseSelectColor = mouseSelectColor
    }

    constructor(asciiPanel: AsciiPanel, label: String, x: Int, y: Int, mouseDefaultColor: Color, mouseEnteredColor: Color, mouseSelectColor: Color, mouseBackgroundColor: Color) : super(asciiPanel, label, x, y, mouseDefaultColor, mouseEnteredColor, mouseBackgroundColor) {
        this.mouseSelectColor = mouseSelectColor
    }

    override fun mouseExited(e: MouseEvent) {
        changeColor()
    }

    private fun changeColor() {
        if (isSelect) {
            mouseCurrentColor = mouseSelectColor
        } else {
            mouseCurrentColor = mouseDefaultColor
        }
        this.repaint()
    }
}