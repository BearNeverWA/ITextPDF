//import com.itextpdf.text.Element;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.*;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Test {

    public static void main(String[] args) {
//        Doc2HTML doc2HTML = new Doc2HTML();
//        doc2HTML.base64toFile(doc2HTML.doc2PDF());
//
//        String encodedString = "";
//        byte[] decoder = java.util.Base64.getDecoder().decode(encodedString);
//        InputStream is = new ByteArrayInputStream(decoder);
        GenerateBlankTransparentImg();
        //ImageMerge();
    }

    public static String getImageSize(String imageSource) {
        if (imageSource != null && imageSource.length() != 0) {
            byte[] decoder = java.util.Base64.getDecoder().decode(imageSource);
            java.io.InputStream is = new java.io.ByteArrayInputStream(decoder);
            try {
                java.awt.image.BufferedImage bufferedImage = javax.imageio.ImageIO.read(is);
                return bufferedImage.getWidth() + "," + bufferedImage.getHeight();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void ImageMerge() {
        java.io.File file = new File("/Users/leojin/Desktop/1.png");
        java.io.File file1 = new File("/Users/leojin/Desktop/1.png");
        java.awt.image.BufferedImage[] images = new java.awt.image.BufferedImage[2];
        try {
            images[0] = ImageIO.read(file);
            images[1] = ImageIO.read(file1);
            int[][] imageArrays = new int[2][];

            for (int i = 0; i < 2; i++) {
                int width = images[i].getWidth();
                int height = images[i].getHeight();
                imageArrays[i] = new int[width * height];
                imageArrays[i] = images[i].getRGB(0, 0, width, height, imageArrays[i], 0, width);
            }

            int newWidth = 0;
            int newHeight = 0;
            for (int i = 0; i < images.length; i++) {
                //横向
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();

                //纵向
                //newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                //newHeight += images[i].getHeight();
            }

            //横向
            if (newWidth < 1) return;
            //纵向
            //if (newHeight<1) return;

            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            int width_i = 0;
            int height_i = 0;
            for (int i = 0; i < images.length; i++) {
                //横向
                ImageNew.setRGB(width_i,0,images[i].getWidth(),newHeight,imageArrays[i],0,images[i].getWidth());
                width_i+=images[i].getWidth();

                //纵向
                //ImageNew.setRGB(0,height_i,newWidth,images[i].getHeight(),imageArrays[i],0,newWidth);
                //height_i+=images[i].getHeight();
            }
            ImageIO.write(ImageNew,"png",new File("/Users/leojin/Desktop/merged.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void GenerateBlankTransparentImg(){
        BufferedImage bufferedImage=new BufferedImage(300,100,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D=bufferedImage.createGraphics();

        bufferedImage=graphics2D.getDeviceConfiguration().createCompatibleImage(300,100,Transparency.TRANSLUCENT);
        graphics2D.dispose();
        graphics2D=bufferedImage.createGraphics();

        graphics2D.setColor(new Color(0,0,0,0));
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.drawRect(0,0,300,100);
        graphics2D.dispose();
        try {
            ImageIO.write(bufferedImage,"png", new File("/Users/leojin/Desktop/blank.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void markTxt(String source, String target, String img) {
        try {
            PDDocument document = PDDocument.load(new File(source));
            document.setAllSecurityToBeRemoved(true);
            PDPage page = document.getPage(0);
            PDPageContentStream stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            PDImageXObject imageXObject = PDImageXObject.createFromFile(img, document);
            PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
            graphicsState.setNonStrokingAlphaConstant(0.5f);
            graphicsState.setAlphaSourceFlag(true);
            graphicsState.getCOSObject().setItem(COSName.BM, COSName.MULTIPLY);
            stream.setGraphicsStateParameters(graphicsState);

            float width = 100;
            float height = 100;
            stream.drawImage(imageXObject, 0, 0, width, height);
            stream.drawImage(imageXObject, 200, 200, width, height);
            stream.close();
            document.save(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addMark4Word(String source, String target, String img) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(source);
            XWPFDocument document = new XWPFDocument(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(new File(img));
            ImageIO.write(image, "png", outputStream);
//            XWPFPicture xwpfPicture=run.
//            document.addPictureData(outputStream.toByteArray(), 6);
            document.write(new FileOutputStream(target));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void addTag(String filePath, String imgPath, String outFilePath) {
//        PdfReader reader = null;
//        PdfStamper stamper = null;
//        ImageIO
//        try {
//            reader = new PdfReader(filePath);
//            stamper = new PdfStamper(reader, new FileOutputStream(outFilePath));
////            BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
//            PdfGState gState = new PdfGState();
//            gState.setFillOpacity(0.5f);
//            gState.setStrokeOpacity(0.5f);
//            Image image = Image.getInstance(imgPath);
//            //first Page
//            PdfContentByte contentByte = stamper.getOverContent(1);
//            contentByte.setGState(gState);
//            image.setAbsolutePosition(0, 0);
//            image.scaleAbsolute(100, 100);
//            contentByte.addImage(image);
//
//            stamper.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            reader.close();
//        }
//    }

//    public static boolean generateImage(String imgStr, String path) {
//        BASE64Decoder decoder = new BASE64Decoder();
//        try {
//            // 解密
//            byte[] b = decoder.decodeBuffer(imgStr);
//            // 处理数据
//            for (int i = 0; i < b.length; ++i) {
//                if (b[i] < 0) {
//                    b[i] += 256;
//                }
//            }
//            OutputStream out = new FileOutputStream(path);
//            out.write(b);
//            out.flush();
//            out.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
//        markImageByIcon(iconPath, srcImgPath, targerPath, null);
//    }

    /**
     * 给图片添加水印图片、可设置水印图片旋转角度
     *
     * @param iconPath   水印图片路径
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     * @param degree     水印图片旋转角度
     */
//    public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
//        // 水印透明度
//        float alpha = 0.5f;
//        // 水印横向位置
//        int positionWidth = 150;
//        // 水印纵向位置
//        int positionHeight = 300;
//        OutputStream os = null;
//        try {
//            Image srcImg = ImageIO.read(new File(srcImgPath));
//            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
//            // 1、得到画笔对象
//            Graphics2D g = buffImg.createGraphics();
//            // 2、设置对线段的锯齿状边缘处理
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
//            // 3、设置水印旋转
//            if (null != degree) {
//                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
//            }
//            // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
//            ImageIcon imgIcon = new ImageIcon(iconPath);
//            // 5、得到Image对象。
//            Image img = imgIcon.getImage();
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
//            // 6、水印图片的位置
//            g.drawImage(img, positionWidth, positionHeight, null);
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
//            // 7、释放资源
//            g.dispose();
//            // 8、生成图片
//            os = new FileOutputStream(targerPath);
//            ImageIO.write(buffImg, "JPG", os);
//            System.out.println("图片完成添加水印图片");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != os)
//                    os.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    public static boolean addWaterMark(String srcImgPath, String tarImgPath, String waterMarkContent, Color markContentColor, Font font, int degree) {
//        try {
//            // 读取原图片信息
//            File srcImgFile = new File(srcImgPath);// 得到文件
//            BufferedImage srcImg = ImageIO.read(srcImgFile);// 文件转化为图片
//            int srcImgWidth = srcImg.getWidth(null);// 获取图片的宽
//            int srcImgHeight = srcImg.getHeight(null);// 获取图片的高
//            // 加水印
//            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = bufImg.createGraphics();
//            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
//            g.setColor(markContentColor); // 根据图片的背景设置水印颜色
//            g.setFont(font); // 设置字体
//            // 设置水印的坐标
//            int x = srcImgWidth - 2 * getWatermarkLength(waterMarkContent, g);
//            int y = srcImgHeight - 2 * getWatermarkLength(waterMarkContent, g);
//            g.rotate(Math.toRadians(degree), (double) srcImgWidth / 2, (double) srcImgHeight / 2);
//            float high = (float) (1648 / 3.7);
//            for (int i = 0; i < 9; i++) {
//                if (i % 2 == 0) {
//                    g.drawString(waterMarkContent, 0.0f - 500, i * high); // 画出水印
//                } else {
//                    g.drawString(waterMarkContent, 0.0f, i * high); // 画出水印
//                }
//            }
//            g.dispose();
//            // 输出图片
//            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
//            ImageIO.write(bufImg, "jpg", outImgStream);
//            System.out.println("添加水印完成");
//            outImgStream.flush();
//            outImgStream.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
//        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
//    }
}
