import java.awt.*;


public class CPictBar extends Panel
{
    // public variables

    CPictButton m_button[];
    int         m_tot;
    boolean     m_fst = true;

    // Constructor
    public CPictBar(Image ups[], Image downs[], Image disabs[],
                    int tot)
    {
        m_tot = tot;
        // Ups must not be null.  If nulls sent for other states,
        // use the ups images...
        if (ups == null)
            return;

        if (downs   == null) downs  = ups;
        if (disabs  == null) disabs = ups;

        //setTitle("");
        setLayout(new FlowLayout());

        // get memory for buttons
        m_button = new CPictButton[tot];

        // create button objects
        for (int i = 0; i < tot; i++)
        {
            m_button[i] = new CPictButton(ups[i], downs[i], disabs[i]);
            if (m_button[i] != null)
                add (m_button[i]);
        }

        //setResizable(res);
    }

/*    public void paint(Graphics g)
    {
        if (allLoaded())
        {
            if (m_fst)
            {
                m_fst = false;
                pack();
            }
        }
    }      */

    public void enableAll() {
        for (int i = 0; i < m_tot; i++) {
            m_button[i].enable();
        }
    }

    public void enable(int i) {
        m_button[i].enable();
    }

    public void disable(int i) {
        m_button[i].disable();
    }

    public void highlight(int i) {
        m_button[i].SetState(2);
    }

    public void highlightPermanent(int i) {
        m_button[i].SetState(4);
    }

    public void unHighlight(int i) {
        m_button[i].SetState(1);
    }

    public boolean allLoaded()
    {
        for (int i = 0; i < m_tot; i++)
        {
            if (!m_button[i].m_loaded)
                return false;
        }
        return true;
    }
}




