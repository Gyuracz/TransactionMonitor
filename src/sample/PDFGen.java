package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;

public class PDFGen {

    public void pdfGeneration(String fileName, ObservableList<Accounts> accounts, ObservableList<Expenditures> expenditures, ObservableList<Incomes> incomes){
        Document pdf = new Document();
        try{
            PdfWriter.getInstance(pdf, new FileOutputStream(fileName + ".pdf"));
            pdf.open();
            pdf.add(createAccountsTable(accounts));
            pdf.add(new Paragraph("\n\n"));
            pdf.add(createExpendituresTable(expenditures));
            pdf.add(new Paragraph("\n\n"));
            pdf.add(createIncomesTable(incomes));
        }catch(Exception e){
            e.printStackTrace();
        }
        pdf.close();
    }

    public PdfPTable createAccountsTable(ObservableList<Accounts> accounts){
        float[] columnWidths = {1, 3, 3};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Bank accounts"));
        cell.setBackgroundColor(GrayColor.GRAYWHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(3);
        table.addCell(cell);

        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Balance");
        table.setHeaderRows(1);

        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        for(int i = 0; i < accounts.size(); i++){
            int act = i + 1;
            table.addCell("" + act);
            table.addCell(accounts.get(i).getName());
            table.addCell(accounts.get(i).getBalance());
        }

        return table;
    }

    public PdfPTable createExpendituresTable(ObservableList<Expenditures> expenditures){
        float[] columnWidths = {1, 3, 3, 3, 3};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Expenditures"));
        cell.setBackgroundColor(GrayColor.GRAYWHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        table.addCell(cell);

        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("ID");
        table.addCell("Account");
        table.addCell("Amount");
        table.addCell("Category");
        table.addCell("Date");
        table.setHeaderRows(1);

        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        for(int i = 0; i < expenditures.size(); i++){
            int act = i + 1;
            table.addCell("" + act);
            table.addCell(expenditures.get(i).getAccount());
            table.addCell(expenditures.get(i).getAmount());
            table.addCell(expenditures.get(i).getCategory());
            table.addCell(expenditures.get(i).getDate());
        }

        return table;
    }

    public PdfPTable createIncomesTable(ObservableList<Incomes> incomes){
        float[] columnWidths = {1, 3, 3, 3, 3};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Incomes"));
        cell.setBackgroundColor(GrayColor.GRAYWHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        table.addCell(cell);

        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("ID");
        table.addCell("Account");
        table.addCell("Amount");
        table.addCell("Category");
        table.addCell("Date");
        table.setHeaderRows(1);

        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        for(int i = 0; i < incomes.size(); i++){
            int act = i + 1;
            table.addCell("" + act);
            table.addCell(incomes.get(i).getAccount());
            table.addCell(incomes.get(i).getAmount());
            table.addCell(incomes.get(i).getCategory());
            table.addCell(incomes.get(i).getDate());
        }

        return table;
    }
}
