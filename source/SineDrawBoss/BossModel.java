
/**
 * Write a description of class SineModel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BossModel
{
    private int frequency, amplitude, phaseShift;

    public BossModel(int frequency, int amplitude, int phaseShift)
    {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phaseShift = phaseShift;
    }
    
    public int getFrequency()
    {
        return frequency;
    }
    
    public int getAmplitude()
    {
        return amplitude;
    }
    
    public int getPhaseShift()
    {
        return phaseShift;
    }

    public void setPhaseShift(int phaseShift)
    {
        this.phaseShift = phaseShift;
    }

    public void setAmplitude(int amplitude)
    {
        this.amplitude = amplitude;
    }    
    
    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }
}
