package smart_houses;

import java.util.Comparator;

public class CompareFaturaConsumo implements Comparator<Fatura> {

    /**
     * @param o1
     * @param o2
     * @return
     */
    public int compare(Fatura o1, Fatura o2) {
        return Double.compare(o1.getConsumo(), o2.getConsumo());
    }
}
