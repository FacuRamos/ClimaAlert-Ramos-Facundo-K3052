package ar.edu.utn.frba.dds.utils;

public class GeneradorIdSecuencial {
  private long siguiente;

  public GeneradorIdSecuencial() {
    this(1L);
  }

  public GeneradorIdSecuencial(long valorInicial) {
    this.siguiente = valorInicial;
  }

  public long siguiente() {
    return siguiente++;
  }
}
