package cbmgo.codec.contatos.service;


import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.Date;

public class ExcelUtils {


    public static Date getDate(XSSFRow row, int column) {


        try{
            Date date = row.getCell(column).getDateCellValue();
            return date;

        }catch (Exception e){
            System.out.println(e);
//            System.out.println(row.getCell(column).getDateCellValue());
        }

        return null;
    }

    public static String  getText(XSSFRow row, int column){

        String text;
        try{
            text = row.getCell(column).getStringCellValue();
            if(text.length()==0 || text.isEmpty() || text.equals("\\N") || text.equals("-")) return null;
            return text;
        }catch (Exception e){

            //log.error(e);
            try{
                text = String.valueOf(row.getCell(column).getNumericCellValue());
                return text;
            }catch (Exception e2){
                //log.error(e2);
            }
        }
        return "";
    }

    public static long getLongCell( XSSFRow row, int column){

        try{
            return (long) row.getCell(column).getNumericCellValue();
        }catch (Exception e){

            //log.error(e);
            try{
                return Long.valueOf(row.getCell(column).getStringCellValue());
            }catch (Exception e2){
                return 0;
            }
        }
    }

    public static int getIntegerCell( XSSFRow row, int column){

        try{
            return (int) row.getCell(column).getNumericCellValue();
        }catch (Exception e){

            //log.error(e);
            try{
                return Integer.valueOf(row.getCell(column).getStringCellValue());
            }catch (Exception e2){
                return 0;
            }
        }
    }

    public static Number getNumericCell( XSSFRow row, int column){

        try{
            return row.getCell(column).getNumericCellValue();
        }catch (Exception e){

            //log.error(e);
            try{
                return Double.valueOf(row.getCell(column).getStringCellValue());
            }catch (Exception e2){
                return null;
            }
        }
    }
}
