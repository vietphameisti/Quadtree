import java.awt.Color
import java.awt.image.BufferedImage
import scala.collection.mutable.ListBuffer

import Rgb.RgbBitmap

/** Node of a quadtree. Each node represents a quadrant.
 *
 *  @constructor create a quadtree starting from an image
 *  @param img the imgto start from
 */
class Quadtree(val img: RgbBitmap) {
    var color: Color = new Color(0,0,0)
    var width: Int = img.width
    var height: Int = img.height
    var children: ListBuffer[Quadtree] = new ListBuffer[Quadtree]()

    /**********************************************
     *             CONSTRUCTOR                    *
     **********************************************/

    /* If the image has the same color stop */
    if(img.hasSameColor) {
        color = img.matrix(0)(0)
    } else {
        /* Else divide in 4 */
        val subImgs = splitImg(img)
        subImgs.foreach( x => {
            children += new Quadtree(x)  // Add children
        })

        /* Take the mean color of the children */
        val colorsum = children.map(_.color).map(colorToList).reduceLeft((x,y) => sumColors(x,y))
        val colormean = colorsum.map(_/children.size)
        color = new Color(colormean(0), colormean(1), colormean(2))
    }

    /** Sum 2 colors component by component */
    private def sumColors(x: List[Int],y: List[Int]) = x.zip(y).map(a => a._1 + a._2)

    /** Convert Color to list of Integers */
    private def colorToList(x: Color): List[Int] = List(x.getRed(), x.getGreen(), x.getBlue())

    /** Split an image into 4 sub images */
    private def splitImg(img: RgbBitmap): List[RgbBitmap] = {
        img.matrix.map(_.grouped((img.height+1)/2).toList).grouped((img.width+1)/2).map(_.transpose).reduce(_ ++ _).map(new RgbBitmap(_))
    }

    /**********************************************
     *               Functions                    *
     **********************************************/

    /** Is this node a leaf? */
    def isLeaf()= children.isEmpty

    /** Print name of the Node and subnodes in a tree */
    def show(prefix: String):Unit= {
        println(prefix + "_ " + width + "x" + height)
        children.foreach(_.show(prefix + "  |"))
    }

    /** alias for the toBitmap function */
    def compress(maxDepth: Int) = toBitmap(maxDepth)

    /** convert quadtree to image */
    //@annotation.tailrec
    def toBitmap(depth: Int = -1): RgbBitmap = {

        // If it's a leaf or we reached the wanted detail, return the color
        if(this.isLeaf  || depth == 0){
            val mat = List.fill(width*height)(color).grouped(width).toList.transpose
            return new RgbBitmap(mat)
        }

        // Else...
        else {
            // recursively get the children's bitmap
            val submaps: List[List[List[Color]]] = children.map(_.toBitmap(depth - 1).matrix).toList

            /* Case 4 children
             *  +---+---+
             *  | 0 | 1 |
             *  +---+---+
             *  | 2 | 3 |
             *  +---+---+
             */
            if(submaps.size > 2)
            {
                val img = (submaps(0), submaps(1)).zipped.map(_ ++ _) ::: (submaps(2), submaps(3)).zipped.map(_ ++ _)
                return new RgbBitmap(img);
            }
            // Case 2 Children
            else {

                /* Vertical
                 *  +---+
                 *  | 0 |
                 *  +---+
                 *  | 1 |
                 *  +---+
                 */
                if(height == 1) {
                    val img = submaps(0) ::: submaps(1)
                    return new RgbBitmap(img);
                }

                /* Horizontal
                 *  +---+---+
                 *  | 0 | 1 |
                 *  +---+---+
                 */
                else if(width==1) {
                    val img = (submaps(0), submaps(1)).zipped.map(_ ++ _)
                    return new RgbBitmap(img);
                }

                else {
                    return new RgbBitmap(List(List(color)))
                }
            }

        }

    }

}