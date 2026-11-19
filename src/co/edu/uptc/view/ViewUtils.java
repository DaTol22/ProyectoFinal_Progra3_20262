package co.edu.uptc.view;

import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import co.edu.uptc.enums.ResourceType;
import co.edu.uptc.model.BookableResource;
import co.edu.uptc.model.ParkingSpot;
import co.edu.uptc.model.StudyRoom;
import co.edu.uptc.model.TimeRange;

final class ViewUtils {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    private ViewUtils() {
    }

    static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        return label;
    }

    static String typeLabel(ResourceType type) {
        if (type == ResourceType.PARKING) {
            return "Parqueadero";
        }
        if (type == ResourceType.STUDY_ROOM) {
            return "Sala de estudio";
        }
        if (type == ResourceType.COURT) {
            return "Cancha";
        }
        return type.name();
    }

    static JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        return spinner;
    }

    static JSpinner createTimeSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        return spinner;
    }

    static void setTime(JSpinner spinner, LocalTime time) {
        spinner.setValue(Date.from(LocalDate.now().atTime(time).atZone(ZoneId.systemDefault()).toInstant()));
    }

    static LocalDate getDate(JSpinner spinner) {
        return ((Date) spinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    static LocalTime getTime(JSpinner spinner) {
        return ((Date) spinner.getValue()).toInstant().atZone(ZoneId.systemDefault())
                .toLocalTime().withSecond(0).withNano(0);
    }

    static String formatRange(TimeRange range) {
        boolean sameDay = range.getStartTime().toLocalDate().equals(range.getEndTime().toLocalDate());
        return range.getStartTime().format(DATE_TIME) + " → "
                + range.getEndTime().format(sameDay ? TIME : DATE_TIME);
    }

    static String describe(BookableResource resource) {
        String extra = "";
        if (resource instanceof ParkingSpot) {
            extra = " · $" + ((ParkingSpot) resource).getRatePerHour() + "/hora";
        } else if (resource instanceof StudyRoom) {
            extra = " · franjas de " + ((StudyRoom) resource).getSlotDuration().toMinutes() + " min";
        }
        return resource.getName() + " (" + resource.getId() + ") · " + typeLabel(resource.getType())
                + " · " + resource.getHours().getOpens().format(TIME) + "–"
                + resource.getHours().getCloses().format(TIME) + extra;
    }
}