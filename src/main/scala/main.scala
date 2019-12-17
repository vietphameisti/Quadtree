import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

/** Contains the main function. */
object Launcher  {
    def main(args: Array[String]): Unit = {

      // load image and display it
      val img = Rgb.BitmapLoader.load("src/main/Images/sign_1.ppm").get
      //img.show("Original Image")

      // build quadtree from image
      val quadtree = new Quadtree(img)
      quadtree.show("")

      // build image from quadtree
      quadtree.toBitmap().show("Reconstructed From Quadtree")

      // build compressed image from quadtree
      val compressed = quadtree.compress(5)
      compressed.show("Compressed")

      // save image
      Rgb.BitmapLoader.save(compressed, "saved.ppm")

      // Mirror image
      img.mirrorLR.show("Mirrored LR")
      img.mirrorTB.show("Mirrored TB")

      // Invert colors in image
      img.invert.show("Inverted")

      // Rotate 90 degrees
      img.rotate.show("90 deg rotation")

      println("finished")
      Application.launch(classOf[GUI], args: _*)

    }
}

class GUI extends Application {

  override def start(primaryStage: Stage) {

    val openFXML=new FXMLLoader(getClass.getResource("/ImageProcessingWithQuadtree.fxml"))
    //openFXML.setController(new ImageProcessingWithQuadtree)
    val root=openFXML.load[Parent]
    val scene=new Scene(root)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}