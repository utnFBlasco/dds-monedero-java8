package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void agregarMovimiento(Movimiento movimientoNuevo) {
    movimientos.add(movimientoNuevo);
  }

  public void poner(double saldoADepositar) {
    Validaciones.esMontoValido(saldoADepositar);
    Validaciones.validarLimiteDeDepositos(this);
    this.saldo += saldoADepositar;
    agregarMovimiento(new Movimiento(LocalDate.now(), saldoADepositar, true));
  }

  public void sacar(double saldoAExtraer) {
    Validaciones.esMontoValido(saldoAExtraer);
    Validaciones.validarSaldoSuficiente(saldoAExtraer, this);
    Validaciones.validarLimiteExtracción(saldoAExtraer, this);
    this.saldo -= saldoAExtraer;
    agregarMovimiento(new Movimiento(LocalDate.now(), saldoAExtraer, false));
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
