
/**
 * 
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
    
    public void adjustPhaseShift()
    {
        if (phaseShift > targetPhaseShift) phaseShift--;
        else if (phaseShift < targetPhaseShift) phaseShift++;
    }

    public void adjustAmplitude()
    {
        if (amplitude > targetAmplitude) amplitude--;
        else if (amplitude < targetAmplitude) amplitude++;
    }

    public void adjustFrequency()
    {
        if (frequency > targetFrequency) frequency--;
        else if (frequency < targetFrequency) frequency++;
    }

    private void setTargetFrequency(int targetFrequency)
    {
        this.targetFrequency = targetFrequency;
    }
    
    private void setTargetAmplitude(int targetAmplitude)
    {
        this.targetAmplitude = targetAmplitude;
    }
    
    private void setTargetPhaseShift(int targetPhaseShift)
    {
        this.targetPhaseShift = targetPhaseShift;
    }
    
    public void setWave(int frequency, int amplitude, int phaseShift)
    {
        setTargetFrequency(frequency);
        setTargetAmplitude(amplitude);
        setTargetPhaseShift(phaseShift);
    }
}
