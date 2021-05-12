package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;

public class Validaciones {
    public static void esMontoValido (double saldoAValidar) {
        if (saldoAValidar <= 0) {
            throw new MontoNegativoException(saldoAValidar + ": el monto a ingresar debe ser un valor positivo");
        }
    }

    public static void validarLimiteDeDepositos (Cuenta cuenta) {
        if (cuenta.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
            throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
        }
    }

    public static void validarSaldoSuficiente(double saldoAValidar, Cuenta cuenta) {
        if (cuenta.getSaldo() - saldoAValidar < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + cuenta.getSaldo() + " $");
        }
    }

    public static void validarLimiteExtracción(double saldoAValidar, Cuenta cuenta) {
        double limite = 1000 - cuenta.getMontoExtraidoA(LocalDate.now());

        if (saldoAValidar > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
                + " diarios, límite: " + limite);
        }
    }
}
