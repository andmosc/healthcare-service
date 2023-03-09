import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicalServiceImplTest {

    PatientInfoRepository patientInfoRepository = mock(PatientInfoFileRepository.class);
    SendAlertService sendAlertService = mock(SendAlertServiceImpl.class);
    MedicalServiceImpl medicalService = mock(MedicalServiceImpl.class);

    @BeforeEach
    public void setUpEach() {
        System.out.println("test started");
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
    }

    @AfterEach
    public void finished() {
        System.out.println("test completed");
    }


    @Test
    void testCheckBloodPressureMessageOutput() {
        String patientId = "123";
        String expected = "Warning, patient with id: null, need help";
        BloodPressure currentPressure = new BloodPressure(60, 120);

        when(patientInfoRepository.getById(anyString())).thenReturn(
                new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
        );

        medicalService.checkBloodPressure(patientId, currentPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(sendAlertService, atLeastOnce()).send(argumentCaptor.capture());

        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    void testCheckBloodPressureNoMessageOutput() {
        String patientId = "123";
        BloodPressure currentPressure = new BloodPressure(60, 120);

        when(patientInfoRepository.getById(anyString())).thenReturn(
                new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
        );
        medicalService.checkBloodPressure(patientId, currentPressure);
        verify(sendAlertService).send(anyString());
    }

    @Test
    void checkTemperatureMessageOutput() {
        String patientId = "123";
        String expected = "Warning, patient with id: null, need help";

        when(patientInfoRepository.getById(anyString())).thenReturn(
                new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
        );

        medicalService.checkTemperature(patientId, new BigDecimal("35"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(sendAlertService, atLeastOnce()).send(argumentCaptor.capture());

        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    void checkTemperatureNoMessageOutput() {
        String patientId = "123";

        when(patientInfoRepository.getById(anyString())).thenReturn(
                new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
        );
        medicalService.checkTemperature(patientId, new BigDecimal("36.6"));
        verify(sendAlertService, never()).send(anyString());
    }

    @Test
    public void getPatientInfoException() {
        String patientId = "123";
        String errMessage = "Patient not found";
        try {
            medicalService.getPatientInfo(patientId);
        } catch (RuntimeException e) {
            Assertions.assertEquals(errMessage, e.getMessage());
        }
    }

    @Test
    public void getPatientInfoNoException() {
        when(patientInfoRepository.getById(anyString())).thenReturn(
                new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
        );
        Assertions.assertEquals(patientInfoRepository.getById(anyString()),medicalService.getPatientInfo(anyString()));
    }

    private PatientInfo getPatientInfo(String patientId) {
        PatientInfo patientInfo = patientInfoRepository.getById(patientId);
        if (patientInfo == null) {
            throw new RuntimeException("Patient not found");
        }
        return patientInfo;
    }

}
