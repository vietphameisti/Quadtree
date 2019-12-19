import java.io.FileInputStream

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

/** MAIN function. */
object Launcher  {
    def main(args: Array[String]): Unit = {

      Application.launch(classOf[GUI], args: _*)

    }
}

/** FXML GUI application. */
class GUI extends Application {

  override def start(primaryStage: Stage) {

    val openFXML=new FXMLLoader(getClass.getResource("ImageProcessingWithQuadtree.fxml"))

    val root=openFXML.load[Parent]
    val scene=new Scene(root)
    val img=new Image( new FileInputStream("src/main/Images/images.png"))
    primaryStage.setScene(scene)
    primaryStage.getIcons().add(img)
    primaryStage.setTitle("Image Processing With QuadTree ")
    primaryStage.show()
  }
}