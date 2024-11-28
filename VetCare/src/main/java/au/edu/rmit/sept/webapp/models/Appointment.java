package au.edu.rmit.sept.webapp.models;

public class Appointment {
    private Long appointment_id;
    private Long pet_id;
    private Long vet_id;
    private Long user_id;
    private Long clinic_id;
    private String appointment_date;
    private String appointment_time;

    public Appointment() {
    }

    public Appointment(Long appointment_id, Long pet_id, Long vet_id, Long user_id, Long clinic_id,
            String appointment_date, String appointment_time) {
        this.appointment_id = appointment_id;
        this.pet_id = pet_id;
        this.vet_id = vet_id;
        this.user_id = user_id;
        this.clinic_id = clinic_id;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
    }

    public Long getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(Long appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Long getPet_id() {
        return pet_id;
    }

    public void setPet_id(Long pet_id) {
        this.pet_id = pet_id;
    }

    public Long getVet_id() {
        return vet_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setVet_id(Long vet_id) {
        this.vet_id = vet_id;
    }

    public Long getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(Long clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }
}
