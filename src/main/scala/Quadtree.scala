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

    //println("Node created " + img.width + " " + img.height)

    if(img.hasSameColor || (img.height * img.width) < 1) {
        color = img.matrix(0)(0)
    } else {
        // split into 4 sub-images
        val subImgs = splitImg(img)

        // add children to the list
        subImgs.foreach( x => {
            children += new Quadtree(x)
        })

        if (subImgs.size != 2 && subImgs.size != 4)
            System.exit(-1*subImgs.size)

        // Take the mean color of the children
        val sum = children.map(_.color).map(x => List(x.getRed(), x.getGreen(), x.getBlue())).reduceLeft((x,y) => {x.zip(y).map(a => a._1 + a._2)})
        color = new Color(sum(0)/children.size, sum(1)/children.size, sum(2)/children.size)
    }

    /** Is this node a leaf? */
    def isLeaf()= children.isEmpty

    /** Print name of the Node and subnodes in a tree */
    def show(prefix: String) {
        println(prefix + "_ " + width + "x" + height)
        children.foreach(_.show(prefix + "  |"))
    }

    /** Split an image into 4 sub images */
    private def splitImg(img: RgbBitmap): List[RgbBitmap] = {
        img.matrix.map(_.grouped((img.height+1)/2).toList).grouped((img.width+1)/2).map(_.transpose).reduce(_ ++ _).map(new RgbBitmap(_))
    }

    /** convert quadtree to image */
    //@annotation.tailrec
    final def toBitmap(depth: Int): RgbBitmap = {

        // If it's a leaf or we reached the wanted detail, return the color
        if(this.isLeaf  || depth <= 0){
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