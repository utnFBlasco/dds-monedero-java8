package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

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

  public void esMontoValido (double saldoAValidar) {
    if (saldoAValidar <= 0) {
      throw new MontoNegativoException(saldoAValidar + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarLimiteDeDepositos () {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validarSaldoSuficiente(double saldoAValidar) {
    if (getSaldo() - saldoAValidar < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void validarLimiteExtracción(double saldoAValidar) {
    double limite = 1000 - getMontoExtraidoA(LocalDate.now());

    if (saldoAValidar > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

  public void poner(double saldoADepositar) {
    esMontoValido(saldoADepositar);
    validarLimiteDeDepositos();
    agregarMovimiento(new Movimiento(LocalDate.now(), saldoADepositar, true));
  }

  public void sacar(double saldoAExtraer) {
    esMontoValido(saldoAExtraer);
    validarSaldoSuficiente(saldoAExtraer);
    validarLimiteExtracción(saldoAExtraer);
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
