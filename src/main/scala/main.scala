/** Contains the main function. */
object Launcher {
    def main(args: Array[String]): Unit = {

      // load image and display it
      val img = Rgb.BitmapLoader.load("src/main/Images/synth_1.ppm").get
      img.show

      // recursively build the quadtree
      val quadtree = new Quadtree(img)
      quadtree.show("")

      println("finished")

    }
}