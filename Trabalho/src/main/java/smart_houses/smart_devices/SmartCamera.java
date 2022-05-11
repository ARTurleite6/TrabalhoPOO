package smart_houses.smart_devices;


public class SmartCamera extends SmartDevice {
    private int resolutionX;
    private int resolutionY;
    private int fileDim;

    public SmartCamera(){
        super();
        this.resolutionX = 0;
        this.resolutionY = 0;
        this.fileDim = 0;
    }

    public SmartCamera(boolean ligado, double consumo, int resolutionX, int resolutionY, int fileDim){
        super(ligado, consumo);
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.fileDim = fileDim;
    }

    public SmartCamera(SmartCamera camera){
        super(camera);
        this.resolutionX = camera.getResolutionX();
        this.resolutionY = camera.getResolutionY();
        this.fileDim = camera.getFileDim();
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(int resolutionX) {
        this.resolutionX = resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
    }

    public int getFileDim() {
        return fileDim;
    }

    public void setFileDim(int fileDim) {
        this.fileDim = fileDim;
    }

    @Override
    public String toString() {
        return "SmartCamera{" +
                "id=" + this.getId() +
                ", on=" + this.isOn() +
                ", resolutionX=" + resolutionX +
                ", resolutionY=" + resolutionY +
                ", fileDim=" + fileDim +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SmartCamera that = (SmartCamera) o;

        if (getResolutionX() != that.getResolutionX()) return false;
        if (getResolutionY() != that.getResolutionY()) return false;
        return getFileDim() == that.getFileDim();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getResolutionX();
        result = 31 * result + getResolutionY();
        result = 31 * result + getFileDim();
        return result;
    }

    public SmartCamera clone(){
        return new SmartCamera(this);
    }

    public double comsumption(){
        return this.getConsume()  + (this.resolutionX * 0.001 * this.resolutionY * 0.001 * this.fileDim * 0.0001);
    }
}
