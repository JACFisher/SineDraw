/**
 * Holds the data for the current shape of the sine wave.
 */
public class BossModel
{
    private int frequency, amplitude, phaseShift;

    /**
     * Constructor for objects of class BossModel.
     */
    public BossModel(int frequency, int amplitude, int phaseShift)
    {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phaseShift = phaseShift;
    }

    /**
     * Return the current frequency.
     * 
     * @return int The current frequency.
     */
    public int getFrequency()
    {
        return frequency;
    }

    /**
     * Return the current amplitude.
     * 
     * @return int The current amplitude.
     */
    public int getAmplitude()
    {
        return amplitude;
    }
    
    /**
     * Return the current phase shift.
     * 
     * @return int The current phase shift.
     */
    public int getPhaseShift()
    {
        return phaseShift;
    }

    /**
     * Update the sine wave's phase shift.
     * 
     * @param phaseShift The lastest phase shift.
     */
    public void setPhaseShift(int phaseShift)
    {
        this.phaseShift = phaseShift;
    }

    /**
     * Update the sine wave's amplitude.
     * 
     * @param amplitude The latest amplitude.
     */
    public void setAmplitude(int amplitude)
    {
        this.amplitude = amplitude;
    }    

    /**
     * Updates the sine wave's frequency.
     * 
     * @param frequency The latest frequency.
     */
    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }
}
