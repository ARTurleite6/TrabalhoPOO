import java.util.Objects;

public class Fornecedor {

    private double basePrice;
    private String name;
    private double tax;


    public Fornecedor() {
        this.basePrice = 0;
        this.name = "n/a";
        this.tax = 0;
    }
    public Fornecedor(double basePrice, String name, double tax){
        this.basePrice =basePrice;
        this.name = name;
        this.tax = tax;
    }
    public Fornecedor(Fornecedor fornecedor){
        this.basePrice=fornecedor.getBasePrice();
        this.name=fornecedor.getName();
        this.tax= fornecedor.getTax();
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return Double.compare(that.basePrice, basePrice) == 0 && Double.compare(that.tax, tax) == 0 && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basePrice, name, tax);
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "basePrice=" + basePrice +
                ", name='" + name + '\'' +
                ", tax=" + tax +
                '}';
    }
    public Fornecedor clone(){
        return new Fornecedor(this);
    }

}
