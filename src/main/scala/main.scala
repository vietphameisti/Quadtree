import scala.swing._
import javax.swing.ImageIcon

object Launcher {

    def main(args: Array[String]): Unit = {
      val img = Pix.Pixmap.load("src/main/Images/sign_1.ppm").get

      val mainframe = new MainFrame() {
        title = "Test"
        visible = true
        contents = new Label() {
          icon = new ImageIcon(img.image)
        }
      }
    }
}