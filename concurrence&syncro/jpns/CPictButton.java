import java.awt.*;

public class CPictButton extends Canvas
{
    // public variables

    Image         m_up, m_down, m_disabled, m_img;
    int           m_state;
    MediaTracker  m_trk;
    boolean       m_loaded;

    public CPictButton(Image up, Image down, Image disabled)
    {
        // Up must not be null.  If nulls sent for other states,
        // use the up image...
        if (up != null)
            m_up = up;
        else
            return;

        m_trk = new MediaTracker(this);

        m_down      = (down     == null) ? up : down;
        m_disabled  = (disabled == null) ? up : disabled;

        m_trk.addImage(m_up       ,1);
        m_trk.addImage(m_down     ,1);
        m_trk.addImage(m_disabled ,1);

        // now set the initial state to up...
        m_state = 1;
        m_img   = m_up;

        try
        {
            m_trk.waitForID(1);
        }
        catch(InterruptedException e)
        {
            return;
        }

        m_loaded = true;
        resize( m_up.getWidth(this),m_up.getHeight(this));
        repaint();

    }

    // called to set the button state and change image
    public void SetState(int state)
    {
        m_state = state;
        switch (state)
        {
            default:
            case 1: m_img   = m_up;       break;
            case 2: m_img   = m_down;     break;
            case 3: m_img   = m_disabled; break;
	    case 4: m_img   = m_down;     break;
        }
        repaint();
    }

    // called to determine state
    public boolean isEnabled()
    {
        if (m_state == 3) return false;
        return true;
    }

    // called to change to enabled state
    public synchronized void enable()
    {
        SetState(1);
    }

    // called to change to disabled state
    public synchronized void disable()
    {
        SetState(3);
    }

    // mouse event to handle for button state
    public boolean mouseUp(Event event, int x, int y)
    {
        if (m_state == 3 /* disabled */) return true;
        if (m_state != 4) SetState(1);
        return false;
    }

    // mouse event to handle for button state
    public boolean mouseDown(Event event, int x, int y)
    {
        if (m_state == 3 /* disabled */) return true;
        SetState(2);
        return false;
    }


    // paint image
    public void paint(Graphics g)
    {
        if (m_img != null)
        {
            g.drawImage(m_img, 0, 0, this);
        }
    }
    // update
    public void update(Graphics g)
    {
        paint (g);
    }

}
