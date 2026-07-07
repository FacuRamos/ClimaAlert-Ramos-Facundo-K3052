package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dto.DatosActualesClima;
import ar.edu.utn.frba.dds.dto.DatosClima;
import ar.edu.utn.frba.dds.modelDomain.AdapterNotificadorMail;
import ar.edu.utn.frba.dds.modelDomain.Entidad;
import ar.edu.utn.frba.dds.modelDomain.MedioDeContacto;
import ar.edu.utn.frba.dds.modelDomain.Notificador;
import ar.edu.utn.frba.dds.repository.ControlDelClimaRepository;
import ar.edu.utn.frba.dds.repository.EntidadRepository;
import ar.edu.utn.frba.dds.services.ControlDeClimaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Con el asterisco importamos todos los métodos estáticos de Mockito (mock, when, verify, times, eq, etc.)
import static org.mockito.Mockito.*;

public class TestClimaAlert {

    private ControlDelClimaRepository controlDelClimaRepository;
    private EntidadRepository entidadRepository;
    private AdapterNotificadorMail adapterNotificadorMail;
    private ControlDeClimaServiceImpl controlClimaService;

    @BeforeEach
    public void setUp() {
        // mock(objetoAmockear.class) con esto le digo que tiene que mockear un objeto que cumpla con las reglas del objeto que le paso como argumento
        controlDelClimaRepository = mock(ControlDelClimaRepository.class);
        entidadRepository = mock(EntidadRepository.class);
        adapterNotificadorMail = mock(AdapterNotificadorMail.class);

        // Instanciamos el notificador inyectando el adaptador mockeado
        Notificador notificador = new Notificador(adapterNotificadorMail);

        // Instanciamos el servicio a testear pasándole sus dependencias mockeadas
        controlClimaService = new ControlDeClimaServiceImpl(
                entidadRepository,
                controlDelClimaRepository,
                null, // No requerimos restDatosClimasProperties para controlarAlertaDeClima
                null, // No requerimos restTemplate para controlarAlertaDeClima
                notificador
        );
    }

    @Test
    public void testControlarAlertaDeClima_CondicionCumplida_EnviaMail() throws IOException {
        // 1. ARRANGE (Preparar el escenario)
        // Temperatura > 5 y Humedad > 10 (Se cumple la condición)
        DatosActualesClima datosActuales = new DatosActualesClima(15.0, 30.0);
        DatosClima datosClima = new DatosClima(datosActuales);

        // when(objetoAMocker.metodo).thenReturn(dato) con esto le digo que cuando llamen al objeto con tal metodo debe retornar ese valor
        when(controlDelClimaRepository.buscarUltimo()).thenReturn(Optional.of(datosClima));

        // Creamos una entidad con su medio de contacto (mail)
        Entidad entidad = new Entidad();
        entidad.setNombre("Facundo");
        MedioDeContacto medio = new MedioDeContacto("facundo@example.com");
        entidad.agregarMedioDeContacto(medio);

        when(entidadRepository.buscarTodos()).thenReturn(List.of(entidad));

        // 2. ACT (Ejecutar la acción)
        controlClimaService.controlarAlertaDeClima();

        // 3. ASSERT (Verificar)
        // Verificamos que se haya invocado el método enviar del adaptador de mail exactamente 1 vez
        verify(adapterNotificadorMail, times(1)).enviar(
                eq("facundo@example.com"),
                eq("Alerta de clima"),
                anyString()
        );
    }

    @Test
    public void testControlarAlertaDeClima_CondicionNoCumplida_NoEnviaMail() throws IOException {
        // 1. ARRANGE (Preparar el escenario)
        // Temperatura <= 5 o Humedad <= 10 (No se cumple la condición)
        DatosActualesClima datosActuales = new DatosActualesClima(4.0, 5.0);
        DatosClima datosClima = new DatosClima(datosActuales);

        when(controlDelClimaRepository.buscarUltimo()).thenReturn(Optional.of(datosClima));

        // Creamos una entidad con su medio de contacto (mail)
        Entidad entidad = new Entidad();
        entidad.setNombre("Facundo");
        MedioDeContacto medio = new MedioDeContacto("facundo@example.com");
        entidad.agregarMedioDeContacto(medio);

        when(entidadRepository.buscarTodos()).thenReturn(List.of(entidad));

        // 2. ACT (Ejecutar la acción)
        controlClimaService.controlarAlertaDeClima();

        // 3. ASSERT (Verificar)
        // Verificamos que NO se haya invocado el método enviar
        verify(adapterNotificadorMail, never()).enviar(anyString(), anyString(), anyString());
    }

    @Test
    public void testControlarAlertaDeClima_NoReenviaSiYaFueEnviado() throws IOException {
        // ARRANGE
        DatosActualesClima datosActuales = new DatosActualesClima(15.0, 30.0);
        DatosClima datosClima = new DatosClima(datosActuales);

        when(controlDelClimaRepository.buscarUltimo()).thenReturn(Optional.of(datosClima));

        Entidad entidad = new Entidad();
        entidad.setNombre("Facundo");
        MedioDeContacto medio = new MedioDeContacto("facundo@example.com");
        entidad.agregarMedioDeContacto(medio);

        when(entidadRepository.buscarTodos()).thenReturn(List.of(entidad));

        // ACT
        // Primera vez: Debe enviar el mail
        controlClimaService.controlarAlertaDeClima();
        // Segunda vez: No debe enviar el mail de nuevo
        controlClimaService.controlarAlertaDeClima();

        // ASSERT
        // Verificamos que se haya invocado enviar EXACTAMENTE 1 vez en total
        verify(adapterNotificadorMail, times(1)).enviar(
                eq("facundo@example.com"),
                eq("Alerta de clima"),
                anyString()
        );
    }
}
