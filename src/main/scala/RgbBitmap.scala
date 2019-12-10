package Rgb

import java.awt.Color
import java.awt.image.BufferedImage

import scala.swing._
import javax.swing.ImageIcon

/** Matrix of RGB pixels. Stores an image.
 *
 *  @constructor create a new RGB matrix
 *  @param values matrix of Colors
 */
class RgbBitmap(values: List[List[Color]]) {
    var matrix = values
    val width = values.size
    val height = values(0).size

    /** Second constructor using a flattened list */
    def this(values: List[Color], width: Int) {
        this(values.grouped(width).toList.transpose)
    }

    /** Display the image in a window */
    def show(tit: String)={
        val image=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

        for (y <- 0 until height; x <- 0 until width)
            image.setRGB(x, y, matrix(x)(y).getRGB())

        val mainframe = new MainFrame() {
            title = tit
            visible = true
            contents = new Label() {
                icon = new ImageIcon(image)
            }
        }
    }

    /** Check if all the nodes have the same color */
    def hasSameColor()= {
        matrix.flatten.distinct.length == 1
    }

    def shape() = {
        println("width " + width + " x height " + height)
    }

}