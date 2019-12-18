import java.awt.image.BufferedImage
import java.io.File
import java.awt.Color

import javafx.collections.{FXCollections, ObservableList}
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent

import javafx.scene.control.ComboBox
import javafx.collections.FXCollections

import javafx.scene.control.TextArea;


class GuiController {
  var img: Rgb.RgbBitmap = new Rgb.RgbBitmap(List(List(new Color(255, 255, 255))))
  var cur_img: Rgb.RgbBitmap = new Rgb.RgbBitmap(List(List(new Color(255, 255, 255))))

  var quadtree: Quadtree = new Quadtree(img)

  @FXML private var listImage: ListView[File] = _
  @FXML private var imageViewOri: ImageView = _
  @FXML private var imageViewManipulate: ImageView= _
  @FXML private var lblContent: Label= _

  @FXML var tree: TextArea = _

  @FXML
  def initialize(): Unit = {
    val listFile:List[File]=getListOfFiles("src/main/resources/Images")
    val listFileObs: ObservableList[File] = FXCollections.observableArrayList(listFile: _*)
    listImage.setItems(listFileObs)
  }

  //Get list of files
  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      //d.listFiles.filter(_.isFile).toList
      d.listFiles.filter(_.getName.endsWith(".ppm")).toList
    } else {
      List[File]()
    }
  }

  @FXML
  def   mouseClickedListView (event: MouseEvent):Unit = {
      if (event.getClickCount() >= 1 && listImage.getSelectionModel().getSelectedItem() != null) {

        //Get selected image
        val selectedimg = listImage.getSelectionModel().getSelectedItems().toString.replaceAll("\\[|\\]", "")
        lblContent.setText("Selected: " + selectedimg)

        // build quadtree from image
        img = Rgb.BitmapLoader.load(selectedimg).get
        cur_img = img
        quadtree = new Quadtree(img)

        //Display the ppm image on imageview
        val imagebff: BufferedImage=img.showImageViewer(img)
        val image = SwingFXUtils.toFXImage(imagebff, null)
        imageViewOri.setImage(image)

        //Display the image on imageview
        update()
      }
    }

  @FXML
  def compressImage():Unit= {
    // compression rate = 10%
    val depth = quadtree.getMaxDepth
    val compressed_depth: Int = (0.9 * depth).toInt

    // build compressed image from quadtree
    quadtree.compress(compressed_depth)
    cur_img = quadtree.toBitmap

    //Display the image on imageview
    update()
  }

  @FXML
  def rotateImage():Unit= {
    cur_img = cur_img.rotate

    //Display the image on imageview
    update()
  }

  @FXML
  def invertImage():Unit= {
    cur_img = cur_img.invert

    //Display the image on imageview
    update()
  }

  @FXML
  def flipLR():Unit= {
    cur_img = cur_img.mirrorLR

    //Display the image on imageview
    update()
  }

  @FXML
  def flipTB():Unit= {
    cur_img = cur_img.mirrorTB

    //Display the image on imageview
    update()
  }

  @FXML
  def printQuad():Unit= {
    //Display the tree in the TextArea
    val buf = new StringBuilder
    quadtree.print("", buf)
    tree.setText(buf.toString)
  }

  @FXML
  def save():Unit= {
    Rgb.BitmapLoader.save(cur_img, "saved.ppm")
  }

  private def update() {
    val imagebff: BufferedImage=cur_img.showImageViewer(cur_img)
    val image = SwingFXUtils.toFXImage(imagebff, null)
    imageViewManipulate.setImage(image)

    quadtree = new Quadtree(cur_img)
  }
}
