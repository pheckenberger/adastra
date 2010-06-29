package com.idragon.adastra.dynatable;

/**
 * DynaTable evaluator interface.
 *
 * @author  iDragon
 *
 * @param   <T>  Evaluation result type.
 */
public interface DynaTableModelEvaluator<T> {

    /**
     * Evaluate table model attribute.
     *
     * @param   dynaTableModel  Table model.
     *
     * @return  Evaluation result.
     */
    T evaluate(DynaTableModel dynaTableModel);
}
