import java.awt.image.BufferedImage
import java.io._

import Rgb.RgbBitmap
import javax.imageio.ImageIO
import java.io.File;

import scala.swing.Color
object Pixmap {

  private case class PpmHeader(format: String, width: Int, height: Int, maxColor: Int)

  def load(filename: String): Option[RgbBitmap] = {
    implicit val in = new BufferedInputStream(new FileInputStream(filename))
    val header = readHeader
    if (header.format == "P6") {
      // after reading the header file of ppm image, create an RgbBitmap object with width, height properties
      val bm = new RgbBitmap(header.width, header.height);
      for (y <- 0 until bm.height; x <- 0 until bm.width; c = readColor)
        bm.setPixel(x, y, c)
      return Some(bm)
    }
    else
    {
      print("this error")
      return None
    }
//    None
  }

  private def readHeader(implicit in: InputStream) = {
    var format = readLine
    var line = readLine
    while (line.startsWith("#"))
      line = readLine
    val parts = line.split("\\s")
    val width = parts(0).toInt
    val height = parts(1).toInt
    val maxColor = readLine.toInt
    new PpmHeader(format, width, height, maxColor)
  }
  def save(bm:RgbBitmap, filename:String)=
    {
      val out=new DataOutputStream(new FileOutputStream(filename))
      out.writeBytes("P6\u000a%d %d\u000a%d\u000a".format(bm.width,bm.height,255))
      for(y <- 0 until bm.height; x <- 0 until bm.width; c=bm.getPixel(x, y)){
        out.writeByte(c.getRed)
        out.writeByte(c.getGreen)
        out.writeByte(c.getBlue)
      }
    }

  //using color class of swing package
  private def readColor(implicit in: InputStream) = new Color(in.read, in.read, in.read)

  private def readLine(implicit in: InputStream) = {
    var out = ""
    var b = in.read
    while (b != 0xA) {
      out += b.toChar;
      b = in.read()
    }
    out
  }

private def convertToMatrix(img:BufferedImage ): Unit ={
  val width=img.getWidth
  val height=img.getHeight
  var imageArray=Array.ofDim[Int](height,width)
  for (i<-0 to height){
  for(j<-0 to width)
    {
      imageArray(i)(j)=img.getRGB(i,j)
    }}
  imageArray
}
    def main(args: Array[String]): Unit = {
      val img = Pixmap.load("C:\\Users\\Administrator\\IdeaProjects\\Quadtree\\src\\main\\Images\\sign_1.ppm").get
      //val grayImg = BitmapOps.grayscale(img);
      //Pixmap.save(grayImg, "/image_gray.ppm")
//      val mainframe = new MainFrame() {
//        title = "Test"
//        visible = true
//        contents = new Label() {
//          icon = new ImageIcon(img.image)
//        }
//      }
      val file:File=new File("C:\\Users\\Administrator\\IdeaProjects\\Quadtree\\src\\main\\Images\\EISTI-logo-1.jpg")
      val fis=new BufferedInputStream(new FileInputStream(file))
      // imgBuff: BuffBufferedInputStreameredImage =ImageIO.read(new File("C:\\Users\\Administrator\\IdeaProjects\\Quadtree\\src\\main\\Images\\sign_1.ppm"))
      val imgBuff=ImageIO.read(fis)
      val width=imgBuff.getWidth()
      val height=imgBuff.getHeight()
      var imageArray=Array.ofDim[Int](height,width)
      for (i<-0 until height)
        for(j<-0 until width)
          imageArray(i)(j)=imgBuff.getRGB(i,j)}


//      for (i<-0 until height)
//        for(j<-0 until width)
//          print(imageArray(i)(j))

}