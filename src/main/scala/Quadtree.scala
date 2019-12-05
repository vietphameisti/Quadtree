import java.awt.Color
import java.awt.image.BufferedImage
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import Rgb.RgbBitmap

/** Node of a quadtree. Each node represents a quadrant.
 *
 *  @constructor create a quadtree starting from an image
 *  @param img the imgto start from
 */
class Quadtree(val img: RgbBitmap) {
    var color: Color = new Color(0,0,0)
    var children: ListBuffer[Quadtree] = new ListBuffer[Quadtree]()

    println("Node created " + img.width + " " + img.height)

    if(img.hasSameColor || (img.height * img.width) < 1) {
        color = img.matrix(0)(0)
    } else {
        // split in 4 sub-images
        val subImgs = splitImg(img)
        subImgs.foreach(x => println(x.size + " " +  x(0).size))

        // add 4 children to the list
        subImgs.foreach( x => {
            children += new Quadtree(new RgbBitmap(x))
        })

        // color = average(children.color)
    }

    /** Is this node a leaf? */
    def isLeaf()= children.isEmpty

    /** Print name of the Node and subnodes in a tree */
    def show(prefix: String) {
        print(prefix + "_ ")
        print(color)
        print('\n')
        children.foreach(_.show(prefix + "  |"))
    }

    /** Split an image into 4 sub images */
    private def splitImg(img: RgbBitmap): List[List[List[Color]]] = {
        img.matrix.map(_.grouped((img.width+1)/2).toList).grouped((img.width+1)/2).map(_.transpose).reduce(_ ++ _)
    }

}