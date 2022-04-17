package cbmgo.codec.contatos.service;


import cbmgo.codec.contatos.service.rest.ApiClient;
import comum.model.dto.security.SignUpForm;
import comum.model.persistent.UserSicad;
import comum.repository.UserSicadRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContatosBMService {

    @Autowired
    UserSicadRepository userSicadRepository;
    @Autowired
    ApiClient apiClient;


//    @PostConstruct
    public String saveContatos(MultipartFile file, String obm){

//        final String FILE_NAME = "C:\\Users\\I7_TV_Touch\\Downloads\\Plano-de-Chamada.xlsx";

        try{
//            InputStream excelInputStream = new FileInputStream(new File(FILE_NAME));
//            InputStream excelInputStream = new FileInputStream(file);
            InputStream excelInputStream = file.getInputStream();

            persistExcelFile(excelInputStream, obm);
            excelInputStream.close();

            return "Planilha inserida com sucesso!";
        }catch(Exception e){
            e.printStackTrace();
            return  "Erro ao inserir a planilha. Por favor verificar a formação dos dados conforme o modelo";
        }


    }


    public void persistExcelFile(InputStream obmContacts, String obm) throws IOException {

        List<UserSicad> users = new ArrayList<UserSicad>();
        XSSFWorkbook workbook = new XSSFWorkbook(obmContacts);
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            XSSFRow row = worksheet.getRow(i);

            System.out.println(ExcelUtils.getDate(row,22));


            UserSicad user =  UserSicad.builder()
                    .id(Long.valueOf(ExcelUtils.getText(row,12).toLowerCase().replace(".", "").replace("-","")))
                    //.atualizacao(row.getCell(0).getStringCellValue())
                    .patente(ExcelUtils.getText(row, 1 ))
                    .quadroFuncional(ExcelUtils.getText(row, 2))
                    .rg(ExcelUtils.getIntegerCell(row,3))
                    .nome(ExcelUtils.getText(row, 4 ) )
                    .obm(ExcelUtils.getText(row, 5 ) )
                    .celular(ExcelUtils.getText(row, 6) )
                    .telefeneResidencial(ExcelUtils.getText(row, 7) )
                    .email(ExcelUtils.getText(row, 8) )
                    .email1(ExcelUtils.getText(row, 9))
                    .funcoes(new ArrayList<>(Arrays.asList(row.getCell(10).getStringCellValue().toLowerCase())))
                    //11
                    .cpf(ExcelUtils.getText(row,12).toLowerCase().replace(".", "").replace("-",""))
                    .nomeDeGuerra(ExcelUtils.getText(row,13))
                    .nascimento(ExcelUtils.getText(row,14))
                    .status(ExcelUtils.getText(row,15))
                    .sexo(ExcelUtils.getText(row,16))
                    .regional(ExcelUtils.getText(row,17))
                    .ausenciaMotivo(ExcelUtils.getText(row,18))
                    .ausenciaInicio(convertToLocalDate(ExcelUtils.getDate(row,19)))
                    .ausenciaFim(convertToLocalDate(ExcelUtils.getDate(row,20)))
                    .operacional(isOperacional (ExcelUtils.getText(row,21)))
                    .inclusao(convertToLocalDate(ExcelUtils.getDate(row,22)))
                    .municipio(ExcelUtils.getText(row, 23))
                    .uf(ExcelUtils.getText(row, 24))
                    .build();

            user.setUsername(user.getCpf());

            System.out.println(i +" " + user);
            users.add(user);
        }

        List<UserSicad> filteredtUsers = groupUsers(users, obm);

        userSicadRepository.saveAll(filteredtUsers);
//        createUsersKeyCloak(users);
    }

    public void createUsersKeyCloak(List<UserSicad> users){


        for(UserSicad userSicad : users){
            try{
                SignUpForm signUpRequest = new SignUpForm(userSicad);
                apiClient.registerUser(signUpRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //Filter distinct users by obm
    public List<UserSicad> groupUsers(List<UserSicad> users, String obm){

        List<UserSicad> distinctUsers = new ArrayList<>();
        Map<String, List<UserSicad>> groupedUsers = users.stream()
                .collect(Collectors.groupingBy(UserSicad::getCpf));

        for (Map.Entry<String, List<UserSicad>> listaDeUsuariosAgrupadosPorCPF : groupedUsers.entrySet()) {

            //criar um usuario para juntar as funções
            UserSicad user = listaDeUsuariosAgrupadosPorCPF.getValue().get(0);
            user.setFuncoes(new ArrayList<>());

            List<UserSicad> listaDoMesmoUsuario = listaDeUsuariosAgrupadosPorCPF.getValue();


            if(obm != null && obm.length() != 0){
                if(user.getObm().trim().toLowerCase().equals(obm.trim().toLowerCase())){

                    for(UserSicad u: listaDoMesmoUsuario){
                        user.getFuncoes().addAll(u.getFuncoes());
                    }

                    distinctUsers.add(user);
                }
            }else{
                distinctUsers.add(user);
            }

        }

        return distinctUsers;
    }


    public LocalDate convertToLocalDate(Date dateToConvert) {

        if(dateToConvert == null || dateToConvert.equals("") || dateToConvert.equals("-")) return null;

        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

//    public static LocalDate localDateConverter(Date stringDate){
//
//        if(stringDate == null || stringDate.equals("") || stringDate.equals("-")) return null;
//
//        try{
////            String date = stringDate.substring(0,19);
////            if(stringDate.length() == 0) return null;
//            DateTimeFormatter formatter;
////            if(stringDate.length() == 8) {
//                formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
////            }else{
////                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////            }
//
//            LocalDate localDate = LocalDate.parse(stringDate, formatter);
//            return localDate;
//        }catch (Exception e){
//            return null;
//        }
//    }

    public Boolean isOperacional(String value){

        if(value == null || value.equals("") || value.equals("-") || value.length() ==0) return null;

        if(value.toLowerCase().equals("op")) return true;
        return false;
    }



}
