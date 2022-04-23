package com.example.healthadvisors.service;


import com.example.healthadvisors.entity.Appointment;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final MailService mailService;


    List<Appointment> findAppointmentsByDoctorId(int id) {

        return appointmentRepository.findAllByDoctor_Id(id);
    }

    List<Appointment>findAppointmentByPatientId(int id){

        return appointmentRepository.findAllByPatientId(id);
    }
    List<Appointment>findAppointmentByDate(Date date){
        return appointmentRepository.findAppointmentsByAppointmentDate(date);
    }

    public void deleteAppointmentById(int appointmentId){
        appointmentRepository.deleteById(appointmentId);
    }


    public void newAppointment(int doctorId, String appointmentDate, User user) {
        Doctor doctor = doctorService.findDoctorById(doctorId);
        Patient patient = user.getPatient();
        Appointment newAppointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(LocalDateTime.parse(appointmentDate))
                .build();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);

        appointmentRepository.save(newAppointment);

        mailService.sendAppointmentEmail(doctor, patient, newAppointment);
    }
}
