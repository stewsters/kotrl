package rlengine.ui


import java.awt.Color
import java.awt.image.RGBImageFilter

/**
 * Change the background color of all character of the tileset to black.

 * @author Julien MAITRE
 */
class AsciiBackgroundFilter(private val m_BackgroundColor: Color) : RGBImageFilter() {

    init {
        // The transformation of colors don't depend of the location of points in the image.
        canFilterIndexColorModel = true
    }

    override fun filterRGB(x: Int, y: Int, rgb: Int): Int {
        val red = rgb and 0x00FF0000
        val green = rgb and 0x0000FF00
        val blue = rgb and 0x000000FF
        val alpha = rgb and 0xFF000000.toInt()

        if (red shr 4 * 4 == m_BackgroundColor.red &&
                green shr 2 * 4 == m_BackgroundColor.green &&
                blue shr 0 * 4 == m_BackgroundColor.blue) {
            return alpha shl 6 * 4
        } else {
            return rgb
        }
    }
}