
/**
 * Holds the data for the current shape of the sine wave
 */
public class WorkerModel
{
    private int frequency, amplitude, phaseShift;
    private int targetPhaseShift, targetAmplitude, targetFrequency;
    static final int PHASE_INIT = 0, AMP_INIT = 50, FREQUENCY_INIT = 5;    
    /**
     * Constructor for objects of class WorkerModel
     */
    public WorkerModel()
    {
        this.frequency = FREQUENCY_INIT;
        targetFrequency = FREQUENCY_INIT;
        this.amplitude = AMP_INIT;
        targetAmplitude = AMP_INIT;
        this.phaseShift = PHASE_INIT;
        targetPhaseShift = PHASE_INIT;
    }

    /**
     * Return the current frequency.
     * 
     * @return int The current frequency
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
     * Adjusts the phase shift one closer to the target.
     * This allows for smoother transitions.
     */
    public void adjustPhaseShift()
    {
        if (phaseShift > targetPhaseShift) phaseShift--;
        else if (phaseShift < targetPhaseShift) phaseShift++;
    }

    /**
     * Adjusts the amplitude one closer to the target.
     * This allows for smoother transitions.
     */
    public void adjustAmplitude()
    {
        if (amplitude > targetAmplitude) amplitude--;
        else if (amplitude < targetAmplitude) amplitude++;
    }

    /**
     * Adjusts the frequency one closer to the target.
     * This allows for smoother transitions.
     */
    public void adjustFrequency()
    {
        if (frequency > targetFrequency) frequency--;
        else if (frequency < targetFrequency) frequency++;
    }

    /**
     * Update the target frequency.
     * 
     * @param targetFrequency The latest target frequency.
     */
    private void setTargetFrequency(int targetFrequency)
    {
        this.targetFrequency = targetFrequency;
    }
    
    /**
     * Update the target amplitude.
     * 
     * @param targetAmplitude The latest target amplitude.
     */
    private void setTargetAmplitude(int targetAmplitude)
    {
        this.targetAmplitude = targetAmplitude;
    }
    
    /**
     * Update the target phase shift.
     * 
     * @param phaseShift The latest target phase shift.
     */
    private void setTargetPhaseShift(int targetPhaseShift)
    {
        this.targetPhaseShift = targetPhaseShift;
    }

    /**
     * Updates the sine wave's frequency, amplitude, and phase shift.
     * Sets targets to allow for smoother transitions.
     * 
     * @param frequency The latest frequency.
     * @param amplitude The latest amplitude.
     * @param phaseShift The latest phase shift.
     */
    public void setWave(int frequency, int amplitude, int phaseShift)
    {
        setTargetFrequency(frequency);
        setTargetAmplitude(amplitude);
        setTargetPhaseShift(phaseShift);
    }
}
