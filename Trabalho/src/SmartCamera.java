public class SmartCamera extends SmartDevice{
    private int resolution;
    private int fileDim;

    public SmartCamera(){
        super();
        this.resolution = 0;
        this.fileDim = 0;
    }

    public SmartCamera(boolean ligado, double instalationPrice, int resolution, int fileDim){
        super(ligado, instalationPrice);
        this.resolution = resolution;
        this.fileDim = fileDim;
    }

    public SmartCamera(int id, boolean ligado, double instalationPrice, int resolution, int fileDim){
        super(ligado, id, instalationPrice);
        this.resolution = resolution;
        this.fileDim = fileDim;
    }

    public SmartCamera(SmartCamera camera){
        super(camera);
        this.resolution = camera.getResolution();
        this.fileDim = camera.getFileDim();
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getFileDim() {
        return fileDim;
    }

    public void setFileDim(int fileDim) {
        this.fileDim = fileDim;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("SmartCamera{").append("id=").append(this.getId()).append(", on=").append(this.isOn()).append(", instalationCost=").append(this.getInstalationCost()).append(", resolution=").append(this.resolution).append(", fileDim=").append(this.fileDim).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SmartCamera that = (SmartCamera) o;

        if (getResolution() != that.getResolution()) return false;
        return getFileDim() == that.getFileDim();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getResolution();
        result = 31 * result + getFileDim();
        return result;
    }

    public SmartCamera clone(){
        return new SmartCamera(this);
    }

    public double comsumption(){
        return this.resolution * this.fileDim;
    }
}
