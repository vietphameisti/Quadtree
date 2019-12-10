import java.awt.Color

/** Contains the main function. */
object Launcher {
    def main(args: Array[String]): Unit = {

      // load image and display it
      val img = Rgb.BitmapLoader.load("src/main/Images/sign_1.ppm").get
      img.show("Original Image")

      // build quadtree from image
      val quadtree = new Quadtree(img)
      quadtree.show("")

      // build image from quadtree
      quadtree.toBitmap(1000).show("Reconstructed From Quadtree")

      // build compressed image from quadtree
      quadtree.toBitmap(5).show("Compressed")

      // save image
      // TODO

      // transpose image
      val img2 = new Rgb.RgbBitmap(img.matrix.transpose.transpose.map(x=>x.reverse))
      img2.show("Mirrored")

      // transpose image
      val img3 = new Rgb.RgbBitmap(img.matrix.flatten.map(x => new Color(255-x.getRed(), 255-x.getGreen(), 255-x.getBlue())).grouped(img.width).toList)
      img3.show("Inverted")

      val img4 = new Rgb.RgbBitmap(img.matrix.transpose.map(x=>x.reverse))
      img4.show("90 deg rotation")

      println("finished")

    }
}