package org.avalon.rainbow.common.docgen;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.avalon.rainbow.common.utils.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public abstract class PdfTemplateGenerator implements IDocGenerator {

    private Rectangle pageSize;
    private boolean needPageNo;
    private String header;
    private int headerAlignment = 1;
    private String footer;
    private int footerAlignment = 1;
    private String font = "styles/simhei.ttf";

    protected byte[] generatePdf(byte[] content) throws Exception {
        Document document = new Document();
        document.setPageSize(Objects.requireNonNullElse(pageSize, DocumentConsts.PAGE_A4));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        if (needPageNo || StringUtils.isNotBlank(header) || StringUtils.isNotBlank(footer)) {
            pdfWriter.setPageEvent(initHeaderFooter());
        }
        document.open();
        if (StringUtils.isBlank(font)) {
            font = "styles/simhei.ttf";
        }
        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontProvider.register(font);
        String cssPath = this.getClass().getResource("/").getPath();
        cssPath = cssPath.replaceAll("%20", " ") + "styles/doc.css";
        if ("\\".equals(File.separator)) {
            cssPath = cssPath.replaceAll("/", "\\\\");
        }
        if (!cssPath.startsWith(File.separator)) {
            cssPath = File.separator + cssPath;
        }
        FileInputStream inputStream = new FileInputStream(cssPath);
        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,
                new ByteArrayInputStream(content), inputStream, StandardCharsets.UTF_8, fontProvider);
        inputStream.close();
        outputStream.close();
        pdfWriter.close();
        document.close();
        return outputStream.toByteArray();
    }

    protected PdfPageEventHelper initHeaderFooter() {
        return new PdfPageEventHelper(){
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                float center = document.getPageSize().getRight() / 2;
                float left = document.getPageSize().getLeft() + 48;
                float right = document.getPageSize().getRight() - 48;
                float top = document.getPageSize().getTop() - 36;
                float bottom = document.getPageSize().getBottom() + 36;
                Font font1 = FontFactory.getFont(getFont(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
                        10, Font.NORMAL, BaseColor.BLACK);

                if (StringUtils.isNotBlank(header)) {
                    Phrase headerPhrase = new Phrase(header, font1);
                    ColumnText.showTextAligned(writer.getDirectContent(), headerAlignment, headerPhrase, center, top, 0);
                }
                if(StringUtils.isNotBlank(footer) && !needPageNo) {
                    Phrase phrase = new Phrase(footer, font1);
                    ColumnText.showTextAligned(writer.getDirectContent(), footerAlignment, phrase, center, bottom, 0);
                }
                if (StringUtils.isNotBlank(footer) && needPageNo) {
                    Phrase phrase = new Phrase(footer, font1);
                    ColumnText.showTextAligned(writer.getDirectContent(), footerAlignment, phrase, left, bottom, 0);
                    Phrase pageNumberPh = new Phrase(String.valueOf(document.getPageNumber()), font1);
                    ColumnText.showTextAligned(writer.getDirectContent(), footerAlignment, pageNumberPh, center, bottom, 0);
                }
                if (StringUtils.isBlank(footer) && needPageNo) {
                    Phrase pageNumberPh = new Phrase(String.valueOf(document.getPageNumber()), font1);
                    ColumnText.showTextAligned(writer.getDirectContent(), footerAlignment, pageNumberPh, center, bottom, 0);
                }
                super.onEndPage(writer, document);
            }
        };
    }

    public byte[] generate(String templateName, Map<String, Object> params) throws Exception {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        Context context = new Context();
        context.setVariables(params);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(resolver);
        String htmlString = templateEngine.process(templateName, context);
        return generatePdf(htmlString.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public byte[] generate() throws Exception {
/*        String templatePath = this.getClass().getResource("/").getPath();
        templatePath = templatePath.substring(1).replaceAll("%20", " ") + "documents/";
        String thymeleafPath = getTemplate();
        String fileName = getTemplate();
        if (thymeleafPath.contains("/")) {
            fileName = thymeleafPath.substring(thymeleafPath.lastIndexOf("/") + 1);
            templatePath += thymeleafPath.substring(0, thymeleafPath.lastIndexOf("/"));
        }
        if ("\\".equals(File.separator)) {
            templatePath = templatePath.replaceAll("/", "\\\\");
        }
        if (!templatePath.startsWith(File.separator)) {
            templatePath = File.separator + templatePath;
        }*/
        return generate(getTemplate(), getParameters());

/*        Configuration config = new Configuration(Configuration.VERSION_2_3_30);
        config.setDirectoryForTemplateLoading(new File(templatePath));
        Template template = config.getTemplate(fileName, "UTF-8");
        StringWriter writer = new StringWriter();
        template.process(getParameters(), writer);
        writer.close();*/
    }

    public Rectangle getPageSize() {
        return pageSize;
    }

    public void setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isNeedPageNo() {
        return needPageNo;
    }

    public void setNeedPageNo(boolean needPageNo) {
        this.needPageNo = needPageNo;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getHeaderAlignment() {
        return headerAlignment;
    }

    public void setHeaderAlignment(int headerAlignment) {
        this.headerAlignment = headerAlignment;
    }

    public int getFooterAlignment() {
        return footerAlignment;
    }

    public void setFooterAlignment(int footerAlignment) {
        this.footerAlignment = footerAlignment;
    }
}
