package telegram.utilitarios;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utilitarios {

    private static final String FORMATO_DATA_BRASILEIRO = "dd/MM/yyyy HH:mm";
    private static final SimpleDateFormat FORMATO_HORA_BRASILEIRO = new SimpleDateFormat("HH:mm");
    private static final DateTimeFormatter formatadorBrasileiro = DateTimeFormatter.ofPattern(FORMATO_DATA_BRASILEIRO);


    public static String formatarData(LocalDateTime dataASerFormatada) throws Exception {

        try {

            return dataASerFormatada.format(formatadorBrasileiro);

        } catch (Exception e) {
            System.out.println("Erro ao converter data para formato de Data Brasileiro");
        }

        return "";

    }
}
