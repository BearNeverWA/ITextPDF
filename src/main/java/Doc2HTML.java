import com.spire.doc.FileFormat;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;
import sun.misc.BASE64Decoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Doc2HTML {
    // doc转换为html
    void docToHtml() {
        String sourceFileName = "res/1.doc";
        String targetFileName = "html/1.html";
        final String imagePathStr = "image/1.png";
        try {
            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            // 保存图⽚，并返回图⽚的相对路径
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] bytes, PictureType pictureType, String s, float v, float v1) {
                    try {
                        FileOutputStream out = new FileOutputStream(imagePathStr + s);
                        out.write(bytes);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "image/" + s;
                }
            });
//            wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
//                try (FileOutputStream out = new FileOutputStream(imagePathStr + name)) {
//                    out.write(content);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return "image/" + name;
//            });
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(new File(targetFileName));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //     docx转换为html
    public void docxToHtml() {
        String sourceFileName = "res/1.docx";
        String targetFileName = "html/2.html";
        String imagePathStr = "image/";
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图⽚的⽂件夹
            options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
            // html中图⽚的路径
            options.URIResolver(new BasicURIResolver("image"));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String doc2PDF() {
        String s=null;
        try {
            byte[] b = Files.readAllBytes(Paths.get("res/1.doc"));
            InputStream inputStream = new ByteArrayInputStream(b);
            com.spire.doc.Document document = new com.spire.doc.Document();
            document.loadFromStream(inputStream, FileFormat.Doc);
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            document.saveToFile(outputStream,FileFormat.PDF);
            s=Base64.getEncoder().encodeToString(outputStream.toByteArray());
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void base64toFile(String baseString){
        try{
            byte[] b=Base64.getDecoder().decode(baseString);
            for (int i=0;i<b.length;i++){
                if (b[i]<0) b[i]+=256;
            }
            OutputStream outputStream=new FileOutputStream("pdf/1.pdf");
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}