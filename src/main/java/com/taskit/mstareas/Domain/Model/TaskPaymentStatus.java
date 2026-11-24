package com.taskit.mstareas.Domain.Model;

public enum TaskPaymentStatus {
    UNPAID, // Sin pago aún
    RETAINED, // Pagado y retenido en Escrow (listo para completar)
    RELEASED, // Dinero liberado al trabajador
    FAILED, // Pago rechazado en Mercado Pago
    REFUNDED // Reembolsado por disputa
}