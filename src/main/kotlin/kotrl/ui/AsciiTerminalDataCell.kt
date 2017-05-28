package kotrl.ui


import java.awt.Color

/**
 * Representation of data of one character in the AsciiPanel

 * @author Julien MAITRE
 */
class AsciiTerminalDataCell {
    var data: Char = ' '
    var dataColor: Color
    var backgroundColor: Color

    constructor() {
        this.data = 0.toChar()
        this.dataColor = Color.WHITE
        this.backgroundColor = Color.BLACK
    }

    constructor(data: Char, dataColor: Color, backgroundColor: Color) {
        this.data = data
        this.dataColor = dataColor
        this.backgroundColor = backgroundColor
    }
}