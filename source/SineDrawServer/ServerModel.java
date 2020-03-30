/**
 * Holds the data set by BossHandlers and sent to WorkerHandlers.  Holds data in String form.
 */
public class ServerModel
{
    String data;
    /**
     * Constructor for objects of class ServerModel.
     */
    public ServerModel()
    {
        data = "0,0,0";
    }

    /**
     * Sets the data in this model.
     * 
     * @param data Data to set.
     */
    public void setData(String data)
    {
        this.data = data;
    }

    /**
     * Fetches the data in this model.
     * 
     * @return data Data in this model.
     */
    public String getData()
    {
        return data;
    }
}
