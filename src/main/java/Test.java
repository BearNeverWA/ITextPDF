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
        Doc2HTML doc2HTML=new Doc2HTML();
        doc2HTML.base64toFile(doc2HTML.doc2PDF());
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
//            // ??????
//            byte[] b = decoder.decodeBuffer(imgStr);
//            // ????????????
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
     * ???????????????????????????????????????????????????????????????
     *
     * @param iconPath   ??????????????????
     * @param srcImgPath ???????????????
     * @param targerPath ??????????????????
     * @param degree     ????????????????????????
     */
//    public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
//        // ???????????????
//        float alpha = 0.5f;
//        // ??????????????????
//        int positionWidth = 150;
//        // ??????????????????
//        int positionHeight = 300;
//        OutputStream os = null;
//        try {
//            Image srcImg = ImageIO.read(new File(srcImgPath));
//            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
//            // 1?????????????????????
//            Graphics2D g = buffImg.createGraphics();
//            // 2??????????????????????????????????????????
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
//            // 3?????????????????????
//            if (null != degree) {
//                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
//            }
//            // 4???????????????????????? ?????????????????????gif??????png??????????????????????????????
//            ImageIcon imgIcon = new ImageIcon(iconPath);
//            // 5?????????Image?????????
//            Image img = imgIcon.getImage();
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
//            // 6????????????????????????
//            g.drawImage(img, positionWidth, positionHeight, null);
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
//            // 7???????????????
//            g.dispose();
//            // 8???????????????
//            os = new FileOutputStream(targerPath);
//            ImageIO.write(buffImg, "JPG", os);
//            System.out.println("??????????????????????????????");
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
//            // ?????????????????????
//            File srcImgFile = new File(srcImgPath);// ????????????
//            BufferedImage srcImg = ImageIO.read(srcImgFile);// ?????????????????????
//            int srcImgWidth = srcImg.getWidth(null);// ??????????????????
//            int srcImgHeight = srcImg.getHeight(null);// ??????????????????
//            // ?????????
//            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = bufImg.createGraphics();
//            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
//            g.setColor(markContentColor); // ???????????????????????????????????????
//            g.setFont(font); // ????????????
//            // ?????????????????????
//            int x = srcImgWidth - 2 * getWatermarkLength(waterMarkContent, g);
//            int y = srcImgHeight - 2 * getWatermarkLength(waterMarkContent, g);
//            g.rotate(Math.toRadians(degree), (double) srcImgWidth / 2, (double) srcImgHeight / 2);
//            float high = (float) (1648 / 3.7);
//            for (int i = 0; i < 9; i++) {
//                if (i % 2 == 0) {
//                    g.drawString(waterMarkContent, 0.0f - 500, i * high); // ????????????
//                } else {
//                    g.drawString(waterMarkContent, 0.0f, i * high); // ????????????
//                }
//            }
//            g.dispose();
//            // ????????????
//            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
//            ImageIO.write(bufImg, "jpg", outImgStream);
//            System.out.println("??????????????????");
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
