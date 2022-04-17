package cbmgo.codec.contatos.service;

import comum.model.persistent.Municipio;
import comum.model.persistent.rural.Colaborador;
import comum.model.persistent.rural.Propriedade;
import comum.repository.ColaboradorRepository;
import comum.repository.MunicipioRepository;
import comum.repository.PropriedadeRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ContatosRuralService {


    @Autowired
    PropriedadeRepository propriedadeRepository;
    @Autowired
    MunicipioRepository municipioRepository;
    @Autowired
    MunicipioService municipioService;
    @Autowired
    ColaboradorRepository colaboradorRepository;


    public String saveColaboradores(MultipartFile file){


        try{
            InputStream excelInputStream = file.getInputStream();

            saveColaboradoresFromExcel(excelInputStream);
            excelInputStream.close();

            return "Planilha inserida com sucesso!";
        }catch(Exception e){
            e.printStackTrace();
            return  "Erro ao inserir a planilha. Por favor verificar a formação dos dados conforme o modelo";
        }


    }



    public void saveColaboradoresFromExcel(InputStream colaboradoresIS) throws IOException {

        List<Colaborador> colaboradores = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(colaboradoresIS);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        int counter =1;

        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            XSSFRow row = worksheet.getRow(i);

            Colaborador colaborador =  Colaborador.builder()
                    .id(ExcelUtils.getLongCell(row,0))
                    .propriedade(new Propriedade(ExcelUtils.getLongCell(row,1)))
                    .nome(ExcelUtils.getText(row,2))
                    .cpf(ExcelUtils.getText(row,3))
                    .rg(ExcelUtils.getText(row,4))
//                    .sexo(ExcelUtils.getText(row,5))
//                    .tipopess(ExcelUtils.getText(row,6))
                    .naturalidade(ExcelUtils.getText(row,7))
                    .pai(ExcelUtils.getText(row,8))
                    .mae(ExcelUtils.getText(row,9))
//                    .dn(10)
//                    .foto(11)
                    .obs(ExcelUtils.getText(row,12))
                    .endereco(ExcelUtils.getText(row,13))

                    .build();

            colaboradores.add(colaborador);
            System.out.println(counter++ +" " + colaborador);

        }

        colaboradorRepository.saveAll(colaboradores);
    }

    public String saveMunicipiosFromIBGE(MultipartFile file){


        try{
            InputStream excelInputStream = file.getInputStream();

            saveMunicipiosFromExcel(excelInputStream);
            excelInputStream.close();

            return "Planilha inserida com sucesso!";
        }catch(Exception e){
            e.printStackTrace();
            return  "Erro ao inserir a planilha. Por favor verificar a formação dos dados conforme o modelo";
        }
    }

    public void saveMunicipiosFromExcel(InputStream obmContacts) throws IOException {

        List<Municipio> municipios = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(obmContacts);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        int counter =1;

        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            XSSFRow row = worksheet.getRow(i);

            Municipio municipio =  Municipio.builder()
                    .id(Long.valueOf(row.getCell(11).getStringCellValue()))
                    .municipio(row.getCell(12).getStringCellValue())
                    .uf(row.getCell(1).getStringCellValue())
                    .ufCodigo(Integer.valueOf(row.getCell(0).getStringCellValue()))
                    .build();

            if(municipio.getUfCodigo().compareTo(52) == 0){
                municipios.add(municipio);
                System.out.println(counter++ +" " + municipio.getMunicipio());
            }

        }


        municipioRepository.saveAll(municipios);
    }


    @Transactional
    public String savePropriedadesRurais(MultipartFile file){


        try{
            InputStream excelInputStream = file.getInputStream();

            persistPropriedadesRurais(excelInputStream);
            excelInputStream.close();

            return "Planilha inserida com sucesso!";
        }catch(Exception e){
            e.printStackTrace();
            return  "Erro ao inserir a planilha. Por favor verificar a formação dos dados conforme o modelo";
        }


    }




    @Transactional
    public void persistPropriedadesRurais(InputStream propriedadesIS) throws IOException {

        List<Propriedade> propriedades = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(propriedadesIS);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        Long municipioId;
        String municipio = null;

        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            XSSFRow row = worksheet.getRow(i);

            try{
                municipioId = ExcelUtils.getLongCell(row,11);
                if(municipioId != 0){
                    municipio = municipioService.findMunicipioNome(municipioId);
                }

            } catch (Exception e) {
                e.printStackTrace();
                municipio = null;
            }


            Propriedade propriedade =  Propriedade.builder()
                    .id((long) ExcelUtils.getLongCell(row,0))
                    .id_atividade( ExcelUtils.getLongCell(row,5))
                    .cidade(municipio)
                    .nome(ExcelUtils.getText(row,6))
                    .latitude((Double) ExcelUtils.getNumericCell(row ,7))
                    .longitude((Double) ExcelUtils.getNumericCell(row ,8))
                    .nr_propriedade(ExcelUtils.getLongCell(row,9))
                    .referencia(ExcelUtils.getText(row,10))
                    .dataDeRegistro(localDateTimeConverter(ExcelUtils.getText(row,21)))
                    .build();

//            if(i <= 10){
                propriedades.add(propriedade);
                System.out.println(i +" " + propriedade);
//            }else{
//                return;
//            }
        }

        propriedadeRepository.saveAll(propriedades);
    }

    

    public static LocalDateTime localDateTimeConverter(String stringDate){

        if(stringDate.equals("\\N")) return null;

        try{
            String date = stringDate.substring(0,19);
            if(stringDate.length() == 0) return null;
            DateTimeFormatter formatter;
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            return localDateTime;
        }catch (Exception e){
            return null;
        }

    }




    public void write(List<Colaborador> total, String excelFilePath) throws IOException, InvalidFormatException {

        try(
                FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                Workbook workbook = WorkbookFactory.create(inputStream);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = 1;
            Float ord = 0f;

            for(int i=0; i < total.size(); i++){

                Row row = sheet.createRow(++rowCount);
                writeResult(workbook, total.get(i), row, ++ord);

            }

//            ESCREVE NO ARQUIVO NO DIRETORIO ESPECIFICADO EM "excelFilePath)
//            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
//                workbook.write(outputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            workbook.write(out);

        }
    }

    private void writeResult(Workbook workbook, Colaborador result, Row row, Float ord) {
//
//        int i = 0;
//
//        //style
//        CellStyle style = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        style.setFont(font);
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//
//        Cell cell0 = row.createCell(i++);
//        cell0.setCellValue(ord);
//        cell0.setCellStyle(style);
//
//        Cell cell = row.createCell(i++);
//        cell.setCellValue(result.getReference());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getSymbol());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getData().toString());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getQttyBuy());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getPriceBuy());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getMediumBuyPrice());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getQttySell());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getPriceSell());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getMediumSellPrice());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getQttyResult());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getLastQtty());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getLastMediumPrice());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getProfitDayTrade());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getProfitNormal());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getProfitResult());
//
//        cell = row.createCell(i++);
//        cell.setCellValue(result.getComment());

    }

}
