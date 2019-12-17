import java.awt.image.BufferedImage
import java.io.File

import javafx.collections.{FXCollections, ObservableList}
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
class ImageProcessingWithQuadtree {
  @FXML val btnCompress=new Button
  @FXML val btnRotate=new Button
//  @FXML private var listImage: ListView[String] = _
  @FXML private var listImage: ListView[File] = _
  @FXML private var imageViewOri: ImageView = _
  @FXML private var imageViewManipulate: ImageView= _
  @FXML private var lblContent: Label= _
  @FXML
  def initialize(): Unit = {

    val listFile:List[File]=getListOfFiles("src/main/Images")
   // val listFiles: ObservableList[String] = FXCollections.observableArrayList(listFile: _*)
//    val rssFeeds = "scala java c#".split(" ")
//    val rssFeedsData: ObservableList[String] = FXCollections.observableArrayList(rssFeeds: _*)
//    listImage.setItems(rssFeedsData)
    val listFileObs: ObservableList[File] = FXCollections.observableArrayList(listFile: _*)
    listImage.setItems(listFileObs)
    //test for controller
   // lblContent.setText("Hello controller")
   // print(listFile)
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
  def compressImage():Unit= {
    // load image and display it
    val img = Rgb.BitmapLoader.load(listImage.getSelectionModel().getSelectedItems().toString.replaceAll("\\[|\\]", "")).get
    // build quadtree from image
    val quadtree = new Quadtree(img)
    // build compressed image from quadtree
    val compressed = quadtree.compress(5)
    val imagebff: BufferedImage=img.showImageViewer(compressed)
    val image = SwingFXUtils.toFXImage(imagebff, null)
    imageViewManipulate.setImage(image)
  }
//  def mouseClickListview(listImage: ListView[File]): Unit = {
//    listImage.getSelectionModel.selectedItemProperty.addListener(
//      new ChangeListener[File] {
//        def changed(ov: ObservableValue[_ <: File], oldValue: File, newValue: File) {
//          val file = new File(newValue.toString)
//          val image = new Image(file.toURI.toString)
//          imageViewOri.setImage(image)
//        }
//      })
//  }
  @FXML
  def   mouseClickedListView (event: MouseEvent):Unit = {
      if (event.getClickCount() >= 1 && listImage.getSelectionModel().getSelectedItem() != null) {
        lblContent.setText("Selected: " +
        listImage.getSelectionModel().getSelectedItems().toString.replaceAll("\\[|\\]", ""))
        val file = new File(listImage.getSelectionModel().getSelectedItems().toString.replaceAll("\\[|\\]", ""))
        //Display the ppm image on imageview
        val img = Rgb.BitmapLoader.load(listImage.getSelectionModel().getSelectedItems().toString.replaceAll("\\[|\\]", "")).get
        val imagebff: BufferedImage=img.showImageViewer(img)
        val image = SwingFXUtils.toFXImage(imagebff, null)
        imageViewOri.setImage(image)
    }
    }
}
