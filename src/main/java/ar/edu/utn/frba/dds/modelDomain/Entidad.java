package ar.edu.utn.frba.dds.modelDomain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Entidad {
  private Long id;
  @Setter(AccessLevel.NONE)
  private List<MedioDeContacto> mediosDeContactos = new ArrayList<>();
  private String nombre;


  public void agregarMedioDeContacto(MedioDeContacto medioDeContacto){
    this.mediosDeContactos.add(medioDeContacto);
  }
}
