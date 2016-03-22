package rlengine

/**
 * Created by stewsters on 3/17/16.
 */

import rlengine.ui.AsciiSelectableTerminalButton
import rlengine.ui.AsciiTerminal
import rlengine.ui.AsciiTerminalButton
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JOptionPane

class AsciiUISample(tilesetFile: String, characterWidth: Int, characterHeight: Int) {

    private val terminal: AsciiTerminal

    init {
        terminal = AsciiTerminal(TITLE, Dimension(WINDOW_WIDTH, WINDOW_HEIGHT), tilesetFile, characterWidth, characterHeight)

        val asciiPanel = terminal.asciiPanel
        val rand = Random()

        for (i in 0..15) {
            for (j in 0..9) {
                asciiPanel.write(i, j, rand.nextInt(256).toChar(), Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)))
            }
        }

        val button1 = AsciiTerminalButton(asciiPanel, "Click on me!", 0, 12, Color.GREEN, Color.ORANGE)
        button1.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                JOptionPane.showMessageDialog(terminal, "Thanks!")
            }
        })
        asciiPanel.add(button1)

        val button2 = AsciiSelectableTerminalButton(asciiPanel, "Select me!", 0, 14, Color.GREEN, Color.ORANGE, Color.MAGENTA)
        button2.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val astb = e!!.component as AsciiSelectableTerminalButton
                astb.isSelect = !astb.isSelect
            }
        })
        asciiPanel.add(button2)

        terminal.repaint()
    }

    companion object {

        val TITLE = "ASCII Demo"
        val WINDOW_WIDTH = 16
        val WINDOW_HEIGHT = 16

        @JvmStatic fun main(args: Array<String>) {
            val choices = arrayOf("Anikki [8x8]", "Yoshis island [9x12]", "Vidumec [15x15]", "Wanderlust [16x16]", "Curses square [24x24]")

            val input = JOptionPane.showInputDialog(null, "Choose tilset...",
                    "Choice of tileset", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]) as String

            if (input == "Anikki [8x8]") {
                AsciiUISample("src/assets/Anikki_square_8x8.png", 8, 8)
            } else if (input == "Yoshis island [9x12]") {
                AsciiUISample("src/assets/Yoshis_island.png", 9, 12)
            } else if (input == "Vidumec [15x15]") {
                AsciiUISample("src/assets/Vidumec_15x15.png", 15, 15)
            } else if (input == "Wanderlust [16x16]") {
                AsciiUISample("src/assets/wanderlust.png", 16, 16)
            } else {
                AsciiUISample("src/assets/Curses_square_24.png", 24, 24)
            }
        }
    }
}