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

    /** Check if all the nodes have the same color */
    def hasSameColor()= {
        matrix.flatten.distinct.length == 1
    }

    /*************************************
     *         MANIPULATION              *
     *************************************/

    /** Mirroring Left-Right */
    def mirrorLR()=new Rgb.RgbBitmap(matrix.reverse) // reverse rows

    /** Mirroring Top-Bottom */
    def mirrorTB()=new Rgb.RgbBitmap(matrix.map(_.reverse)) // revers each column

    /** Invert */
    def invert()=new Rgb.RgbBitmap(matrix.flatten.map(invertColor).grouped(height).toList) // invert each color

    private def invertColor(x:Color): Color={
        new Color(255-x.getRed(), 255-x.getGreen(), 255-x.getBlue())
    }

    /** Rotate */
    def rotate() = new Rgb.RgbBitmap(matrix.transpose.map(_.reverse)) // transpose and invert rows

    /*************************************
     *         DISPLAYING                *
     *************************************/

    /** Display the image in a separate window */
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

    /** Display the image as a BufferedImage */
    def showImageViewer(rgbImage:RgbBitmap):BufferedImage={
        val image=new BufferedImage(rgbImage.width,rgbImage.height, BufferedImage.TYPE_3BYTE_BGR)
        for (y <- 0 until height; x <- 0 until width)
          image.setRGB(x, y, rgbImage.matrix(x)(y).getRGB())

        image
    }
}