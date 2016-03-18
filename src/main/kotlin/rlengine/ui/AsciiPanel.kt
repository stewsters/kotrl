package rlengine.ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.FilteredImageSource
import java.awt.image.LookupOp
import java.awt.image.ShortLookupTable
import java.io.File
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger
import javax.imageio.ImageIO
import javax.swing.JPanel

/**
 * JPanel with a ASCII render system

 * @author julien MAITRE
 */
class AsciiPanel(cellDimensions: Dimension, tilesetFile: String, characterWidth: Int, characterHeight: Int) : JPanel() {

    private var character: Array<BufferedImage>
    var defaultCharacterColor: Color
    var defaultCharacterBackgroundColor: Color
    val characterSize: Dimension
    val cellDimensions: Dimension

    private val terminal: Array<Array<AsciiTerminalDataCell>>
    private val oldTerminal: Array<Array<AsciiTerminalDataCell>>
    private var image: Image? = null
    private var graphics: Graphics? = null
    override fun getGraphics(): Graphics? {
        return super.getGraphics()
    }

    init {
        this.cellDimensions = cellDimensions
        this.characterSize = Dimension(characterWidth, characterHeight)
        this.defaultCharacterColor = Color.WHITE
        this.defaultCharacterBackgroundColor = Color.BLACK

        terminal = Array<Array<AsciiTerminalDataCell>>(cellDimensions.height) { Array<AsciiTerminalDataCell>(cellDimensions.width, { AsciiTerminalDataCell() }) }
        oldTerminal = Array<Array<AsciiTerminalDataCell>>(cellDimensions.height) { Array<AsciiTerminalDataCell>(cellDimensions.width, { AsciiTerminalDataCell() }) }
        //        for (i in 0..size.height - 1) {
        //            for (j in 0..size.width - 1) {
        //                val tdc =
        //                terminal[i][j] = tdc
        //                oldTerminal[i][j] = tdc
        //            }
        //        }

        this.preferredSize = Dimension(cellDimensions.width * characterSize.width, cellDimensions.height * characterSize.height)
        character = Array<BufferedImage>(256, { BufferedImage(characterSize.width, characterSize.height, BufferedImage.TYPE_INT_ARGB) })

        try {

            val tilesets = ImageIO.read(File(tilesetFile))

            // Recuperation of the background color
            val imageBackgroundColor = tilesets.getSubimage(0, 0, 1, 1)
            val color = imageBackgroundColor.getRGB(0, 0)
            val m_characterBackgroundColor = Color.getColor(null, color)

            // Modification of characters background
            val characterBackgroundColorModified = createImage(FilteredImageSource(tilesets.source, AsciiBackgroundFilter(m_characterBackgroundColor)))

            // Creation of tileset with a modification of the background color
            val tilesetsModified = BufferedImage(tilesets.width, tilesets.height, BufferedImage.TYPE_INT_ARGB)
            val graphicsTilesetsModified = tilesetsModified.graphics
            graphicsTilesetsModified.color = Color.BLACK
            graphicsTilesetsModified.fillRect(0, 0, tilesetsModified.width, tilesetsModified.height)
            // Draw in a BufferedImage for characters recuperation
            graphicsTilesetsModified.drawImage(characterBackgroundColorModified, 0, 0, this)


            for (i in 0..255) {
                val x = i % 16 * characterSize.width
                val y = i / 16 * characterSize.height
                character[i].graphics.drawImage(tilesetsModified, 0, 0, characterSize.width, characterSize.height, x, y, x + characterSize.width, y + characterSize.height, this)
            }
        } catch (ex: IOException) {
            Logger.getLogger(AsciiTerminal::class.java.name).log(Level.SEVERE, null, ex)
        }

        this.layout = null
    }

    fun write(positionX: Int, positionY: Int, character: Char, characterColor: Color) {
        val tdc = AsciiTerminalDataCell()
        tdc.data = character
        tdc.dataColor = characterColor
        tdc.backgroundColor = defaultCharacterBackgroundColor
        this.write(positionX, positionY, tdc)
    }

    fun write(positionX: Int, positionY: Int, character: Char, characterColor: Color, characterBackgroundColor: Color) {
        val tdc = AsciiTerminalDataCell()
        tdc.data = character
        tdc.dataColor = characterColor
        tdc.backgroundColor = characterBackgroundColor
        this.write(positionX, positionY, tdc)
    }

    fun write(positionX: Int, positionY: Int, character: AsciiTerminalDataCell) {
        if (positionX < 0 || positionX > cellDimensions.width - 1) {
            throw IllegalArgumentException("X position between [0 and " + cellDimensions.width + "]")
        }
        if (positionY < 0 || positionY > cellDimensions.height - 1) {
            throw IllegalArgumentException("Y position between [0 and " + cellDimensions.height + "]")
        }

        terminal[positionY][positionX] = character
    }

    @JvmOverloads fun writeString(positionX: Int, positionY: Int, string: String, characterColor: Color, characterBackgroundColor: Color = defaultCharacterBackgroundColor) {
        var posX = positionX
        for (c in string.toCharArray()) {
            if (posX < 0 || posX > cellDimensions.width - 1) {
                throw IllegalArgumentException("X position between [0 and " + cellDimensions.width + "]")
            }
            if (positionY < 0 || positionY > cellDimensions.height - 1) {
                throw IllegalArgumentException("Y position between [0 and " + cellDimensions.height + "]")
            }

            val tdc = AsciiTerminalDataCell()
            tdc.data = c
            tdc.dataColor = characterColor
            tdc.backgroundColor = characterBackgroundColor
            write(posX, positionY, tdc)
            posX++
        }
    }

    fun read(positionX: Int, positionY: Int): AsciiTerminalDataCell? {
        return this.oldTerminal[positionX][positionY]
    }

    @JvmOverloads fun clear(x: Int = 0, y: Int = 0, width: Int = cellDimensions.width - 1, height: Int = cellDimensions.height - 1) {
        if (x < 0 || x > cellDimensions.width - 1) {
            throw IllegalArgumentException("X position between [0 and " + (cellDimensions.width - 1) + "]")
        }
        if (y < 0 || y > cellDimensions.height - 1) {
            throw IllegalArgumentException("Y position between [0 and " + (cellDimensions.height - 1) + "]")
        }
        if (width < 1) {
            throw IllegalArgumentException("Width under 0")
        }
        if (height < 1) {
            throw IllegalArgumentException("Height under 0")
        }
        if (width + x > cellDimensions.width - 1 || height + y > cellDimensions.height - 1) {
            throw IllegalArgumentException("Clear over the terminal")
        }
        for (i in x..cellDimensions.height - 1) {
            for (j in x..cellDimensions.width - 1) {
                val tdc = AsciiTerminalDataCell()
                terminal[i][j] = tdc
                oldTerminal[i][j] = tdc
            }
        }
        if (graphics != null) {
            graphics!!.color = defaultCharacterBackgroundColor
            graphics!!.fillRect(x * characterSize.width, y * characterSize.height, (x + width) * characterSize.width, (y + height) * characterSize.height)
        }
    }

    override fun paint(g: Graphics) {
        if (image == null) {
            image = this.createImage(this.width, this.height)
            graphics = image!!.graphics
            graphics!!.color = defaultCharacterBackgroundColor
            graphics!!.fillRect(0, 0, this.width, this.height)
        }

        for (component in components) {
            component.paint(graphics)
        }

        for (i in 0..(cellDimensions.height - 1)) {
            for (j in 0..(cellDimensions.width - 1)) {
                if (terminal[i][j].data == oldTerminal[i][j].data &&
                        terminal[i][j].dataColor == oldTerminal[i][j].dataColor &&
                        terminal[i][j].backgroundColor == oldTerminal[i][j].backgroundColor) {
                    continue
                }

                val lookupOp = setColorCharacter(terminal[i][j].backgroundColor, terminal[i][j].dataColor)
                graphics!!.drawImage(lookupOp.filter(character!![terminal[i][j].data.toInt()], null), j * characterSize.width, i * characterSize.height, this)

                oldTerminal[i][j].data = terminal[i][j].data
                oldTerminal[i][j].dataColor = terminal[i][j].dataColor
                oldTerminal[i][j].backgroundColor = terminal[i][j].backgroundColor
            }
        }

        g!!.drawImage(image, 0, 0, this)
    }

    private fun setColorCharacter(bgColor: Color, fgColor: Color): LookupOp {
        val red = ShortArray(256)
        val green = ShortArray(256)
        val blue = ShortArray(256)
        val alpha = ShortArray(256)

        // Recuperation of compound colors of foreground character color
        val dcr = fgColor.red.toShort()
        val dcg = fgColor.green.toShort()
        val dcb = fgColor.blue.toShort()

        // Recuperation of compound colors of background character color
        val bgr = bgColor.red.toShort()
        val bgg = bgColor.green.toShort()
        val bgb = bgColor.blue.toShort()

        for (j in 0..255) {
            // if is foreground color
            if (j.toInt() != 0) {

                /**
                 * Calculation of j*255/dcr .
                 * Cross product
                 * dcr = 180     255
                 * j =  ?       X
                 * Distribute the requested color [0 to 255] on the character color [0 to X]
                 */
                // Red
                if (dcr.toInt() != 0) {
                    red[j] = (j * dcr / 255).toShort()
                } else {
                    red[j] = 0
                }

                // green
                if (dcg.toInt() != 0) {
                    green[j] = (j * dcg / 255).toShort()
                } else {
                    green[j] = 0
                }

                // Blue
                if (dcb.toInt() != 0) {
                    blue[j] = (j * dcb / 255).toShort()
                } else {
                    blue[j] = 0
                }

                // Alpha
                alpha[j] = 255
            } else {
                red[j] = bgr
                green[j] = bgg
                blue[j] = bgb
                alpha[j] = 255
            }// else is background color
        }

        val data = arrayOf(red, green, blue, alpha)
        val lookupTable = ShortLookupTable(0, data)
        val lookupOp = LookupOp(lookupTable, null)
        return lookupOp
    }
}