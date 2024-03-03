package logic;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HoraActual {

    public static void main(String[] args) {
        // Define los nombres de las zonas horarias y sus respectivos identificadores
        Map<String, String> zonasHorarias = new HashMap<>();
        zonasHorarias.put("Asia/Tokyo", "Hora en Tokio:");
        zonasHorarias.put("Australia/Sydney", "Hora en Sydney:");
        zonasHorarias.put("Europe/Berlin", "Hora en Berlín:");
        zonasHorarias.put("America/Denver", "Hora en Denver:");
        zonasHorarias.put("America/Chicago", "Hora en Chicago:");

        // Obtén la hora actual para cada zona horaria y muestra la hora
        for (Map.Entry<String, String> entry : zonasHorarias.entrySet()) {
            String zonaHoraria = entry.getKey();
            String nombreZonaHoraria = entry.getValue();
            ZonedDateTime horaActual = ZonedDateTime.now(java.time.ZoneId.of(zonaHoraria));
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println(nombreZonaHoraria + " " + formatoHora.format(horaActual));
        }
    }
}